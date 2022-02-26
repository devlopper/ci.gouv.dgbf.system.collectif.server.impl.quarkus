-- Actes de gestion
DROP MATERIALIZED VIEW VMA_ACTE_GESTION;
CREATE MATERIALIZED VIEW VMA_ACTE_GESTION REFRESH NEXT SYSDATE + 1/24/4 COMPLETE AS
SELECT 
    ab.acte_id AS "IDENTIFIANT"
    ,TO_CHAR(ab.acte_numero) AS "CODE"
    ,TO_CHAR(ab.acte_numero)||' '||ab.acte_ref_externe_acte as LIBELLE
    ,TO_NUMBER(ab.exo_num) AS "EXERCICE"
    ,ab.acte_date_signature AS "DATE"
    ,SUM(fd.find_montant_ae) as "MONTANT_AE"
    ,SUM(fd.find_montant_cp) as "MONTANT_CP"
FROM 
    SIIBC_MEA.acte_budgetaire ab
JOIN SIIBC_MEA.financement_depenses fd ON fd.acte_id = ab.acte_id
JOIN SIIBC_MEA.categorie_acte_budgetaire cab ON cab.cab_id = ab.acte_cab_id AND cab.cab_tact_code IN ('REGLEMENTAIRE')
WHERE ab.acte_statut = 'APPLIQUE'
GROUP BY ab.acte_id,ab.acte_numero,ab.acte_ref_externe_acte,ab.exo_num,ab.acte_date_signature
ORDER BY TO_CHAR(ab.acte_numero) ASC;
ALTER TABLE VMA_ACTE_GESTION ADD CONSTRAINT VMA_ACTE_GESTION_PK PRIMARY KEY (IDENTIFIANT);
ALTER TABLE VMA_ACTE_GESTION ADD CONSTRAINT VMA_ACTE_GESTION_UK_CODE UNIQUE (CODE);

-- Dépenses des actes de gestion
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

-- Recettes des actes de gestion
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