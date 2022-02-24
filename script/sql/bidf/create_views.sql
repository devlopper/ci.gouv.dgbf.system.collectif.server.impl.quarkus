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
	,fd.FIND_BVOTE_AE AS "BUDGET_INITIAL_AE",fd.FIND_BVOTE_CP AS "BUDGET_INITIAL_CP"
	,fd.FIND_MONTANT_AE - fd.FIND_BVOTE_AE AS "MOUVEMENT_AE",fd.FIND_MONTANT_CP - fd.FIND_BVOTE_CP AS "MOUVEMENT_CP"
	,fd.FIND_MONTANT_AE AS "BUDGET_ACTUEL_AE",fd.FIND_MONTANT_CP AS "BUDGET_ACTUEL_CP"
FROM financement_depenses fd
LEFT JOIN ligne_de_depenses ld ON ld.ldep_id = fd.ldep_id AND ld.exo_num = fd.exo_num
LEFT JOIN activite_de_service a ON a.ads_id = ld.ads_id
LEFT JOIN nature_depenses nd ON nd.ndep_id = a.ndep_id
LEFT JOIN nature_economique ne ON ne.nat_id = ld.nat_id
LEFT JOIN source_financement sf ON sf.sfin_id = fd.sfin_id
LEFT JOIN bailleur b ON b.bai_id = fd.bai_id
LEFT JOIN action ac ON ac.adp_id = a.adp_id
LEFT JOIN unite_spec_bud u ON u.usb_id = ac.usb_id
LEFT JOIN section_budgetaire s ON s.secb_id = u.secb_id;

-- Disponibles dépenses
CREATE OR REPLACE VIEW VA_DEPENSE_DISPONIBLE AS
SELECT
    fd.find_id AS "IDENTIFIANT",NVL(vlb.DISPONIBLE_AE,0) AS "AE",NVL(vlb.DISPONIBLE_CP,0) AS "CP"
FROM financement_depenses fd
LEFT JOIN vs_ligne_budgetaire vlb on vlb.fin_id = fd.find_id;