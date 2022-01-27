-- Section
DROP MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_SECTION;
CREATE MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_SECTION
REFRESH ON COMMIT COMPLETE AS
SELECT s.uuid AS "IDENTIFIANT",s.secb_code AS "CODE",s.secb_libelle AS "LIBELLE"
FROM SIIBC_CA.section_budgetaire s,SIIBC_CA.gouvernement g
WHERE s.gouv_id = g.uuid AND g.gouv_statut = 'ENABLED' AND g.gouv_utilise = 1 AND s.entitystatus = 'COMMITTED';
ALTER TABLE VMA_SECTION ADD CONSTRAINT VMA_SECTION_PK PRIMARY KEY (IDENTIFIANT);
ALTER TABLE VMA_SECTION ADD CONSTRAINT VMA_SECTION_UK_CODE UNIQUE (CODE);

-- Unité administrative
DROP MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_UNITE_ADMINISTRATIVE;
CREATE MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_UNITE_ADMINISTRATIVE
REFRESH ON COMMIT COMPLETE AS SELECT ua.uuid AS "IDENTIFIANT",ua.ua_code AS "CODE",ua.ua_liblg AS "LIBELLE"
    ,CASE WHEN s.uuid IS NULL THEN NULL ELSE s.uuid END AS "SECTION"
    ,CASE WHEN s.uuid IS NULL THEN NULL ELSE s.uuid END AS "SECTION_IDENTIFIANT"
    ,CASE WHEN s.uuid IS NULL THEN NULL ELSE s.secb_code END AS "SECTION_CODE"
    ,CASE WHEN s.uuid IS NULL THEN NULL ELSE s.secb_code||' '||s.secb_libelle END AS "SECTION_CODE_LIBELLE"
FROM SIIBC_CA.unite_administrative ua,SIIBC_CA.section_budgetaire s
WHERE s.uuid (+) = ua.ua_secb_id AND ua.entitystatus = 'COMMITTED' AND s.entitystatus = 'COMMITTED';
ALTER TABLE VMA_UNITE_ADMINISTRATIVE ADD CONSTRAINT VMA_UA_PK PRIMARY KEY (IDENTIFIANT);
ALTER TABLE VMA_UNITE_ADMINISTRATIVE ADD CONSTRAINT VMA_UA_UK_CODE UNIQUE (CODE);
CREATE INDEX VMA_UA_K_SECTION ON VMA_UNITE_ADMINISTRATIVE (SECTION ASC);

-- Type unité de spécialisation du budget
DROP MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_TYPE_USB;
CREATE MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_TYPE_USB
REFRESH ON COMMIT COMPLETE AS SELECT type_usb.uuid AS "IDENTIFIANT",type_usb.tusb_code AS "CODE",type_usb.tusb_liblg AS "LIBELLE"
FROM SIIBC_CPP.type_usb type_usb
WHERE type_usb.entitystatus = 'COMMITTED';
--ALTER TABLE VMA_TYPE_USB ADD CONSTRAINT VMA_TYPE_USB_PK PRIMARY KEY (IDENTIFIANT);
--ALTER TABLE VMA_TYPE_USB ADD CONSTRAINT VMA_TYPE_USB_UK_CODE UNIQUE (CODE);

-- Cat�gorie unit� de sp�cialisation du budget
DROP MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_CATEGORIE_USB;
CREATE MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_CATEGORIE_USB
REFRESH ON COMMIT COMPLETE AS SELECT categorie_usb.uuid AS "IDENTIFIANT",categorie_usb.cusb_code AS "CODE"
,categorie_usb.cusb_lib AS "LIBELLE",categorie_usb.tusb_id AS "TYPE"
FROM SIIBC_CPP.categorie_usb categorie_usb
WHERE categorie_usb.entitystatus = 'COMMITTED';

-- Catégorie budget
DROP MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_CATEGORIE_BUDGET;
CREATE MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_CATEGORIE_BUDGET
REFRESH ON COMMIT COMPLETE AS
SELECT categorie_budget.uuid AS "IDENTIFIANT",categorie_budget.cbud_code AS "CODE",categorie_budget.cbud_liblg AS "LIBELLE"
FROM SIIBC_CPP.categorie_budget categorie_budget
WHERE categorie_budget.entitystatus = 'COMMITTED';

-- Unité de spécialisation du budget
DROP MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_USB;
CREATE MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_USB
REFRESH ON COMMIT COMPLETE AS SELECT usb.uuid AS "IDENTIFIANT",usb.usb_code AS "CODE",usb.usb_liblg AS "LIBELLE",usb.usb_cusb_id AS "CATEGORIE"
,s.uuid AS "SECTION",s.uuid AS "SECTION_IDENTIFIANT",s.secb_code AS "SECTION_CODE",s.secb_code||' '||s.secb_libelle AS "SECTION_CODE_LIBELLE"
FROM SIIBC_CPP.usb usb,SIIBC_CA.section_budgetaire s
WHERE s.uuid = usb.usb_secb_id AND usb.entitystatus = 'COMMITTED' AND s.entitystatus = 'COMMITTED';
ALTER TABLE VMA_USB ADD CONSTRAINT VMA_USB_PK PRIMARY KEY (IDENTIFIANT);
ALTER TABLE VMA_USB ADD CONSTRAINT VMA_USB_UK_CODE UNIQUE (CODE);
CREATE INDEX VMA_USB_K_SECTION ON VMA_USB (SECTION ASC);

-- Action
DROP MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_ACTION;
CREATE MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_ACTION
REFRESH ON COMMIT COMPLETE AS SELECT a.uuid AS "IDENTIFIANT",a.adp_code AS "CODE",a.adp_liblg AS "LIBELLE"
,s.uuid AS "SECTION",s.uuid AS "SECTION_IDENTIFIANT",s.secb_code AS "SECTION_CODE",s.secb_code||' '||s.secb_libelle AS "SECTION_CODE_LIBELLE"
,usb.uuid AS "USB",usb.uuid AS "USB_IDENTIFIANT",usb.usb_code AS "USB_CODE",usb.usb_code||' '||usb.usb_liblg AS "USB_CODE_LIBELLE"
FROM SIIBC_CPP.action a,SIIBC_CPP.usb usb,SIIBC_CA.section_budgetaire s
WHERE a.adp_usb_id = usb.uuid AND s.uuid = usb.usb_secb_id AND usb.entitystatus = 'COMMITTED' AND a.entitystatus = 'COMMITTED';
ALTER TABLE VMA_ACTION ADD CONSTRAINT VMA_ACTION_PK PRIMARY KEY (IDENTIFIANT);
ALTER TABLE VMA_ACTION ADD CONSTRAINT VMA_ACTION_UK_CODE UNIQUE (CODE);
CREATE INDEX VMA_ACTION_K_SECTION ON VMA_ACTION (SECTION ASC);
CREATE INDEX VMA_ACTION_K_USB ON VMA_ACTION (USB ASC);

-- Catégorie Activité
DROP MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_CATEGORIE_ACTIVITE;
CREATE MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_CATEGORIE_ACTIVITE
REFRESH ON COMMIT COMPLETE AS SELECT ca.catv_id AS "IDENTIFIANT",ca.catv_code AS "CODE",ca.catv_liblg AS "LIBELLE"
FROM SIIBC_ADS.categorie_activite ca;
--ALTER TABLE VMA_CATEGORIE_ACTIVITE ADD CONSTRAINT VMA_CATEGORIE_ACTIVITE_PK PRIMARY KEY (IDENTIFIANT);
ALTER TABLE VMA_CATEGORIE_ACTIVITE ADD CONSTRAINT VMA_CATEGORIE_ACTIVITE_UK_CODE UNIQUE (CODE);

-- Activité
DROP MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_ACTIVITE;
CREATE MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_ACTIVITE
REFRESH ON COMMIT COMPLETE AS SELECT a.ads_id AS "IDENTIFIANT",a.ads_code AS "CODE",a.ads_liblg AS "LIBELLE"
,c.catv_id AS "CATEGORIE",c.catv_id AS "CATEGORIE_IDENTIFIANT",c.catv_code AS "CATEGORIE_CODE",c.catv_code||' '||c.catv_liblg AS "CATEGORIE_CODE_LIBELLE"
,action.uuid AS "ACTION",action.uuid AS "ACTION_IDENTIFIANT",action.adp_code AS "ACTION_CODE",action.adp_code||' '||action.adp_liblg AS "ACTION_CODE_LIBELLE"
,usb.uuid AS "USB",usb.uuid AS "USB_IDENTIFIANT",usb.usb_code AS "USB_CODE",usb.usb_code||' '||usb.usb_liblg AS "USB_CODE_LIBELLE"
,section.uuid AS "SECTION",section.uuid AS "SECTION_IDENTIFIANT",section.secb_code AS "SECTION_CODE",section.secb_code||' '||section.secb_libelle AS "SECTION_CODE_LIBELLE"
,nd.uuid AS "NATURE_DEPENSE",nd.uuid AS "NATURE_DEPENSE_IDENTIFIANT",nd.ndep_code AS "NATURE_DEPENSE_CODE",nd.ndep_code||' '||nd.ndep_libct AS "NATURE_DEPENSE_CODE_LIBELLE"
--,CASE WHEN ua.uuid IS NULL THEN NULL ELSE ua.uuid END AS "UA"
--,CASE WHEN ua.ua_code IS NULL THEN NULL ELSE ua.ua_code||' '||ua.ua_liblg END AS "UA_CODE_LIBELLE"
,CAST(NULL AS VARCHAR2(255)) AS "UA",CAST(NULL AS VARCHAR2(1024)) AS "UA_CODE",CAST(NULL AS VARCHAR2(1024)) AS "UA_CODE_LIBELLE"
FROM SIIBC_ADS.activite_de_service a,SIIBC_ADS.categorie_activite c,SIIBC_CPP.action action,SIIBC_CPP.usb usb
,SIIBC_CA.section_budgetaire section,SIIBC_NEC.nature_depense nd,SIIBC_CA.unite_administrative ua
WHERE a.catv_id = c.catv_id (+) AND a.adp_id = action.uuid (+) AND action.adp_usb_id = usb.uuid (+) 
AND usb.usb_secb_id = section.uuid (+) AND nd.ndep_code = a.ndep_id (+) AND ua.uuid (+) = a.ua_benef_id
AND section.entitystatus = 'COMMITTED' AND ua.entitystatus = 'COMMITTED' 
AND usb.entitystatus = 'COMMITTED' AND action.entitystatus = 'COMMITTED';
ALTER TABLE VMA_ACTIVITE ADD CONSTRAINT VMA_ACTIVITE_PK PRIMARY KEY (IDENTIFIANT);
ALTER TABLE VMA_ACTIVITE ADD CONSTRAINT VMA_ACTIVITE_UK_CODE UNIQUE (CODE);
CREATE INDEX VMA_ACTIVITE_K_SECTION ON VMA_ACTIVITE (SECTION ASC);
CREATE INDEX VMA_ACTIVITE_K_USB ON VMA_ACTIVITE (USB ASC);
CREATE INDEX VMA_ACTIVITE_K_ACTION ON VMA_ACTIVITE (ACTION ASC);
CREATE INDEX VMA_ACTIVITE_K_ND ON VMA_ACTIVITE (NATURE_DEPENSE ASC);
CREATE INDEX VMA_ACTIVITE_K_UA ON VMA_ACTIVITE (UA ASC);

-- Activité de recette
DROP MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_ACTIVITE_RECETTE;
CREATE MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_ACTIVITE_RECETTE
REFRESH ON COMMIT COMPLETE AS
SELECT a.ads_code AS "IDENTIFIANT",a.ads_code AS "CODE",a.ads_liblg AS "LIBELLE"
,usb.uuid AS "USB",usb.uuid AS "USB_IDENTIFIANT",usb.usb_code AS "USB_CODE",usb.usb_code||' '||usb.usb_liblg AS "USB_CODE_LIBELLE"
,section.uuid AS "SECTION",section.uuid AS "SECTION_IDENTIFIANT",section.secb_code AS "SECTION_CODE",section.secb_code||' '||section.secb_libelle AS "SECTION_CODE_LIBELLE"
FROM SIIBC_ADS.activite_de_recette a,SIIBC_CPP.usb usb,SIIBC_CA.section_budgetaire section
WHERE a.usb_code = usb.usb_code (+) AND usb.usb_secb_id = section.uuid (+) AND section.entitystatus = 'COMMITTED' AND usb.entitystatus = 'COMMITTED';
ALTER TABLE VMA_ACTIVITE_RECETTE ADD CONSTRAINT VMA_ACTIVITE_RECETTE_PK PRIMARY KEY (IDENTIFIANT);
ALTER TABLE VMA_ACTIVITE_RECETTE ADD CONSTRAINT VMA_ACTIVITE_RECETTE_UK_CODE UNIQUE (CODE);
CREATE INDEX VMA_ACTIVITE_RECETTE_K_SECTION ON VMA_ACTIVITE_RECETTE (SECTION ASC);
CREATE INDEX VMA_ACTIVITE_RECETTE_K_USB ON VMA_ACTIVITE (USB ASC);

-- Nature de dépense
DROP MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_NATURE_DEPENSE;
CREATE MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_NATURE_DEPENSE
REFRESH ON COMMIT COMPLETE AS SELECT t.uuid AS "IDENTIFIANT",t.ndep_code AS "CODE",t.ndep_libct AS "LIBELLE"
FROM SIIBC_NEC.nature_depense t;
--ALTER TABLE VMA_NATURE_DEPENSE ADD CONSTRAINT VMA_NATURE_DEPENSE_PK PRIMARY KEY (IDENTIFIANT);
ALTER TABLE VMA_NATURE_DEPENSE ADD CONSTRAINT VMA_NATURE_DEPENSE_UK_CODE UNIQUE (CODE);

-- Nature économique
DROP MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_NATURE_ECONOMIQUE;
CREATE MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_NATURE_ECONOMIQUE
REFRESH ON COMMIT COMPLETE AS SELECT n.uuid AS "IDENTIFIANT",n.nat_code AS "CODE",n.nat_liblg AS "LIBELLE"
FROM SIIBC_NEC.nature_economique n,SIIBC_NEC.TABLE_REFERENTIEL tref,SIIBC_NEC.VERSION_REFERENTIEL vref
WHERE n.nat_tref = tref.uuid (+) AND tref.tref_vers_id = vref.uuid AND vref.VERS_CODE='312' AND n.nat_imputable=1 AND n.nat_nat is null;
--ALTER TABLE VMA_NATURE_DEPENSE ADD CONSTRAINT VMA_NATURE_DEPENSE_PK PRIMARY KEY (IDENTIFIANT);
ALTER TABLE VMA_NATURE_ECONOMIQUE ADD CONSTRAINT VMA_NATURE_ECO_UK_CODE UNIQUE (CODE);

-- Bailleur
DROP MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_BAILLEUR;
CREATE MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_BAILLEUR
REFRESH ON COMMIT COMPLETE AS SELECT b.uuid AS "IDENTIFIANT",b.bai_code AS "CODE",b.bai_lib AS "LIBELLE"
FROM SIIBC_FINEXT.bailleur b;

-- Source financement
DROP MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_SOURCE_FINANCEMENT;
CREATE MATERIALIZED VIEW "SIIBC_COLLECTIF".VMA_SOURCE_FINANCEMENT
REFRESH ON COMMIT COMPLETE AS SELECT sf.uuid AS "IDENTIFIANT",sf.src_code AS "CODE",sf.src_libelle AS "LIBELLE"
FROM SIIBC_BUDGET.source_financement sf;

-- Actes de gestion
DROP MATERIALIZED VIEW VMA_ACTE_GESTION;
CREATE MATERIALIZED VIEW VMA_ACTE_GESTION REFRESH NEXT SYSDATE + 1/24/4 COMPLETE AS
SELECT 
    ab.acte_id AS "IDENTIFIANT"
    ,TO_CHAR(ab.acte_numero) AS "CODE"
    ,TO_CHAR(ab.acte_numero)||' '||ab.acte_ref_externe_acte as LIBELLE
    ,TO_NUMBER(ab.exo_num) AS "EXERCICE"
    ,SUM(fd.find_montant_ae) as "MONTANT_AE"
    ,SUM(fd.find_montant_cp) as "MONTANT_CP"
FROM 
    SIIBC_MEA.acte_budgetaire ab
JOIN SIIBC_MEA.financement_depenses fd ON fd.acte_id = ab.acte_id
JOIN SIIBC_MEA.categorie_acte_budgetaire cab ON cab.cab_id = ab.acte_cab_id AND cab.cab_tact_code IN ('REGLEMENTAIRE')
GROUP BY ab.acte_id,ab.acte_numero,ab.acte_ref_externe_acte,ab.exo_num
ORDER BY TO_CHAR(ab.acte_numero) ASC;
ALTER TABLE VMA_ACTE_GESTION ADD CONSTRAINT VMA_ACTE_GESTION_PK PRIMARY KEY (IDENTIFIANT);
ALTER TABLE VMA_ACTE_GESTION ADD CONSTRAINT VMA_ACTE_GESTION_UK_CODE UNIQUE (CODE);

-- Lignes de dépenses des actes de gestion
DROP MATERIALIZED VIEW VMA_ACTE_GESTION_DEPENSE;
CREATE MATERIALIZED VIEW VMA_ACTE_GESTION_DEPENSE REFRESH NEXT SYSDATE + 1/24/4 COMPLETE AS
SELECT
    o.acte_id||o.ldep_id||o.find_id AS "IDENTIFIANT"
    ,o.acte_id AS "ACTE"
    ,ag.exercice AS "EXERCICE"
    ,ld.ads_id AS "ACTIVITE"
    ,ld.nat_id AS "NATURE_ECONOMIQUE"
    ,fd.sfin_id AS "SOURCE_FINANCEMENT"
    ,fd.bai_id AS "BAILLEUR"
    ,o.montant_ae AS "MONTANT_AE"
    ,o.montant_cp AS "MONTANT_CP"
FROM SIIBC_MEA.operation_acte o
JOIN VMA_ACTE_GESTION ag ON ag.identifiant = o.acte_id
LEFT JOIN SIIBC_MEA.ligne_de_depenses ld ON ld.ldep_id = o.ldep_id AND ld.acte_id = o.acte_id
LEFT JOIN SIIBC_MEA.financement_depenses fd ON fd.find_id = o.find_id AND fd.acte_id = o.acte_id
ORDER BY ag.exercice DESC,ld.ads_id ASC,ld.nat_id ASC,fd.sfin_id ASC,fd.bai_id ASC;
ALTER TABLE VMA_ACTE_GESTION_DEPENSE ADD CONSTRAINT VMA_ACTE_GESTION_LD_PK PRIMARY KEY (IDENTIFIANT);
CREATE INDEX VMA_ACTE_GESTION_D_K_ACTE ON VMA_ACTE_GESTION_DEPENSE (ACTE ASC);
CREATE INDEX VMA_ACTE_GESTION_D_K_EXERCICE ON VMA_ACTE_GESTION_DEPENSE (EXERCICE ASC);
CREATE INDEX VMA_ACTE_GESTION_D_K_ACTIVITE ON VMA_ACTE_GESTION_DEPENSE (ACTIVITE ASC);
CREATE INDEX VMA_ACTE_GESTION_D_K_NE ON VMA_ACTE_GESTION_DEPENSE (NATURE_ECONOMIQUE ASC);
CREATE INDEX VMA_ACTE_GESTION_D_K_SF ON VMA_ACTE_GESTION_DEPENSE (SOURCE_FINANCEMENT ASC);
CREATE INDEX VMA_ACTE_GESTION_D_K_BAILLEUR ON VMA_ACTE_GESTION_DEPENSE (BAILLEUR ASC);

-- Lignes de recettes des actes de gestion
DROP MATERIALIZED VIEW VMA_ACTE_GESTION_RECETTE;
CREATE MATERIALIZED VIEW VMA_ACTE_GESTION_RECETTE REFRESH NEXT SYSDATE + 1/24/4 COMPLETE AS
SELECT
    TRIM('IDENTIFIANT2') AS "IDENTIFIANT"
    ,TRIM('ACTE2') AS "ACTE"
    ,2021 AS "EXERCICE"
    ,TRIM('ACTIVITE1') AS "ACTIVITE"
    ,TRIM('NATURE_ECONOMIQUE1') AS "NATURE_ECONOMIQUE"
    ,0 AS "MONTANT"
FROM DUAL o;
ALTER TABLE VMA_ACTE_GESTION_RECETTE ADD CONSTRAINT VMA_ACTE_GESTION_R_PK PRIMARY KEY (IDENTIFIANT);
CREATE INDEX VMA_ACTE_GESTION_R_K_ACTE ON VMA_ACTE_GESTION_RECETTE (ACTE ASC);
CREATE INDEX VMA_ACTE_GESTION_R_K_EXERCICE ON VMA_ACTE_GESTION_RECETTE (EXERCICE ASC);
CREATE INDEX VMA_ACTE_GESTION_R_K_ACTIVITE ON VMA_ACTE_GESTION_RECETTE (ACTIVITE ASC);
CREATE INDEX VMA_ACTE_GESTION_R_K_NE ON VMA_ACTE_GESTION_RECETTE (NATURE_ECONOMIQUE ASC);