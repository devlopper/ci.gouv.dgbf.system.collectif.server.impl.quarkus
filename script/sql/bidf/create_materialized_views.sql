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
ALTER TABLE VMA_BAILLEUR ADD CONSTRAINT VMA_BAILLEUR_PK PRIMARY KEY (IDENTIFIANT);

DROP MATERIALIZED VIEW VMA_SOURCE_FINANCEMENT;
CREATE MATERIALIZED VIEW VMA_SOURCE_FINANCEMENT REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT t.sfin_id AS "IDENTIFIANT",t.sfin_code AS "CODE",t.sfin_liblg AS "LIBELLE"
FROM source_financement@dblink_elabo_bidf t;
ALTER TABLE VMA_SOURCE_FINANCEMENT ADD CONSTRAINT VMA_SOURCE_FINANCEMENT_PK PRIMARY KEY (IDENTIFIANT);

DROP MATERIALIZED VIEW VMA_NATURE_ECONOMIQUE;
CREATE MATERIALIZED VIEW VMA_NATURE_ECONOMIQUE REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT t.nat_id AS "IDENTIFIANT",t.nat_code AS "CODE",t.nat_liblg AS "LIBELLE"
FROM nature_economique@dblink_elabo_bidf t;
ALTER TABLE VMA_NATURE_ECONOMIQUE ADD CONSTRAINT VMA_NATURE_ECONOMIQUE_PK PRIMARY KEY (IDENTIFIANT);

DROP MATERIALIZED VIEW VMA_NATURE_DEPENSE;
CREATE MATERIALIZED VIEW VMA_NATURE_DEPENSE REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT t.ndep_id AS "IDENTIFIANT",t.ndep_code AS "CODE",t.ndep_liblg AS "LIBELLE"
FROM nature_depenses@dblink_elabo_bidf t;
ALTER TABLE VMA_NATURE_DEPENSE ADD CONSTRAINT VMA_NATURE_DEPENSE_PK PRIMARY KEY (IDENTIFIANT);

DROP MATERIALIZED VIEW VMA_SECTION;
CREATE MATERIALIZED VIEW VMA_SECTION REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT t.secb_id AS "IDENTIFIANT",t.secb_num AS "CODE",t.secb_liblg AS "LIBELLE"
FROM section_budgetaire@dblink_elabo_bidf t;
ALTER TABLE VMA_SECTION ADD CONSTRAINT VMA_SECTION_PK PRIMARY KEY (IDENTIFIANT);

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
FROM type_usb@dblink_elabo_bidf t;
ALTER TABLE VMA_TYPE_USB ADD CONSTRAINT VMA_TYPE_USB_PK PRIMARY KEY (IDENTIFIANT);

DROP MATERIALIZED VIEW VMA_CATEGORIE_USB;
CREATE MATERIALIZED VIEW VMA_CATEGORIE_USB REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT t.uuid AS "IDENTIFIANT",t.cusb_code AS "CODE",t.cusb_lib AS "LIBELLE"
,tu.uuid AS "TYPE_IDENTIFIANT",tu.tusb_code||' '||tu.tusb_liblg AS "TYPE_CODE_LIBELLE"
FROM categorie_usb@dblink_elabo_bidf t
LEFT JOIN type_usb@dblink_elabo_bidf tu ON tu.uuid = t.tusb_id;
ALTER TABLE VMA_CATEGORIE_USB ADD CONSTRAINT VMA_CATEGORIE_USB_PK PRIMARY KEY (IDENTIFIANT);

DROP MATERIALIZED VIEW VMA_USB;
CREATE MATERIALIZED VIEW VMA_USB REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT usb.usb_id AS "IDENTIFIANT",usb.usb_code AS "CODE",usb.usb_liblg AS "LIBELLE"
,c.uuid AS "CATEGORIE_IDENTIFIANT",c.cusb_code||' '||c.cusb_lib AS "CATEGORIE_CODE_LIBELLE"
,tu.uuid AS "TYPE_IDENTIFIANT",tu.tusb_code||' '||tu.tusb_liblg AS "TYPE_CODE_LIBELLE"
,s.secb_id AS "SECTION_IDENTIFIANT",s.secb_num AS "SECTION_CODE",s.secb_liblg AS "SECTION_LIBELLE",s.secb_num||' '||s.secb_liblg AS "SECTION_CODE_LIBELLE"
FROM unite_spec_bud@dblink_elabo_bidf usb
LEFT JOIN section_budgetaire@dblink_elabo_bidf s ON s.secb_id = usb.secb_id
LEFT JOIN categorie_usb@dblink_elabo_bidf c ON c.uuid = usb.cusb_code
LEFT JOIN type_usb@dblink_elabo_bidf tu ON tu.uuid = c.tusb_id;
ALTER TABLE VMA_USB ADD CONSTRAINT VMA_USB_PK PRIMARY KEY (IDENTIFIANT);

DROP MATERIALIZED VIEW VMA_ACTION;
CREATE MATERIALIZED VIEW VMA_ACTION REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT t.adp_id AS "IDENTIFIANT",t.adp_code AS "CODE",t.adp_liblg AS "LIBELLE"
,s.secb_id AS "SECTION_IDENTIFIANT",s.secb_num AS "SECTION_CODE",s.secb_liblg AS "SECTION_LIBELLE",s.secb_num||' '||s.secb_liblg AS "SECTION_CODE_LIBELLE"
,usb.usb_id AS "USB_IDENTIFIANT",usb.usb_code AS "USB_CODE",usb.usb_liblg AS "USB_LIBELLE",usb.usb_code||' '||usb.usb_liblg AS "USB_CODE_LIBELLE"
FROM action@dblink_elabo_bidf t
LEFT JOIN unite_spec_bud@dblink_elabo_bidf usb ON usb.usb_id = t.usb_id
LEFT JOIN section_budgetaire@dblink_elabo_bidf s ON s.secb_id = usb.secb_id;
ALTER TABLE VMA_ACTION ADD CONSTRAINT VMA_ACTION_PK PRIMARY KEY (IDENTIFIANT);

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
ALTER TABLE VMA_ACTIVITE ADD CONSTRAINT VMA_ACTIVITE_PK PRIMARY KEY (IDENTIFIANT);

DROP MATERIALIZED VIEW VMA_ACTIVITE_RECETTE;
CREATE MATERIALIZED VIEW VMA_ACTIVITE_RECETTE REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT a.ads_code AS "IDENTIFIANT",a.ads_code AS "CODE",a.ads_liblg AS "LIBELLE"
,u.usb_id AS "USB_IDENTIFIANT",u.usb_code AS "USB_CODE",u.usb_liblg AS "USB_LIBELLE",u.usb_code||' '||u.usb_liblg AS "USB_CODE_LIBELLE"
,s.secb_id AS "SECTION_IDENTIFIANT",s.secb_num AS "SECTION_CODE",s.secb_liblg AS "SECTION_LIBELLE",s.secb_num||' '||s.secb_liblg AS "SECTION_CODE_LIBELLE"
FROM activite_de_recette@dblink_elabo_bidf a
LEFT JOIN categorie_activite@dblink_elabo_bidf ca ON ca.catv_code = a.catv_code
LEFT JOIN unite_spec_bud@dblink_elabo_bidf u ON u.usb_code = a.usb_code
LEFT JOIN section_budgetaire@dblink_elabo_bidf s ON s.secb_id = u.secb_id;
ALTER TABLE VMA_ACTIVITE_RECETTE ADD CONSTRAINT VMA_ACTIVITE_RECETTE_PK PRIMARY KEY (IDENTIFIANT);
ALTER TABLE VMA_ACTIVITE_RECETTE ADD CONSTRAINT VMA_ACTIVITE_RECETTE_UK_CODE UNIQUE (CODE);
CREATE INDEX VMA_ACTIVITE_RECETTE_K_S_ID ON VMA_ACTIVITE_RECETTE (SECTION_IDENTIFIANT ASC);
CREATE INDEX VMA_ACTIVITE_RECETTE_K_U_ID ON VMA_ACTIVITE (USB_IDENTIFIANT ASC);

DROP MATERIALIZED VIEW VMA_LIGNE_DEPENSE;
CREATE MATERIALIZED VIEW VMA_LIGNE_DEPENSE REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT ld.exo_num||a.ads_code||ne.nat_code||sf.sfin_code||b.bai_code AS "IDENTIFIANT"
,ld.ldep_id AS "LDEP_ID",fd.find_id AS "FIND_ID",ld.exo_num AS "EXERCICE"
,a.ads_id AS "ACTIVITE_IDENTIFIANT",a.ads_code AS "ACTIVITE_CODE",a.ads_code||' '||a.ads_liblg AS "ACTIVITE_CODE_LIBELLE"
,ne.nat_id AS "NATURE_ECONOMIQUE_IDENTIFIANT",ne.nat_code AS "NATURE_ECONOMIQUE_CODE",ne.nat_code||' '||ne.nat_liblg AS "NATURE_ECONOMIQUE_CODE_LIBELLE"
,sf.sfin_id AS "SOURCE_FINANCEMENT_IDENTIFIANT",sf.sfin_code AS "SOURCE_FINANCEMENT_CODE",sf.sfin_code||' '||sf.sfin_liblg AS "SF_CODE_LIBELLE"
,b.bai_id AS "BAILLEUR_IDENTIFIANT",b.bai_code AS "BAILLEUR_CODE",b.bai_code||' '||b.bai_designation AS "BAILLEUR_CODE_LIBELLE"
,nd.ndep_id AS "NATURE_DEPENSE_IDENTIFIANT",nd.ndep_code AS "NATURE_DEPENSE_CODE",nd.ndep_code||' '||nd.ndep_liblg AS "NATURE_DEPENSE_CODE_LIBELLE"
,ac.adp_id AS "ACTION_IDENTIFIANT",ac.adp_code AS "ACTION_CODE",ac.adp_code||' '||ac.adp_liblg AS "ACTION_CODE_LIBELLE"
,u.usb_id AS "USB_IDENTIFIANT",u.usb_code AS "USB_CODE",u.usb_code||' '||u.usb_liblg AS "USB_CODE_LIBELLE"
,s.secb_id AS "SECTION_IDENTIFIANT",s.secb_num AS "SECTION_CODE",s.secb_num||' '||s.secb_liblg AS "SECTION_CODE_LIBELLE"
,fd.FIND_BVOTE_AE AS "BUDGET_INITIAL_AE",fd.FIND_BVOTE_CP AS "BUDGET_INITIAL_CP"
,fd.FIND_MONTANT_AE AS "BUDGET_ACTUEL_AE",fd.FIND_MONTANT_CP AS "BUDGET_ACTUEL_CP"
FROM financement_depenses@dblink_elabo_bidf fd
LEFT JOIN ligne_de_depenses@dblink_elabo_bidf ld ON ld.ldep_id = fd.ldep_id
LEFT JOIN activite_de_service@dblink_elabo_bidf a ON a.ads_id = ld.ads_id
LEFT JOIN nature_depenses@dblink_elabo_bidf nd ON nd.ndep_id = a.ndep_id
LEFT JOIN nature_economique@dblink_elabo_bidf ne ON ne.nat_id = ld.nat_id
LEFT JOIN source_financement@dblink_elabo_bidf sf ON sf.sfin_id = fd.sfin_id
LEFT JOIN bailleur@dblink_elabo_bidf b ON b.bai_id = fd.bai_id
LEFT JOIN action@dblink_elabo_bidf ac ON ac.adp_id = a.adp_id
LEFT JOIN unite_spec_bud@dblink_elabo_bidf u ON u.usb_id = ac.usb_id
LEFT JOIN section_budgetaire@dblink_elabo_bidf s ON s.secb_id = u.secb_id;
ALTER TABLE VMA_LIGNE_DEPENSE ADD CONSTRAINT VMA_LIGNE_DEPENSE_PK PRIMARY KEY (IDENTIFIANT);
CREATE INDEX VMA_LIGNE_DEPENSE_K_EXERCICE ON VMA_LIGNE_DEPENSE (EXERCICE ASC);
CREATE INDEX VMA_LIGNE_DEPENSE_K_ACTIVITE ON VMA_LIGNE_DEPENSE (ACTIVITE_IDENTIFIANT ASC);
CREATE INDEX VMA_LIGNE_DEPENSE_K_NE ON VMA_LIGNE_DEPENSE (NATURE_ECONOMIQUE_IDENTIFIANT ASC);
CREATE INDEX VMA_LIGNE_DEPENSE_K_SF ON VMA_LIGNE_DEPENSE (SOURCE_FINANCEMENT_IDENTIFIANT ASC);
CREATE INDEX VMA_LIGNE_DEPENSE_K_BAILLEUR ON VMA_LIGNE_DEPENSE (BAILLEUR_IDENTIFIANT ASC);
CREATE INDEX VMA_LIGNE_DEPENSE_K_ND ON VMA_LIGNE_DEPENSE (NATURE_DEPENSE_IDENTIFIANT ASC);
CREATE INDEX VMA_LIGNE_DEPENSE_K_ACTION ON VMA_LIGNE_DEPENSE (ACTION_IDENTIFIANT ASC);
CREATE INDEX VMA_LIGNE_DEPENSE_K_USB ON VMA_LIGNE_DEPENSE (USB_IDENTIFIANT ASC);
CREATE INDEX VMA_LIGNE_DEPENSE_K_SECTION ON VMA_LIGNE_DEPENSE (SECTION_IDENTIFIANT ASC);

DROP MATERIALIZED VIEW VMA_LIGNE_RECETTE;
CREATE MATERIALIZED VIEW VMA_LIGNE_RECETTE REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT lr.exercice||a.ads_code||ne.nat_code AS "IDENTIFIANT",lr.exercice AS "EXERCICE",lr.rec_id AS "REC_ID"
,a.ads_code AS "ACTIVITE_IDENTIFIANT",a.ads_code AS "ACTIVITE_CODE",a.ads_code||' '||a.ads_liblg AS "ACTIVITE_CODE_LIBELLE"
,ne.nat_id AS "NATURE_ECONOMIQUE_IDENTIFIANT",ne.nat_code AS "NATURE_ECONOMIQUE_CODE",ne.nat_code||' '||ne.nat_liblg AS "NATURE_ECONOMIQUE_CODE_LIBELLE"
,u.usb_id AS "USB_IDENTIFIANT",u.usb_code AS "USB_CODE",u.usb_code||' '||u.usb_liblg AS "USB_CODE_LIBELLE"
,s.secb_id AS "SECTION_IDENTIFIANT",s.secb_num AS "SECTION_CODE",s.secb_num||' '||s.secb_liblg AS "SECTION_CODE_LIBELLE"
,lr.MONTANT AS "BUDGET_INITIAL",lr.MONTANT AS "BUDGET_ACTUEL"
FROM ligne_recette@dblink_elabo_bidf lr
LEFT JOIN activite_de_recette@dblink_elabo_bidf a ON a.ads_code = lr.ads_id
LEFT JOIN nature_economique@dblink_elabo_bidf ne ON ne.nat_id = lr.nat_id
LEFT JOIN unite_spec_bud@dblink_elabo_bidf u ON u.usb_code = a.usb_code
LEFT JOIN section_budgetaire@dblink_elabo_bidf s ON s.secb_id = u.secb_id;
ALTER TABLE VMA_LIGNE_RECETTE ADD CONSTRAINT VMA_LIGNE_RECETTE_PK PRIMARY KEY (IDENTIFIANT);
CREATE INDEX VMA_LIGNE_RECETTE_K_EXERCICE ON VMA_LIGNE_RECETTE (EXERCICE ASC);
CREATE INDEX VMA_LIGNE_RECETTE_K_ACTIVITE ON VMA_LIGNE_RECETTE (ACTIVITE_IDENTIFIANT ASC);
CREATE INDEX VMA_LIGNE_RECETTE_K_NE ON VMA_LIGNE_RECETTE (NATURE_ECONOMIQUE_IDENTIFIANT ASC);
CREATE INDEX VMA_LIGNE_RECETTE_K_USB ON VMA_LIGNE_RECETTE (USB_IDENTIFIANT ASC);
CREATE INDEX VMA_LIGNE_RECETTE_K_SECTION ON VMA_LIGNE_RECETTE (SECTION_IDENTIFIANT ASC);