-- Section
CREATE OR REPLACE VIEW VA_SECTION AS
SELECT s.uuid AS "IDENTIFIANT",s.secb_code AS "CODE",s.secb_libelle AS "LIBELLE"
FROM SIIBC_CA.section_budgetaire s,SIIBC_CA.gouvernement g
WHERE s.gouv_id = g.uuid AND g.gouv_statut = 'ENABLED' AND g.gouv_utilise = 1 AND s.entitystatus = 'COMMITTED';

-- Unité administrative
CREATE OR REPLACE VIEW VA_UNITE_ADMINISTRATIVE AS
SELECT ua.uuid AS "IDENTIFIANT",ua.ua_code AS "CODE",ua.ua_liblg AS "LIBELLE"
    ,CASE WHEN s.uuid IS NULL THEN NULL ELSE s.uuid END AS "SECTION"
    ,CASE WHEN s.uuid IS NULL THEN NULL ELSE s.uuid END AS "SECTION_IDENTIFIANT"
    ,CASE WHEN s.uuid IS NULL THEN NULL ELSE s.secb_code END AS "SECTION_CODE"
    ,CASE WHEN s.uuid IS NULL THEN NULL ELSE s.secb_code||' '||s.secb_libelle END AS "SECTION_CODE_LIBELLE"
FROM SIIBC_CA.unite_administrative ua,SIIBC_CA.section_budgetaire s
WHERE s.uuid (+) = ua.ua_secb_id AND ua.entitystatus = 'COMMITTED' AND s.entitystatus = 'COMMITTED';

-- Type unité de spécialisation du budget
CREATE OR REPLACE VIEW VA_TYPE_USB AS
SELECT type_usb.uuid AS "IDENTIFIANT",type_usb.tusb_code AS "CODE",type_usb.tusb_liblg AS "LIBELLE"
FROM SIIBC_CPP.type_usb type_usb
WHERE type_usb.entitystatus = 'COMMITTED';

-- Catégorie unité de spécialisation du budget
CREATE OR REPLACE VIEW VA_CATEGORIE_USB AS
SELECT categorie_usb.uuid AS "IDENTIFIANT",categorie_usb.cusb_code AS "CODE"
,categorie_usb.cusb_lib AS "LIBELLE",categorie_usb.tusb_id AS "TYPE"
FROM SIIBC_CPP.categorie_usb categorie_usb
WHERE categorie_usb.entitystatus = 'COMMITTED';

-- Catégorie budget
CREATE OR REPLACE VIEW VA_CATEGORIE_BUDGET AS
SELECT categorie_budget.uuid AS "IDENTIFIANT",categorie_budget.cbud_code AS "CODE",categorie_budget.cbud_liblg AS "LIBELLE"
FROM SIIBC_CPP.categorie_budget categorie_budget
WHERE categorie_budget.entitystatus = 'COMMITTED';

-- Unité de spécialisation du budget
CREATE OR REPLACE VIEW VA_USB AS
SELECT usb.uuid AS "IDENTIFIANT",usb.usb_code AS "CODE",usb.usb_liblg AS "LIBELLE",usb.usb_cusb_id AS "CATEGORIE"
,s.uuid AS "SECTION",s.uuid AS "SECTION_IDENTIFIANT",s.secb_code AS "SECTION_CODE",s.secb_code||' '||s.secb_libelle AS "SECTION_CODE_LIBELLE"
FROM SIIBC_CPP.usb usb,SIIBC_CA.section_budgetaire s
WHERE s.uuid = usb.usb_secb_id AND usb.entitystatus = 'COMMITTED' AND s.entitystatus = 'COMMITTED';

-- Action
CREATE OR REPLACE VIEW VA_ACTION AS
SELECT a.uuid AS "IDENTIFIANT",a.adp_code AS "CODE",a.adp_liblg AS "LIBELLE"
,s.uuid AS "SECTION",s.uuid AS "SECTION_IDENTIFIANT",s.secb_code AS "SECTION_CODE",s.secb_code||' '||s.secb_libelle AS "SECTION_CODE_LIBELLE"
,usb.uuid AS "USB",usb.uuid AS "USB_IDENTIFIANT",usb.usb_code AS "USB_CODE",usb.usb_code||' '||usb.usb_liblg AS "USB_CODE_LIBELLE"
FROM SIIBC_CPP.action a,SIIBC_CPP.usb usb,SIIBC_CA.section_budgetaire s
WHERE a.adp_usb_id = usb.uuid AND s.uuid = usb.usb_secb_id AND usb.entitystatus = 'COMMITTED' AND a.entitystatus = 'COMMITTED';

-- Catégorie Activité
CREATE OR REPLACE VIEW VA_CATEGORIE_ACTIVITE AS
SELECT ca.catv_id AS "IDENTIFIANT",ca.catv_code AS "CODE",ca.catv_liblg AS "LIBELLE"
FROM SIIBC_ADS.categorie_activite ca;

-- Activité
CREATE OR REPLACE VIEW VA_ACTIVITE AS
SELECT a.ads_id AS "IDENTIFIANT",a.ads_code AS "CODE",a.ads_liblg AS "LIBELLE"
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

-- Nature de dépense
CREATE OR REPLACE VIEW VA_NATURE_DEPENSE AS
SELECT t.uuid AS "IDENTIFIANT",t.ndep_code AS "CODE",t.ndep_libct AS "LIBELLE"
FROM SIIBC_NEC.nature_depense t;

-- Nature économique
CREATE OR REPLACE VIEW VA_NATURE_ECONOMIQUE AS
SELECT n.uuid AS "IDENTIFIANT",n.nat_code AS "CODE",n.nat_liblg AS "LIBELLE"
FROM SIIBC_NEC.nature_economique n,SIIBC_NEC.TABLE_REFERENTIEL tref,SIIBC_NEC.VERSION_REFERENTIEL vref
WHERE n.nat_tref = tref.uuid (+) AND tref.tref_vers_id = vref.uuid AND vref.VERS_CODE='312' AND n.nat_imputable=1 AND n.nat_nat is null;

-- Bailleur
CREATE OR REPLACE VIEW VA_BAILLEUR AS
SELECT b.uuid AS "IDENTIFIANT",b.bai_code AS "CODE",b.bai_lib AS "LIBELLE"
FROM SIIBC_FINEXT.bailleur b;

-- Source financement
CREATE OR REPLACE VIEW VA_SOURCE_FINANCEMENT AS
SELECT sf.uuid AS "IDENTIFIANT",sf.src_code AS "CODE",sf.src_libelle AS "LIBELLE"
FROM SIIBC_BUDGET.source_financement sf;