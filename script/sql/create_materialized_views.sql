-- Liste des actes de gestions
DROP MATERIALIZED VIEW VMA_ACTE_GESTION;
CREATE MATERIALIZED VIEW VMA_ACTE_GESTION REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT 
    ab.acte_id AS "IDENTIFIANT"
    ,TO_CHAR(ab.acte_numero) AS "CODE"
    ,TO_CHAR(ab.acte_numero)||' '||ab.acte_ref_externe_acte as LIBELLE
    ,SUM(fd.find_montant_ae) as "MONTANT_AE"
    ,SUM(fd.find_montant_cp) as "MONTANT_CP"
FROM 
    meatest.acte_budgetaire ab
JOIN meatest.financement_depenses fd ON fd.acte_id = ab.acte_id
GROUP BY ab.acte_id,ab.acte_numero,ab.acte_ref_externe_acte
ORDER BY TO_CHAR(ab.acte_numero) ASC;
ALTER TABLE VMA_ACTE_GESTION ADD CONSTRAINT VMA_ACTE_GESTION_PK PRIMARY KEY (IDENTIFIANT);

-- Liste des lignes de dépenses des actes de gestion
DROP MATERIALIZED VIEW VMA_ACTE_GESTION_LIGNE_DEPENSE;
CREATE MATERIALIZED VIEW VMA_ACTE_GESTION_LIGNE_DEPENSE REFRESH NEXT SYSDATE + 1/(24) COMPLETE AS
SELECT
    o.acte_id||o.ldep_id||o.find_id AS "IDENTIFIANT"
    ,o.acte_id AS "ACTE"
    ,ab.exo_num AS "EXERCICE"
    ,ld.dep_activite_id AS "ACTIVITE"
    ,ld.dep_nature_eco_id AS "NATURE_ECONOMIQUE"
    ,fd.fin_sfin_id AS "SOURCE_FINANCEMENT"
    ,fd.bailleur_id AS "BAILLEUR"
    ,o.montant_ae AS "MONTANT_AE"
    ,o.montant_cp AS "MONTANT_CP"
FROM MEATEST.operation_acte o
LEFT JOIN meatest.acte_budgetaire ab ON ab.acte_id = o.acte_id
LEFT JOIN BUDGET.ligne_depense ld ON ld.uuid = o.ldep_id
LEFT JOIN BUDGET.financement fd ON fd.uuid = o.find_id
ORDER BY ab.exo_num DESC,ld.dep_activite_id ASC,ld.dep_nature_eco_id ASC,fd.fin_sfin_id ASC,fd.bailleur_id ASC;
ALTER TABLE VMA_ACTE_GESTION_LIGNE_DEPENSE ADD CONSTRAINT VMA_ACTE_GESTION_LIGNE_DEPENSE_PK PRIMARY KEY (IDENTIFIANT);
CREATE INDEX VMA_ACTE_GESTION_LIGNE_DEPENSE_K_ACTE ON VMA_ACTE_GESTION_LIGNE_DEPENSE (ACTE ASC);

