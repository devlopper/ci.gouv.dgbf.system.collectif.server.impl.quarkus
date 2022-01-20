CREATE OR REPLACE VIEW VA_LIGNE_DEPENSE AS
SELECT
    fd.find_id AS "IDENTIFIANT"
    ,nd.ndep_id AS "ND_IDENTIFIANT",nd.ndep_code AS "ND_CODE",nd.ndep_code||' '||nd.ndep_liblg AS "ND_CODE_LIBELLE"
    ,s.secb_id AS "SECTION_IDENTIFIANT",s.secb_num AS "SECTION_CODE",s.secb_num||' '||s.secb_liblg AS "SECTION_CODE_LIBELLE"
    ,ua.ua_id AS "UA_IDENTIFIANT",ua.ua_code AS "UA_CODE",ua.ua_code||' '||ua.ua_liblg AS "UA_CODE_LIBELLE"
    ,u.usb_id AS "USB_IDENTIFIANT",u.usb_code AS "USB_CODE",u.usb_code||' '||u.usb_liblg AS "USB_CODE_LIBELLE"
    ,a.adp_id AS "ACTION_IDENTIFIANT",a.adp_code AS "ACTION_CODE",a.adp_code||' '||a.adp_liblg AS "ACTION_CODE_LIBELLE"
    ,ads.ads_id AS "ACTIVITE_IDENTIFIANT",ads.ads_code AS "ACTIVITE_CODE",ads.ads_code||' '||ads.ads_liblg AS "ACTIVITE_CODE_LIBELLE"
    ,ne.nat_id AS "NE_IDENTIFIANT",ne.nat_code AS "NE_CODE",ne.nat_code||' '||ne.nat_liblg AS "NE_CODE_LIBELLE"
    ,sf.sfin_id AS "SF_IDENTIFIANT",sf.sfin_code AS "SF_CODE",sf.sfin_code||' '||sf.sfin_liblg AS "SF_CODE_LIBELLE"
    ,b.bai_id AS "BAILLEUR_IDENTIFIANT",b.bai_code AS "BAILLEUR_CODE",b.bai_code||' '||b.bai_designation AS "BAILLEUR_CODE_LIBELLE"
    ,ca.catv_id AS "CA_IDENTIFIANT",ca.catv_code AS "CA_CODE",ca.catv_code||' '||ca.catv_libelle AS "CA_CODE_LIBELLE"
    ,cb.uuid AS "CB_IDENTIFIANT",cb.cbud_code AS "CB_CODE",cb.cbud_code||' '||cb.cbud_liblg AS "CB_CODE_LIBELLE"
    ,fd.find_bvote_ae AS "BUDGET_INITIAL_AE",fd.find_bvote_cp AS "BUDGET_INITIAL_CP"
    ,fd.find_montant_ae AS "BUDGET_ACTUEL_AE",fd.find_montant_cp AS "BUDGET_ACTUEL_CP"
	
FROM financement_depenses@dblink_elabo_bidf fd
JOIN ligne_de_depenses@dblink_elabo_bidf ld on ld.ldep_id = fd.ldep_id
LEFT JOIN vs_ligne_budgetaire@dblink_elabo_bidf vlb on vlb.fin_id = fd.find_id
LEFT JOIN nature_depenses@dblink_elabo_bidf nd on nd.ndep_id = ld.ndep_id
LEFT JOIN nature_economique@dblink_elabo_bidf ne on ne.nat_id = ld.nat_id
LEFT JOIN section_budgetaire@dblink_elabo_bidf s on s.secb_id = ld.secb_id
LEFT JOIN unite_administrative@dblink_elabo_bidf ua on ua.ua_id = ld.ua_id
LEFT JOIN unite_spec_bud@dblink_elabo_bidf u on u.usb_id = ld.usb_id
LEFT JOIN action@dblink_elabo_bidf a on a.adp_id = ld.adp_id
LEFT JOIN categorie_budget@dblink_elabo_bidf cb on cb.uuid = ld.cbud_id
LEFT JOIN activite_de_service@dblink_elabo_bidf ads on ads.ads_id = ld.ads_id
LEFT JOIN categorie_activite@dblink_elabo_bidf ca on ca.catv_id = ld.catv_id
LEFT JOIN source_financement@dblink_elabo_bidf sf on sf.sfin_id = fd.sfin_id
LEFT JOIN bailleur@dblink_elabo_bidf b on b.bai_id = fd.bai_id

CREATE OR REPLACE VIEW VA_LIGNE_DEPENSE_DISPONIBLE AS
SELECT
    fd.find_id AS "IDENTIFIANT"
    ,NVL(vlb.DISPONIBLE_AE,0) AS "DISPONIBLE_AE",NVL(vlb.DISPONIBLE_CP,0) AS "DISPONIBLE_CP"
FROM financement_depenses@dblink_elabo_bidf fd
LEFT JOIN vs_ligne_budgetaire@dblink_elabo_bidf vlb on vlb.fin_id = fd.find_id,

CREATE OR REPLACE VIEW VA_LIGNE_DEPENSE_MOUVEMENT AS
SELECT
	ld.identifiant AS "IDENTIFIANT"
	,ld.version_acte AS "VERSION_ACTE"
	,SUM(NVL(o.MONTANT_AE,0)) as "AE"
	,SUM(NVL(o.MONTANT_CP,0)) as "CP" 
FROM TA_LIGNE_DEPENSE ld,VMA_OPERATION_MOUVEMENT_CREDIT o,TA_MOUVEMENT_CREDIT_VERS_ACTE i 
WHERE ld.ACTIVITE=o.ACTIVITE AND ld.NATURE_ECONOMIQUE=o.NATURE_ECONOMIQUE AND ld.SOURCE_FINANCEMENT=o.SOURCE_FINANCEMENT 
AND ld.BAILLEUR=o.BAILLEUR AND ld.version_acte = i.version_acte AND o.acte = i.mouvement_credit AND i.inclus = 1
GROUP BY ld.identifiant,ld.version_acte
ORDER BY ld.identifiant,ld.version_acte ASC;

CREATE OR REPLACE VIEW VA_LIGNE_DEPENSE_MOUVEMENT AS
SELECT
	ld.identifiant AS "IDENTIFIANT"
	,ld.version_acte AS "VERSION_ACTE"
	,SUM(NVL(o.MONTANT_AE,0)) as "AE"
	,SUM(NVL(o.MONTANT_CP,0)) as "CP" 
FROM TA_LIGNE_DEPENSE ld,VMA_OPERATION_MOUVEMENT_CREDIT o,TA_MOUVEMENT_CREDIT_VERS_ACTE i 
WHERE ld.ACTIVITE=o.ACTIVITE AND ld.NATURE_ECONOMIQUE=o.NATURE_ECONOMIQUE AND ld.SOURCE_FINANCEMENT=o.SOURCE_FINANCEMENT 
AND ld.BAILLEUR=o.BAILLEUR AND ld.version_acte = i.version_acte AND o.acte = i.mouvement_credit AND i.inclus = 1
GROUP BY ld.identifiant,ld.version_acte
ORDER BY ld.identifiant,ld.version_acte ASC;

CREATE OR REPLACE VIEW VA_LIGNE_DEPENSE_BUDGET_FINAL AS   
SELECT 
    ld.identifiant as "IDENTIFIANT"
    ,ld.version_acte AS "VERSION_ACTE"
    ,NVL(vld.budget_actuel_ae,0)-NVL(m.ae,0)+NVL(ld.ajustement_ae,0) AS "AE"
    ,NVL(vld.budget_actuel_cp,0)-NVL(m.cp,0)+NVL(ld.ajustement_cp,0) AS "CP"
FROM TA_LIGNE_DEPENSE ld
LEFT JOIN VA_LIGNE_DEPENSE vld ON vld.identifiant = ld.identifiant
LEFT JOIN VA_LIGNE_DEPENSE_MOUVEMENT m ON m.identifiant = ld.identifiant AND m.version_acte = ld.version_acte;

CREATE OR REPLACE VIEW VA_MOUVEMENT_CREDIT AS
SELECT 
    fd.acte_id as identifiant,
    TO_CHAR(a.ACTE_NUMERO) AS "CODE"
    ,TO_CHAR(a.ACTE_NUMERO)||' '||a.ACTE_REF_EXTERNE_ACTE as LIBELLE
    ,sum(fd.find_montant_ae) as "MONTANT_AE"
    ,sum(fd.find_montant_cp) as "MONTANT_CP"
FROM 
    meatest.financement_depenses fd
LEFT OUTER JOIN meatest.acte_budgetaire a ON a.acte_id=fd.acte_id
GROUP BY fd.acte_id,a.ACTE_NUMERO,a.ACTE_REF_EXTERNE_ACTE;

CREATE OR REPLACE VIEW VA_MOUVEMENT_CREDIT_LIGNE AS
SELECT
    o.acte_id||o.find_id AS "IDENTIFIANT",o.acte_id AS "ACTE"
    ,ld.ads_id AS "ACTIVITE",ld.nat_id AS "NATURE_ECONOMIQUE"
    ,fd.find_id AS "SOURCE_FINANCEMENT",fd.bai_id AS "BAILLEUR"
    ,o.montant_ae AS "AE",o.montant_cp AS "CP"
FROM MEATEST.operation_acte o
LEFT JOIN ligne_de_depenses@dblink_elabo_bidf ld ON ld.ldep_id = o.ldep_id
LEFT JOIN financement_depenses@dblink_elabo_bidf fd ON fd.find_id = o.find_id;

CREATE OR REPLACE VIEW VA_ACTIVITE_MONTANTS AS
SELECT a.identifiant AS "IDENTIFIANT",va.identifiant AS "VERSION_ACTE"
    ,SUM(ld.ajustement_ae) AS "MONTANT_AE",SUM(ld.ajustement_cp) AS "MONTANT_CP"
FROM COLLECTIF.VMA_ACTIVITE a
JOIN COLLECTIF.VMA_ACTION action ON action.identifiant = a.action
JOIN COLLECTIF.TA_LIGNE_DEPENSE ld ON ld.activite = a.identifiant
JOIN COLLECTIF.TA_VERSION_ACTE va ON va.acte = ld.version_acte
GROUP BY a.identifiant,va.identifiant
ORDER BY a.identifiant,va.identifiant;

CREATE OR REPLACE VIEW VA_SECTION_MONTANTS AS 
SELECT s.identifiant AS "IDENTIFIANT",va.identifiant AS "VERSION_ACTE"
    ,SUM(NVL(ld.ajustement_ae,0)) AS "MONTANT_AE",SUM(NVL(ld.ajustement_cp,0)) AS "MONTANT_CP"
FROM COLLECTIF.VMA_SECTION s
JOIN COLLECTIF.VMA_ACTIVITE a ON a.section = s.identifiant
JOIN COLLECTIF.TA_LIGNE_DEPENSE ld ON ld.activite = a.identifiant
JOIN COLLECTIF.TA_VERSION_ACTE va ON va.acte = ld.version_acte
GROUP BY s.identifiant,va.identifiant
ORDER BY s.identifiant,va.identifiant;

CREATE OR REPLACE VIEW VA_NATURE_DEPENSE_MONTANTS
AS SELECT nd.identifiant AS "IDENTIFIANT",va.identifiant AS "VERSION_ACTE"
    ,a.section AS "SECTION",a.usb AS "USB",a.action AS "ACTION"
    ,SUM(ld.ajustement_ae) AS "MONTANT_AE",SUM(ld.ajustement_cp) AS "MONTANT_CP"
FROM COLLECTIF.VMA_NATURE_DEPENSE nd
JOIN COLLECTIF.VMA_ACTIVITE a ON a.nature_depense = nd.identifiant
JOIN COLLECTIF.TA_LIGNE_DEPENSE ld ON ld.activite = a.identifiant
JOIN COLLECTIF.TA_VERSION_ACTE va ON va.acte = ld.version_acte
GROUP BY nd.identifiant,va.identifiant,a.section,a.usb,a.action
ORDER BY nd.identifiant,va.identifiant,a.section,a.usb,a.action;

CREATE OR REPLACE VIEW VA_USB_ND_MONTANTS AS
SELECT usb.identifiant AS "IDENTIFIANT",va.identifiant AS "VERSION_ACTE"
    ,a.nature_depense AS "NATURE_DEPENSE"
    ,SUM(ld.ajustement_ae) AS "MONTANT_AE",SUM(ld.ajustement_cp) AS "MONTANT_CP"
FROM COLLECTIF.VMA_USB usb
JOIN COLLECTIF.VMA_ACTIVITE a ON a.usb = usb.identifiant
JOIN COLLECTIF.TA_LIGNE_DEPENSE ld ON ld.activite = a.identifiant
JOIN COLLECTIF.TA_VERSION_ACTE va ON va.acte = ld.version_acte
GROUP BY usb.identifiant,va.identifiant,a.nature_depense
ORDER BY usb.identifiant,va.identifiant,a.nature_depense;

CREATE OR REPLACE VIEW VA_LIGNE_DEPENSE_MVT_TOTAL AS 
SELECT ld.identifiant as "IDENTIFIANT",ld.version_acte AS "VERSION_ACTE",SUM(NVL(o.MONTANT_AE,0)) as "MONTANT_AE",SUM(NVL(o.MONTANT_CP,0)) as "MONTANT_CP" 
FROM COLLECTIF.TA_LIGNE_DEPENSE ld,COLLECTIF.VMA_OPERATION_MOUVEMENT_CREDIT o 
WHERE ld.ACTIVITE=o.ACTIVITE AND ld.NATURE_ECONOMIQUE=o.NATURE_ECONOMIQUE AND ld.SOURCE_FINANCEMENT=o.SOURCE_FINANCEMENT 
AND ld.BAILLEUR=o.BAILLEUR
GROUP BY ld.identifiant,ld.version_acte
ORDER BY ld.identifiant,ld.version_acte ASC;

CREATE OR REPLACE VIEW VA_LIGNE_DEPENSE_MVT_INCLUS AS 
SELECT ld.identifiant as "IDENTIFIANT",ld.version_acte AS "VERSION_ACTE",SUM(NVL(o.MONTANT_AE,0)) as "MONTANT_AE",SUM(NVL(o.MONTANT_CP,0)) as "MONTANT_CP" 
FROM COLLECTIF.TA_LIGNE_DEPENSE ld,COLLECTIF.VMA_OPERATION_MOUVEMENT_CREDIT o,COLLECTIF.TA_MOUVEMENT_CREDIT_VERS_ACTE i 
WHERE ld.ACTIVITE=o.ACTIVITE AND ld.NATURE_ECONOMIQUE=o.NATURE_ECONOMIQUE AND ld.SOURCE_FINANCEMENT=o.SOURCE_FINANCEMENT 
AND ld.BAILLEUR=o.BAILLEUR AND ld.version_acte = i.version_acte AND o.acte = i.mouvement_credit AND i.inclus = 1
GROUP BY ld.identifiant,ld.version_acte
ORDER BY ld.identifiant,ld.version_acte ASC;