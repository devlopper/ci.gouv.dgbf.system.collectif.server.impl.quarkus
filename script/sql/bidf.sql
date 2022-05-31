------------------------------------------------------------------------------------------------
--- Ces vues permettent de d'accélerer l'accès aux données à travers le lien base de données ---
------------------------------------------------------------------------------------------------

-- Dépenses
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
	,cb.uuid AS "CATEGORIE_BUDGET_IDENTIFIANT",cb.cbud_code AS "CATEGORIE_BUDGET_CODE",cb.cbud_liblg AS "CATEGORIE_BUDGET_LIBELLE",cb.cbud_code||' '||cb.cbud_liblg AS "CATEGORIE_BUDGET_CODE_LIBELLE"
	,NVL(fd.FIND_BVOTE_AE,0) AS "BUDGET_INITIAL_AE",NVL(fd.FIND_BVOTE_CP,0) AS "BUDGET_INITIAL_CP"
	,NVL(fd.FIND_MONTANT_AE,0) - NVL(fd.FIND_BVOTE_AE,0) AS "MOUVEMENT_AE",NVL(fd.FIND_MONTANT_CP,0) - NVL(fd.FIND_BVOTE_CP,0) AS "MOUVEMENT_CP"
	,NVL(fd.FIND_MONTANT_AE,0) AS "BUDGET_ACTUEL_AE",NVL(fd.FIND_MONTANT_CP,0) AS "BUDGET_ACTUEL_CP"
FROM financement_depenses fd
LEFT JOIN ligne_de_depenses ld ON ld.ldep_id = fd.ldep_id AND ld.exo_num = fd.exo_num
LEFT JOIN activite_de_service a ON a.ads_id = ld.ads_id
LEFT JOIN nature_depenses nd ON nd.ndep_id = a.ndep_id
LEFT JOIN nature_economique ne ON ne.nat_id = ld.nat_id
LEFT JOIN source_financement sf ON sf.sfin_id = fd.sfin_id
LEFT JOIN bailleur b ON b.bai_id = fd.bai_id
LEFT JOIN action ac ON ac.adp_id = a.adp_id
LEFT JOIN unite_spec_bud u ON u.usb_id = ac.usb_id
LEFT JOIN categorie_budget cb ON cb.uuid = u.cbud_id
LEFT JOIN section_budgetaire s ON s.secb_id = u.secb_id
WHERE ld.ads_id IS NOT NULL AND ld.nat_id IS NOT NULL AND fd.sfin_id IS NOT NULL AND fd.bai_id IS NOT NULL;

-- Disponibles dépenses
CREATE OR REPLACE VIEW VA_DEPENSE_DISPONIBLE AS
SELECT
    d.ab_exercice AS "EXERCICE",d.ads_id AS "ACTIVITE",d.nat_id AS "NATURE_ECONOMIQUE",d.fin_sfin_id AS "SOURCE_FINANCEMENT",d.bailleur_id AS "BAILLEUR"
    ,NVL(d.DISPONIBLE_AE,0) AS "AE",NVL(d.DISPONIBLE_CP,0) AS "CP"
FROM vs_ligne_budgetaire d;

-- Ressources
CREATE OR REPLACE VIEW VA_RESSOURCE AS
SELECT 
	TO_NUMBER(lr.exo_num) AS "EXERCICE",lr.nrec_id AS "REC_ID"
	,a.ads_code AS "ACTIVITE_IDENTIFIANT",a.ads_code AS "ACTIVITE_CODE",a.ads_code||' '||a.ads_liblg AS "ACTIVITE_CODE_LIBELLE"
	,ne.nat_id AS "NATURE_ECONOMIQUE_IDENTIFIANT",ne.nat_code AS "NATURE_ECONOMIQUE_CODE",ne.nat_code||' '||ne.nat_liblg AS "NATURE_ECONOMIQUE_CODE_LIBELLE"
	,u.usb_id AS "USB_IDENTIFIANT",u.usb_code AS "USB_CODE",u.usb_code||' '||u.usb_liblg AS "USB_CODE_LIBELLE"
	,s.secb_id AS "SECTION_IDENTIFIANT",s.secb_num AS "SECTION_CODE",s.secb_num||' '||s.secb_liblg AS "SECTION_CODE_LIBELLE"
	,NVL(lr.MONTANT_INITIAL,0) AS "BUDGET_INITIAL",NVL(lr.MONTANT_ACTUEL,0) AS "BUDGET_ACTUEL",NVL(lr.MONTANT_ACTUEL,0)-NVL(lr.MONTANT_INITIAL,0) AS "MOUVEMENT"
FROM ligne_recette lr
LEFT JOIN activite_de_recette a ON a.ads_code = lr.ads_id
LEFT JOIN nature_economique ne ON ne.nat_id = lr.nat_id
LEFT JOIN unite_spec_bud u ON u.usb_code = a.usb_code
LEFT JOIN section_budgetaire s ON s.secb_id = u.secb_id
WHERE lr.ads_id IS NOt NULL AND a.ads_code IS NOT NULL AND lr.nat_id IS NOT NULL AND ne.nat_id IS NOT NULL;

------------------------------------------------------------------------------------------------------------------------------------------------
-- Budget actuel des collectifs
------------------------------------------------------------------------------------------------------------------------------------------------
DROP TABLE TA_DEPENSE_ACTUEL_17052022;
CREATE TABLE TA_DEPENSE_ACTUEL_17052022 AS 
SELECT
    v.exercice||'1'||v.ads_id||v.nat_id||v.sfin_id||v.bai_id AS "IDENTIFIANT"
    ,v.exercice AS "EXERCICE",1 AS "NUMERO",v.ads_id AS "ACTIVITE",v.nat_id AS "NATURE_ECONOMIQUE",v.sfin_id AS "SOURCE_FINANCEMENT",v.bai_id AS "BAILLEUR"
    ,ae_actuel AS "AE",cp_actuel AS "CP"
FROM VS_EXECUTION_A_DATE v
WHERE v.exercice = '2022' and v.date_comptable = '17/05/2022';
ALTER TABLE TA_DEPENSE_ACTUEL_17052022 ADD CONSTRAINT TA_DEPENSE_ACTUEL_17052022_PK PRIMARY KEY (IDENTIFIANT);

DROP TABLE TA_DEPENSE_ACTUEL_COLLECTIF;
CREATE TABLE TA_DEPENSE_ACTUEL_COLLECTIF AS 
SELECT t.* FROM TA_DEPENSE_ACTUEL_17052022 t;
ALTER TABLE TA_DEPENSE_ACTUEL_COLLECTIF ADD CONSTRAINT TA_DEPENSE_ACTUEL_COLLECTIF_PK PRIMARY KEY (IDENTIFIANT);
CREATE INDEX TA_DEPENSE_ACTUEL_COL_K_E_N ON TA_DEPENSE_ACTUEL_COLLECTIF (EXERCICE,NUMERO);