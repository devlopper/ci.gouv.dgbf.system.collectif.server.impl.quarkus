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