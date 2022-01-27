CREATE OR REPLACE VIEW VA_DEPENSE_DISPONIBLE AS
SELECT
    fd.find_id AS "IDENTIFIANT"
    ,NVL(vlb.DISPONIBLE_AE,0) AS "AE",NVL(vlb.DISPONIBLE_CP,0) AS "CP"
FROM financement_depenses@dblink_elabo_bidf fd
LEFT JOIN vs_ligne_budgetaire@dblink_elabo_bidf vlb on vlb.fin_id = fd.find_id