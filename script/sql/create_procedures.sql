-- Dépense
CREATE OR REPLACE PROCEDURE PA_ACTUALISER_DEPENSE IS
BEGIN
    DBMS_MVIEW.REFRESH('VMA_DEPENSE', METHOD => 'C', ATOMIC_REFRESH => FALSE,PARALLELISM => 4);
END;

CREATE OR REPLACE PROCEDURE PA_IMPORTER_DEPENSE(version_acte IN VARCHAR2,audit_acteur IN VARCHAR2,audit_fonctionalite IN VARCHAR2,audit_action IN VARCHAR2,audit_date IN DATE) AS
BEGIN
    PA_ACTUALISER_DEPENSE();
    FOR l IN (SELECT l.* FROM VA_DEPENSE_IMPORTABLE l)
    LOOP
        INSERT INTO TA_DEPENSE(identifiant,version_acte,activite,nature_economique,source_financement,bailleur,ajustement_ae,ajustement_cp,audit_acteur,audit_fonctionnalite,audit_action,audit_date) VALUES (l.identifiant,version_acte,l.activite_identifiant,l.nature_economique_identifiant,l.source_financement_identifiant,l.bailleur_identifiant,0,0,audit_acteur,audit_fonctionalite,audit_action,audit_date);
    END LOOP;
    COMMIT;
END;

-- Recette
CREATE OR REPLACE PROCEDURE PA_ACTUALISER_RECETTE IS
BEGIN
    DBMS_MVIEW.REFRESH('VMA_RECETTE', METHOD => 'C', ATOMIC_REFRESH => FALSE,PARALLELISM => 4);
END;

CREATE OR REPLACE PROCEDURE PA_IMPORTER_LIGNE_RECETTE(version_acte IN VARCHAR2,audit_acteur IN VARCHAR2,audit_fonctionalite IN VARCHAR2,audit_action IN VARCHAR2,audit_date IN DATE) AS
BEGIN
    PA_ACTUALISER_RECETTE();
    FOR l IN (SELECT l.* FROM VA_RECETTE_IMPORTABLE l)
    LOOP
        INSERT INTO TA_RECETTE(identifiant,version_acte,activite,nature_economique,ajustement,audit_acteur,audit_fonctionnalite,audit_action,audit_date) VALUES (l.identifiant,version_acte,l.activite_identifiant,l.nature_economique_identifiant,0,audit_acteur,audit_fonctionalite,audit_action,audit_date);
    END LOOP;
    COMMIT;
END;