-- Liste des dépenses
CREATE OR REPLACE VIEW VA_DEPENSE AS
SELECT
	TO_NUMBER(ld.exo_num) AS "EXERCICE",ld.ldep_id AS "LDEP_ID",fd.find_id AS "FIND_ID"
	,a.ads_id AS "ACTIVITE_IDENTIFIANT",a.ads_code AS "ACTIVITE_CODE",a.ads_code||' '||a.ads_liblg AS "ACTIVITE_CODE_LIBELLE"
	,ne.nat_id AS "NATURE_ECONOMIQUE_IDENTIFIANT",ne.nat_code AS "NATURE_ECONOMIQUE_CODE",ne.nat_code||' '||ne.nat_liblg AS "NATURE_ECONOMIQUE_CODE_LIBELLE"
	,sf.sfin_id AS "SOURCE_FINANCEMENT_IDENTIFIANT",sf.sfin_code AS "SOURCE_FINANCEMENT_CODE",sf.sfin_code||' '||sf.sfin_liblg AS "SF_CODE_LIBELLE"
	,b.bai_id AS "BAILLEUR_IDENTIFIANT",b.bai_code AS "BAILLEUR_CODE",b.bai_code||' '||b.bai_designation AS "BAILLEUR_CODE_LIBELLE"
	,nd.ndep_id AS "NATURE_DEPENSE_IDENTIFIANT",nd.ndep_code AS "NATURE_DEPENSE_CODE",nd.ndep_code||' '||nd.ndep_liblg AS "NATURE_DEPENSE_CODE_LIBELLE"
	,ac.adp_id AS "ACTION_IDENTIFIANT",ac.adp_code AS "ACTION_CODE",ac.adp_code||' '||ac.adp_liblg AS "ACTION_CODE_LIBELLE"
	,u.usb_id AS "USB_IDENTIFIANT",u.usb_code AS "USB_CODE",u.usb_code||' '||u.usb_liblg AS "USB_CODE_LIBELLE"
	,s.secb_id AS "SECTION_IDENTIFIANT",s.secb_num AS "SECTION_CODE",s.secb_num||' '||s.secb_liblg AS "SECTION_CODE_LIBELLE"
	,fd.FIND_BVOTE_AE AS "BUDGET_INITIAL_AE",fd.FIND_BVOTE_CP AS "BUDGET_INITIAL_CP"
	,fd.FIND_MONTANT_AE - fd.FIND_BVOTE_AE AS "MOUVEMENT_AE",fd.FIND_MONTANT_CP - fd.FIND_BVOTE_CP AS "MOUVEMENT_CP"
	,fd.FIND_MONTANT_AE AS "BUDGET_ACTUEL_AE",fd.FIND_MONTANT_CP AS "BUDGET_ACTUEL_CP"
FROM financement_depenses@dblink_elabo_bidf fd
LEFT JOIN ligne_de_depenses@dblink_elabo_bidf ld ON ld.ldep_id = fd.ldep_id AND ld.exo_num = fd.exo_num
LEFT JOIN activite_de_service@dblink_elabo_bidf a ON a.ads_id = ld.ads_id
LEFT JOIN nature_depenses@dblink_elabo_bidf nd ON nd.ndep_id = a.ndep_id
LEFT JOIN nature_economique@dblink_elabo_bidf ne ON ne.nat_id = ld.nat_id
LEFT JOIN source_financement@dblink_elabo_bidf sf ON sf.sfin_id = fd.sfin_id
LEFT JOIN bailleur@dblink_elabo_bidf b ON b.bai_id = fd.bai_id
LEFT JOIN action@dblink_elabo_bidf ac ON ac.adp_id = a.adp_id
LEFT JOIN unite_spec_bud@dblink_elabo_bidf u ON u.usb_id = ac.usb_id
LEFT JOIN section_budgetaire@dblink_elabo_bidf s ON s.secb_id = u.secb_id
WHERE ld.ads_id IS NOT NULL AND ld.nat_id IS NOT NULL AND fd.sfin_id IS NOT NULL AND fd.bai_id IS NOT NULL;

-- Liste des ressources
CREATE OR REPLACE VIEW VA_RESSOURCE AS
SELECT 
	TO_NUMBER(lr.exercice) AS "EXERCICE",lr.rec_id AS "REC_ID"
	,a.ads_code AS "ACTIVITE_IDENTIFIANT",a.ads_code AS "ACTIVITE_CODE",a.ads_code||' '||a.ads_liblg AS "ACTIVITE_CODE_LIBELLE"
	,ne.nat_id AS "NATURE_ECONOMIQUE_IDENTIFIANT",ne.nat_code AS "NATURE_ECONOMIQUE_CODE",ne.nat_code||' '||ne.nat_liblg AS "NATURE_ECONOMIQUE_CODE_LIBELLE"
	,u.usb_id AS "USB_IDENTIFIANT",u.usb_code AS "USB_CODE",u.usb_code||' '||u.usb_liblg AS "USB_CODE_LIBELLE"
	,s.secb_id AS "SECTION_IDENTIFIANT",s.secb_num AS "SECTION_CODE",s.secb_num||' '||s.secb_liblg AS "SECTION_CODE_LIBELLE"
	,lr.MONTANT AS "BUDGET_INITIAL",lr.MONTANT AS "BUDGET_ACTUEL",0 AS "MOUVEMENT"
FROM ligne_recette@dblink_elabo_bidf lr
LEFT JOIN ut_bidf_tamp.activite_de_recette@dblink_elabo_bidf a ON a.ads_code = lr.ads_id
LEFT JOIN nature_economique@dblink_elabo_bidf ne ON ne.nat_id = lr.nat_id
LEFT JOIN unite_spec_bud@dblink_elabo_bidf u ON u.usb_code = a.usb_code
LEFT JOIN section_budgetaire@dblink_elabo_bidf s ON s.secb_id = u.secb_id
WHERE lr.ads_id IS NOt NULL AND lr.nat_id IS NOT NULL;


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