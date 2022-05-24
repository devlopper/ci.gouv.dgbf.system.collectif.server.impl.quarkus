CREATE OR REPLACE VIEW VT_DEPENSE_AJUSTEE AS
SELECT
    v.EXERCICE
  , v.SECTION_CODE
  , v.SECTION_CODE_LIBELLE
  , v.USB_CODE
  , v.USB_CODE_LIBELLE
  , v.ACTION_CODE
  , v.ACTION_CODE_LIBELLE
  , v.ACTIVITE_CODE_LIBELLE
  , v.ACTIVITE_CODE
  , v.NATURE_DEPENSE_CODE_LIBELLE
  , v.NATURE_ECONOMIQUE_CODE
  , v.NATURE_ECONOMIQUE_CODE_LIBELLE
  , v.SOURCE_FINANCEMENT_CODE
  , v.BAILLEUR_CODE
  , v.SF_CODE_LIBELLE
  , v.BAILLEUR_CODE_LIBELLE
  , v.BUDGET_INITIAL_AE as vote_ae
  , v.BUDGET_INITIAL_CP as vote_cp
  , v.BUDGET_ACTUEL_AE
  , v.BUDGET_ACTUEL_CP
  , disponible.disponible_ae
  , disponible.disponible_cp
  , d.AJUSTEMENT_AE                    as VARIATION_AE
  , d.AJUSTEMENT_CP                    as VARIATION_CP
  , v.BUDGET_ACTUEL_AE+d.AJUSTEMENT_AE as BUDGET_FINAL_AE
  , v.BUDGET_ACTUEL_CP+d.AJUSTEMENT_CP as BUDGET_FINAL_CP
  , v.ldep_id
  , v.find_id
FROM
    TA_DEPENSE d
    LEFT JOIN
        VMA_DEPENSE v
        ON
            v.identifiant = d.identifiant
    LEFT JOIN
        ut_bidf_tamp.vs_ligne_budgetaire@dblink_elabo_bidf disponible
        on
            v.find_id     =disponible.fin_id
            and v.exercice=disponible.ab_exercice
WHERE
    d.ajustement_ae    <> 0
    or d.ajustement_cp <> 0
;

CREATE OR REPLACE VIEW VS_SAISIE_COLLECTIF AS
SELECT * FROM VT_DEPENSE_AJUSTEE;