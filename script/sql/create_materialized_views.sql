DROP MATERIALIZED VIEW VMA_EXERCICE;
CREATE MATERIALIZED VIEW VMA_EXERCICE REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT DISTINCT TO_CHAR(ld.exo_num) AS "IDENTIFIANT", TO_CHAR(ld.exo_num) AS "CODE",'Exercice budgétaire '||ld.exo_num AS "LIBELLE",ld.exo_num AS "ANNEE"
FROM ligne_de_depenses@dblink_elabo_bidf ld
ORDER BY ld.exo_num DESC;
ALTER TABLE VMA_EXERCICE ADD CONSTRAINT VMA_EXERCICE_PK PRIMARY KEY (IDENTIFIANT);
CREATE INDEX VMA_EXERCICE_K_CODE ON VMA_EXERCICE (CODE ASC);
CREATE INDEX VMA_EXERCICE_K_ANNEE ON VMA_EXERCICE (ANNEE ASC);

DROP MATERIALIZED VIEW VMA_BAILLEUR;
CREATE MATERIALIZED VIEW VMA_BAILLEUR REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT t.bai_id AS "IDENTIFIANT",t.bai_code AS "CODE",t.bai_designation AS "LIBELLE"
FROM bailleur@dblink_elabo_bidf t;

DROP MATERIALIZED VIEW VMA_SOURCE_FINANCEMENT;
CREATE MATERIALIZED VIEW VMA_SOURCE_FINANCEMENT REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT t.sfin_id AS "IDENTIFIANT",t.sfin_code AS "CODE",t.sfin_liblg AS "LIBELLE"
FROM source_financement@dblink_elabo_bidf t;

DROP MATERIALIZED VIEW VMA_NATURE_ECONOMIQUE;
CREATE MATERIALIZED VIEW VMA_NATURE_ECONOMIQUE REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT t.nat_id AS "IDENTIFIANT",t.nat_code AS "CODE",t.nat_liblg AS "LIBELLE"
FROM nature_economique@dblink_elabo_bidf t;

DROP MATERIALIZED VIEW VMA_NATURE_DEPENSE;
CREATE MATERIALIZED VIEW VMA_NATURE_DEPENSE REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT t.ndep_id AS "IDENTIFIANT",t.ndep_code AS "CODE",t.ndep_liblg AS "LIBELLE"
FROM nature_depenses@dblink_elabo_bidf t;

DROP MATERIALIZED VIEW VMA_SECTION;
CREATE MATERIALIZED VIEW VMA_SECTION REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT t.secb_id AS "IDENTIFIANT",t.secb_num AS "CODE",t.secb_liblg AS "LIBELLE"
FROM section_budgetaire@dblink_elabo_bidf t;

DROP MATERIALIZED VIEW VMA_UNITE_ADMINISTRATIVE;
CREATE MATERIALIZED VIEW VMA_UNITE_ADMINISTRATIVE REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT t.ua_id AS "IDENTIFIANT",t.ua_code AS "CODE",t.ua_liblg AS "LIBELLE"
,t.secb_id AS "SECTION_IDENTIFIANT",s.secb_num||' '||s.secb_liblg AS "SECTION_CODE_LIBELLE"
FROM unite_administrative@dblink_elabo_bidf t
LEFT JOIN section_budgetaire@dblink_elabo_bidf s ON s.secb_id = t.secb_id;
ALTER TABLE VMA_UNITE_ADMINISTRATIVE ADD CONSTRAINT VMA_UNITE_ADMINISTRATIVE_PK PRIMARY KEY (IDENTIFIANT);

DROP MATERIALIZED VIEW VMA_TYPE_USB;
CREATE MATERIALIZED VIEW VMA_TYPE_USB REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT t.uuid AS "IDENTIFIANT",t.tusb_code AS "CODE",t.tusb_liblg AS "LIBELLE"
FROM UT_BIDF_TAMP.type_usb@dblink_elabo_bidf t;

DROP MATERIALIZED VIEW VMA_CATEGORIE_USB;
CREATE MATERIALIZED VIEW VMA_CATEGORIE_USB REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT t.uuid AS "IDENTIFIANT",t.cusb_code AS "CODE",t.cusb_lib AS "LIBELLE"
,tu.uuid AS "TYPE_IDENTIFIANT",tu.tusb_code||' '||tu.tusb_liblg AS "TYPE_CODE_LIBELLE"
FROM UT_BIDF_TAMP.categorie_usb@dblink_elabo_bidf t
LEFT JOIN UT_BIDF_TAMP.type_usb@dblink_elabo_bidf tu ON tu.uuid = t.tusb_id;

DROP MATERIALIZED VIEW VMA_USB;
CREATE MATERIALIZED VIEW VMA_USB REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT usb.usb_id AS "IDENTIFIANT",usb.usb_code AS "CODE",usb.usb_liblg AS "LIBELLE"
,c.uuid AS "CATEGORIE_IDENTIFIANT",c.cusb_code||' '||c.cusb_lib AS "CATEGORIE_CODE_LIBELLE"
,tu.uuid AS "TYPE_IDENTIFIANT",tu.tusb_code||' '||tu.tusb_liblg AS "TYPE_CODE_LIBELLE"
,s.secb_id AS "SECTION_IDENTIFIANT",s.secb_num AS "SECTION_CODE",s.secb_liblg AS "SECTION_LIBELLE",s.secb_num||' '||s.secb_liblg AS "SECTION_CODE_LIBELLE"
FROM unite_spec_bud@dblink_elabo_bidf usb
LEFT JOIN section_budgetaire@dblink_elabo_bidf s ON s.secb_id = usb.secb_id
LEFT JOIN UT_BIDF_TAMP.categorie_usb@dblink_elabo_bidf c ON c.uuid = usb.cusb_code
LEFT JOIN UT_BIDF_TAMP.type_usb@dblink_elabo_bidf tu ON tu.uuid = c.tusb_id;

DROP MATERIALIZED VIEW VMA_ACTION;
CREATE MATERIALIZED VIEW VMA_ACTION REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT t.adp_id AS "IDENTIFIANT",t.adp_code AS "CODE",t.adp_liblg AS "LIBELLE"
,s.secb_id AS "SECTION_IDENTIFIANT",s.secb_num AS "SECTION_CODE",s.secb_liblg AS "SECTION_LIBELLE",s.secb_num||' '||s.secb_liblg AS "SECTION_CODE_LIBELLE"
,usb.usb_id AS "USB_IDENTIFIANT",usb.usb_code AS "USB_CODE",usb.usb_liblg AS "USB_LIBELLE",usb.usb_code||' '||usb.usb_liblg AS "USB_CODE_LIBELLE"
FROM action@dblink_elabo_bidf t
LEFT JOIN unite_spec_bud@dblink_elabo_bidf usb ON usb.usb_id = t.usb_id
LEFT JOIN section_budgetaire@dblink_elabo_bidf s ON s.secb_id = usb.secb_id;

DROP MATERIALIZED VIEW VMA_CATEGORIE_BUDGET;
CREATE MATERIALIZED VIEW VMA_CATEGORIE_BUDGET REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT uuid AS "IDENTIFIANT",cbud_code AS "CODE",cbud_liblg AS "LIBELLE"
FROM categorie_budget@dblink_elabo_bidf
ORDER BY cbud_code ASC;
CREATE INDEX VMA_CATEGORIE_BUDGET_K_CODE ON VMA_CATEGORIE_BUDGET (CODE ASC);

DROP MATERIALIZED VIEW VMA_CATEGORIE_ACTIVITE;
CREATE MATERIALIZED VIEW VMA_CATEGORIE_ACTIVITE REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT catv_id AS "IDENTIFIANT",catv_code AS "CODE",catv_libelle AS "LIBELLE"
FROM categorie_activite@dblink_elabo_bidf
ORDER BY catv_code ASC;
CREATE INDEX VMA_CATEGORIE_ACTIVITE_K_CODE ON VMA_CATEGORIE_ACTIVITE (CODE ASC);

DROP MATERIALIZED VIEW VMA_ACTIVITE;
CREATE MATERIALIZED VIEW VMA_ACTIVITE REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT t.ads_id AS "IDENTIFIANT",t.ads_code AS "CODE",t.ads_liblg AS "LIBELLE"
,t.ndep_id AS "NATURE_DEPENSE_IDENTIFIANT",n.ndep_code AS "NATURE_DEPENSE_CODE",n.ndep_liblg AS "NATURE_DEPENSE_LIBELLE",n.ndep_code||' '||n.ndep_liblg AS "NATURE_DEPENSE_CODE_LIBELLE"
,t.catv_id AS "CA_IDENTIFIANT",ca.catv_code AS "CA_CODE",ca.catv_libelle AS "CA_LIBELLE",ca.catv_code||' '||ca.catv_libelle AS "CA_CODE_LIBELLE"
,t.adp_id AS "ACTION_IDENTIFIANT",a.adp_code AS "ACTION_CODE",a.adp_liblg AS "ACTION_LIBELLE",a.adp_code||' '||a.adp_liblg AS "ACTION_CODE_LIBELLE"
,u.usb_id AS "USB_IDENTIFIANT",u.usb_code AS "USB_CODE",u.usb_liblg AS "USB_LIBELLE",u.usb_code||' '||u.usb_liblg AS "USB_CODE_LIBELLE"
,s.secb_id AS "SECTION_IDENTIFIANT",s.secb_num AS "SECTION_CODE",s.secb_liblg AS "SECTION_LIBELLE",s.secb_num||' '||s.secb_liblg AS "SECTION_CODE_LIBELLE"
,ua.ua_id AS "UA_IDENTIFIANT",ua.ua_code AS "UA_CODE",ua.ua_liblg AS "UA_LIBELLE",ua.ua_code||' '||ua.ua_liblg AS "UA_CODE_LIBELLE"
FROM activite_de_service@dblink_elabo_bidf t
LEFT JOIN unite_administrative@dblink_elabo_bidf ua ON ua.ua_id = t.ua_id
LEFT JOIN categorie_activite@dblink_elabo_bidf ca ON ca.catv_id = t.catv_id
LEFT JOIN nature_depenses@dblink_elabo_bidf n ON n.ndep_id = t.ndep_id
LEFT JOIN action@dblink_elabo_bidf a ON a.adp_id = t.adp_id
LEFT JOIN unite_spec_bud@dblink_elabo_bidf u ON u.usb_id = a.usb_id
LEFT JOIN section_budgetaire@dblink_elabo_bidf s ON s.secb_id = u.secb_id;

DROP MATERIALIZED VIEW VMA_ACTIVITE_RESSOURCE;
CREATE MATERIALIZED VIEW VMA_ACTIVITE_RESSOURCE REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT a.ads_code AS "IDENTIFIANT",a.ads_code AS "CODE",a.ads_liblg AS "LIBELLE"
,u.usb_id AS "USB_IDENTIFIANT",u.usb_code AS "USB_CODE",u.usb_liblg AS "USB_LIBELLE",u.usb_code||' '||u.usb_liblg AS "USB_CODE_LIBELLE"
,s.secb_id AS "SECTION_IDENTIFIANT",s.secb_num AS "SECTION_CODE",s.secb_liblg AS "SECTION_LIBELLE",s.secb_num||' '||s.secb_liblg AS "SECTION_CODE_LIBELLE"
FROM activite_de_recette@dblink_elabo_bidf a
LEFT JOIN categorie_activite@dblink_elabo_bidf ca ON ca.catv_code = a.catv_code
LEFT JOIN unite_spec_bud@dblink_elabo_bidf u ON u.usb_code = a.usb_code
LEFT JOIN section_budgetaire@dblink_elabo_bidf s ON s.secb_id = u.secb_id;
ALTER TABLE VMA_ACTIVITE_RESSOURCE ADD CONSTRAINT VMA_ACTIVITE_RESSOURCE_UK_CODE UNIQUE (CODE);
CREATE INDEX VMA_ACTIVITE_RESSOURCE_K_S_ID ON VMA_ACTIVITE_RESSOURCE (SECTION_IDENTIFIANT ASC);
CREATE INDEX VMA_ACTIVITE_RESSOURCE_K_U_ID ON VMA_ACTIVITE_RESSOURCE (USB_IDENTIFIANT ASC);

DROP MATERIALIZED VIEW VMA_DEPENSE;
CREATE MATERIALIZED VIEW VMA_DEPENSE REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT version_collectif.identifiant||d.activite_code||d.nature_economique_code||d.source_financement_code||d.bailleur_code AS "IDENTIFIANT",version_collectif.identifiant AS "VERSION_COLLECTIF",d.*
FROM UT_BIDF_TAMP.va_depense@dblink_elabo_bidf d
JOIN VMA_EXERCICE exercice ON exercice.annee = d.exercice
JOIN TA_COLLECTIF collectif ON collectif.exercice = exercice.identifiant
JOIN TA_VERSION_COLLECTIF version_collectif ON version_collectif.collectif = collectif.identifiant;
ALTER TABLE VMA_DEPENSE ADD CONSTRAINT VMA_DEPENSE_PK PRIMARY KEY (IDENTIFIANT);
CREATE INDEX VMA_DEPENSE_K_EXERCICE ON VMA_DEPENSE (EXERCICE ASC);
CREATE INDEX VMA_DEPENSE_K_V_COLLECTIF ON VMA_DEPENSE (VERSION_COLLECTIF ASC);
CREATE INDEX VMA_DEPENSE_K_ACTIVITE ON VMA_DEPENSE (ACTIVITE_IDENTIFIANT ASC);
CREATE INDEX VMA_DEPENSE_K_ACTIVITE_CODE ON VMA_DEPENSE (ACTIVITE_CODE ASC);
CREATE INDEX VMA_DEPENSE_K_ACTIVITE_COD_LIB ON VMA_DEPENSE (ACTIVITE_CODE_LIBELLE ASC);
CREATE INDEX VMA_DEPENSE_K_NE ON VMA_DEPENSE (NATURE_ECONOMIQUE_IDENTIFIANT ASC);
CREATE INDEX VMA_DEPENSE_K_NE_CODE ON VMA_DEPENSE (NATURE_ECONOMIQUE_CODE ASC);
CREATE INDEX VMA_DEPENSE_K_NE_COD_LIB ON VMA_DEPENSE (NATURE_ECONOMIQUE_CODE_LIBELLE ASC);
CREATE INDEX VMA_DEPENSE_K_SF ON VMA_DEPENSE (SOURCE_FINANCEMENT_IDENTIFIANT ASC);
CREATE INDEX VMA_DEPENSE_K_SF_CODE ON VMA_DEPENSE (SOURCE_FINANCEMENT_CODE ASC);
CREATE INDEX VMA_DEPENSE_K_SF_COD_LIB ON VMA_DEPENSE (SF_CODE_LIBELLE ASC);
CREATE INDEX VMA_DEPENSE_K_BAILLEUR ON VMA_DEPENSE (BAILLEUR_IDENTIFIANT ASC);
CREATE INDEX VMA_DEPENSE_K_BAILLEUR_CODE ON VMA_DEPENSE (BAILLEUR_CODE ASC);
CREATE INDEX VMA_DEPENSE_K_BAILLEUR_COD_LIB ON VMA_DEPENSE (BAILLEUR_CODE_LIBELLE ASC);
CREATE INDEX VMA_DEPENSE_K_ND ON VMA_DEPENSE (NATURE_DEPENSE_IDENTIFIANT ASC);
CREATE INDEX VMA_DEPENSE_K_ND_CODE ON VMA_DEPENSE (NATURE_DEPENSE_CODE ASC);
CREATE INDEX VMA_DEPENSE_K_ND_COD_LIB ON VMA_DEPENSE (NATURE_DEPENSE_CODE_LIBELLE ASC);
CREATE INDEX VMA_DEPENSE_K_ACTION ON VMA_DEPENSE (ACTION_IDENTIFIANT ASC);
CREATE INDEX VMA_DEPENSE_K_ACTION_CODE ON VMA_DEPENSE (ACTION_CODE ASC);
CREATE INDEX VMA_DEPENSE_K_ACTION_COD_LIB ON VMA_DEPENSE (ACTION_CODE_LIBELLE ASC);
CREATE INDEX VMA_DEPENSE_K_USB ON VMA_DEPENSE (USB_IDENTIFIANT ASC);
CREATE INDEX VMA_DEPENSE_K_USB_CODE ON VMA_DEPENSE (USB_CODE ASC);
CREATE INDEX VMA_DEPENSE_K_USB_COD_LIB ON VMA_DEPENSE (USB_CODE_LIBELLE ASC);
CREATE INDEX VMA_DEPENSE_K_SECTION ON VMA_DEPENSE (SECTION_IDENTIFIANT ASC);
CREATE INDEX VMA_DEPENSE_K_SECTION_CODE ON VMA_DEPENSE (SECTION_CODE ASC);
CREATE INDEX VMA_DEPENSE_K_SECTION_COD_LIB ON VMA_DEPENSE (SECTION_CODE_LIBELLE ASC);

DROP MATERIALIZED VIEW VMA_RESSOURCE;
CREATE MATERIALIZED VIEW VMA_RESSOURCE REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT version_collectif.identifiant||r.activite_code||r.nature_economique_code AS "IDENTIFIANT",version_collectif.identifiant AS "VERSION_COLLECTIF",r.*
FROM UT_BIDF_TAMP.va_ressource@dblink_elabo_bidf r
JOIN VMA_EXERCICE exercice ON exercice.annee = r.exercice
JOIN TA_COLLECTIF collectif ON collectif.exercice = exercice.identifiant
JOIN TA_VERSION_COLLECTIF version_collectif ON version_collectif.collectif = collectif.identifiant;
ALTER TABLE VMA_RESSOURCE ADD CONSTRAINT VMA_RESSOURCE_PK PRIMARY KEY (IDENTIFIANT);
CREATE INDEX VMA_RESSOURCE_K_EXERCICE ON VMA_RESSOURCE (EXERCICE ASC);
CREATE INDEX VMA_RESSOURCE_K_ACTIVITE ON VMA_RESSOURCE (ACTIVITE_IDENTIFIANT ASC);
CREATE INDEX VMA_RESSOURCE_K_NE ON VMA_RESSOURCE (NATURE_ECONOMIQUE_IDENTIFIANT ASC);
CREATE INDEX VMA_RESSOURCE_K_USB ON VMA_RESSOURCE (USB_IDENTIFIANT ASC);
CREATE INDEX VMA_RESSOURCE_K_SECTION ON VMA_RESSOURCE (SECTION_IDENTIFIANT ASC);

DROP MATERIALIZED VIEW VA_DEPENSE_DISPONIBLE;
CREATE MATERIALIZED VIEW VA_DEPENSE_DISPONIBLE REFRESH NEXT SYSDATE + 1/24/10/2 COMPLETE AS
SELECT d.exercice||d.activite||d.nature_economique||d.source_financement||d.bailleur AS "IDENTIFIANT",d.*
FROM UT_BIDF_TAMP.va_depense_disponible@dblink_elabo_bidf d;