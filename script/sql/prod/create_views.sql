CREATE OR REPLACE VIEW VA_ACTION_MONTANTS AS
SELECT action.identifiant AS "IDENTIFIANT",va.identifiant AS "VERSION_ACTE"
    ,action.section AS "SECTION",action.usb AS "USB"
    ,SUM(ld.ajustement_ae) AS "MONTANT_AE",SUM(ld.ajustement_cp) AS "MONTANT_CP"
FROM COLLECTIF.VMA_ACTION action
JOIN COLLECTIF.VMA_ACTIVITE a ON a.action = action.identifiant
JOIN COLLECTIF.TA_LIGNE_DEPENSE ld ON ld.activite = a.identifiant
JOIN COLLECTIF.TA_VERSION_ACTE va ON va.acte = ld.version_acte
GROUP BY action.identifiant,va.identifiant,action.section,action.usb
ORDER BY action.identifiant,va.identifiant,action.section,action.usb;

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

CREATE OR REPLACE VIEW VA_LIGNE_DEPENSE_BIDF AS 
SELECT ld.identifiant AS "IDENTIFIANT"--,2021 AS "ANNEE_EXERCICE"
,0 AS "BUDGET_INITIAL_AE",0 AS "BUDGET_INITIAL_CP"
,0 AS "MOUVEMENT_AE",0 AS "MOUVEMENT_CP"
,0 AS "BUDGET_ACTUEL_AE",0 AS "BUDGET_ACTUEL_CP"
,0 AS "DISPONIBLE_AE",0 AS "DISPONIBLE_CP"
FROM COLLECTIF.TA_LIGNE_DEPENSE ld;

CREATE OR REPLACE VIEW VA_LIGNE_DEPENSE AS 
SELECT ld.identifiant AS "IDENTIFIANT"
    ,NVL(ldbidf.budget_initial_ae,0) AS "BUDGET_INITIAL_AE",NVL(ldbidf.budget_initial_cp,0) AS "BUDGET_INITIAL_CP"
    ,NVL(ldbidf.mouvement_ae,0) AS "MOUVEMENT_AE",NVL(ldbidf.mouvement_cp,0) AS "MOUVEMENT_CP"
    ,NVL(ldbidf.budget_actuel_ae,0) AS "BUDGET_ACTUEL_AE",NVL(ldbidf.budget_actuel_cp,0) AS "BUDGET_ACTUEL_CP"
    ,NVL(ldmi.montant_ae,0) AS "MOUVEMENT_INCLUS_AE",NVL(ldmi.montant_cp,0) AS "MOUVEMENT_INCLUS_CP"
    
    ,NVL(ldbidf.budget_actuel_ae,0)-NVL(ldmi.montant_ae,0) AS "BUDGET_ACTUEL_CALCULE_AE"
    ,NVL(ldbidf.budget_actuel_cp,0)-NVL(ldmi.montant_cp,0) AS "BUDGET_ACTUEL_CALCULE_CP"
    
    ,NVL(ldbidf.disponible_ae,0) AS "DISPONIBLE_AE",NVL(ldbidf.disponible_cp,0) AS "DISPONIBLE_CP"
    ,NVL(ld.ajustement_ae,0) AS "AJUSTEMENT_AE",NVL(ld.ajustement_cp,0) AS "AJUSTEMENT_CP"    
    ,NVL(ldbidf.budget_actuel_ae,0)-NVL(ldmi.montant_ae,0)+NVL(ld.ajustement_ae,0) AS "COLLECTIF_AE"
    ,NVL(ldbidf.budget_actuel_cp,0)-NVL(ldmi.montant_cp,0)+NVL(ld.ajustement_cp,0) AS "COLLECTIF_CP"
FROM COLLECTIF.TA_LIGNE_DEPENSE ld
LEFT JOIN COLLECTIF.VA_LIGNE_DEPENSE_BIDF ldbidf ON ldbidf.identifiant = ld.identifiant
LEFT JOIN COLLECTIF.VA_LIGNE_DEPENSE_MVT_INCLUS ldmi ON ldmi.identifiant = ld.identifiant;

/*
CREATE OR REPLACE VIEW VA_LIGNE_DEPENSE AS 
SELECT 
    ld.identifiant AS "IDENTIFIANT",ld.version_acte AS "VERSION_ACTE"
    ,0 AS BUDGET_INITIAL_AE,0 AS BUDGET_INITIAL_CP
    ,0 AS BUDGET_ACTUEL_AE,0 AS BUDGET_ACTUEL_CP
    ,0 AS MONTANT_DISPONIBLE_AE,0 AS MONTANT_DISPONIBLE_CP
    ,NVL(mt.montant_ae,0) AS MOUVEMENT_TOTAL_AE,NVL(mt.montant_cp,0) AS MOUVEMENT_TOTAL_CP
    ,NVL(mi.montant_ae,0) AS MOUVEMENT_INCLUS_AE,NVL(mi.montant_cp,0) AS MOUVEMENT_INCLUS_CP
FROM
    TA_LIGNE_DEPENSE ld
LEFT JOIN COLLECTIF.VA_LIGNE_DEPENSE_MVT_TOTAL mt ON mt.identifiant = ld.identifiant
LEFT JOIN COLLECTIF.VA_LIGNE_DEPENSE_MVT_INCLUS mi ON mi.identifiant = ld.identifiant;
*/