CREATE OR REPLACE VIEW AV_MV_QUERY_MERGE_TOKENS AS
SELECT mv.name AS "NAME"
,'SELECT '||(LISTAGG('s.'||atc.column_name, ',') WITHIN GROUP (ORDER BY atc.column_name))||',s.'||mv.deletable_column||',s.identifiant FROM ('||query||') s UNION ALL SELECT '
||(LISTAGG('d.'||atc.column_name, ',') WITHIN GROUP (ORDER BY atc.column_name))||','''||mv.deletable_marker||''' AS '||mv.deletable_column||',d.identifiant FROM '||mv.name||' d ' AS "SELECT_"
,'LEFT JOIN ('||mv.query||') s ON s.identifiant = d.identifiant WHERE s.identifiant IS NULL' AS "JOIN_"
,'INSERT('||(LISTAGG(atc.column_name, ',') WITHIN GROUP (ORDER BY atc.column_name))||','||mv.deletable_column||',identifiant) VALUES ('
||(LISTAGG('s.'||atc.column_name, ',') WITHIN GROUP (ORDER BY atc.column_name))||',s.'||mv.deletable_column||',s.identifiant)'AS "INSERT_"
,'UPDATE SET '||((LISTAGG('d.'||atc.column_name||'=s.'||atc.column_name, ',') WITHIN GROUP (ORDER BY atc.column_name)))||',d.'||mv.deletable_column||'=s.'||mv.deletable_column AS "UPDATE_"
,'DELETE WHERE d.'||mv.deletable_column||' = '''||mv.deletable_marker||'''' AS "DELETE_"
FROM at_mv mv
JOIN all_tab_columns atc ON atc.table_name = mv.name
WHERE owner = USER AND LOWER(atc.column_name) <> LOWER(mv.deletable_column) AND LOWER(atc.column_name) <> 'identifiant'
GROUP BY atc.owner,mv.name,mv.query,mv.deletable_column,mv.deletable_marker;

CREATE OR REPLACE VIEW AV_MV_QUERY_MERGE AS
SELECT name,CONCAT(CONCAT('MERGE INTO '||name||' d USING (',TO_CLOB(select_)),CONCAT(join_,CONCAT(') s ON (s.identifiant = d.identifiant) WHEN NOT MATCHED THEN '
,CONCAT(TO_CLOB(insert_),CONCAT(' WHEN MATCHED THEN ',CONCAT(TO_CLOB(update_),CONCAT(' ',TO_CLOB(delete_)))))))) AS "QUERY"
FROM AV_MV_QUERY_MERGE_TOKENS t;

-- Liste des dépenses
CREATE OR REPLACE VIEW VA_DEPENSE AS
SELECT version_collectif.identifiant||d.activite_code||d.nature_economique_code||d.source_financement_code||d.bailleur_code AS "IDENTIFIANT",version_collectif.identifiant AS "VERSION_COLLECTIF",d.*
FROM UT_BIDF_TAMP.va_depense@dblink_elabo_bidf d
JOIN VMA_EXERCICE exercice ON exercice.annee = d.exercice
JOIN TA_COLLECTIF collectif ON collectif.exercice = exercice.identifiant
JOIN TA_VERSION_COLLECTIF version_collectif ON version_collectif.collectif = collectif.identifiant;

-- Liste des ressources
CREATE OR REPLACE VIEW VA_RESSOURCE AS
SELECT version_collectif.identifiant||r.activite_code||r.nature_economique_code AS "IDENTIFIANT",version_collectif.identifiant AS "VERSION_COLLECTIF",r.*
FROM UT_BIDF_TAMP.va_ressource@dblink_elabo_bidf r
JOIN VMA_EXERCICE exercice ON exercice.annee = r.exercice
JOIN TA_COLLECTIF collectif ON collectif.exercice = exercice.identifiant
JOIN TA_VERSION_COLLECTIF version_collectif ON version_collectif.collectif = collectif.identifiant;


-- Liste des dépenses ayant au moins un mouvement inclus
CREATE OR REPLACE VIEW VA_DEPENSE_MOUVEMENT_INCLUS AS
SELECT depense.identifiant,SUM(NVL(acte_gestion_depense.montant_ae,0)) AS "AE",SUM(NVL(acte_gestion_depense.montant_cp,0)) AS "CP"
FROM ta_depense depense
JOIN ta_version_collectif version_collectif ON version_collectif.identifiant = depense.version_collectif
JOIN ta_collectif collectif ON collectif.identifiant = version_collectif.collectif
LEFT JOIN vma_exercice exercice ON exercice.identifiant = collectif.exercice
LEFT JOIN vma_depense vdepense ON vdepense.version_collectif = version_collectif.identifiant AND vdepense.activite_identifiant = depense.activite 
AND vdepense.nature_economique_identifiant = depense.nature_economique AND vdepense.source_financement_identifiant = depense.source_financement 
AND vdepense.bailleur_identifiant = depense.bailleur
LEFT JOIN vma_acte_gestion_depense acte_gestion_depense ON acte_gestion_depense.exercice = exercice.annee AND acte_gestion_depense.activite = depense.activite 
AND acte_gestion_depense.nature_economique = depense.nature_economique AND acte_gestion_depense.source_financement = depense.source_financement AND acte_gestion_depense.bailleur = depense.bailleur
LEFT JOIN vma_acte_gestion acte_gestion ON acte_gestion.identifiant = acte_gestion_depense.acte AND acte_gestion.exercice = exercice.annee
JOIN ta_acte_gestion_v_collectif acte_gestion_v_collectif ON acte_gestion_v_collectif.acte_gestion = acte_gestion.identifiant AND acte_gestion_v_collectif.version_collectif = version_collectif.identifiant
AND acte_gestion_v_collectif.inclus = 1
GROUP BY depense.identifiant;

-- Liste des dépenses importées
CREATE OR REPLACE VIEW VA_DEPENSE_IMPORTEE AS
SELECT depense.*
FROM VMA_DEPENSE depense
JOIN TA_DEPENSE tdepense ON tdepense.identifiant = depense.identifiant;

-- Liste des dépenses importables
CREATE OR REPLACE VIEW VA_DEPENSE_IMPORTABLE AS
SELECT depense.*
FROM VMA_DEPENSE depense
WHERE depense.identifiant NOT IN (SELECT depense_importee.identifiant FROM VA_DEPENSE_IMPORTEE depense_importee);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

-- Liste des ressources importées
CREATE OR REPLACE VIEW VA_RESSOURCE_IMPORTEE AS
SELECT ressource.*
FROM VMA_RESSOURCE ressource
JOIN TA_RESSOURCE tressource ON tressource.identifiant = ressource.identifiant;

-- Liste des ressources importables
CREATE OR REPLACE VIEW VA_RESSOURCE_IMPORTABLE AS
SELECT ressource.*
FROM VMA_RESSOURCE ressource
WHERE ressource.identifiant NOT IN (SELECT ressource_importee.identifiant FROM VA_RESSOURCE_IMPORTEE ressource_importee);