CREATE OR REPLACE PROCEDURE PA_ACTUALISER_LIGNE_DEPENSE IS
BEGIN
    DBMS_MVIEW.REFRESH('VMA_LIGNE_DEPENSE', METHOD => 'C', ATOMIC_REFRESH => FALSE,PARALLELISM => 4);
END;

CREATE OR REPLACE PROCEDURE PA_IMPORTER_LIGNE_DEPENSE(audit_acteur IN VARCHAR2,audit_fonctionalite IN VARCHAR2,audit_action IN VARCHAR2,audit_date IN DATE) AS
BEGIN
    PA_ACTUALISER_LIGNE_DEPENSE();
    FOR l IN (SELECT l.* FROM VA_LIGNE_DEPENSE_IMPORTABLE l)
    LOOP
        INSERT INTO TA_LIGNE_DEPENSE(identifiant,version_acte,activite,nature_economique,source_financement,bailleur,ajustement_ae,ajustement_cp,audit_acteur,audit_fonctionnalite,audit_action,audit_date) VALUES (l.identifiant,'1',l.activite_identifiant,l.nature_economique_identifiant,l.source_financement_identifiant,l.bailleur_identifiant,0,0,audit_acteur,audit_fonctionalite,audit_action,audit_date);
    END LOOP;
    COMMIT;
END;