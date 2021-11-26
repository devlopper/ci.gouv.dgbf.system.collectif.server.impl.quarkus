CREATE OR REPLACE PROCEDURE PA_IMPORTER_LIGNE_DEPENSE_NVL(audit_acteur IN VARCHAR2,audit_fonctionnalite IN VARCHAR2,audit_action IN VARCHAR2,audit_date IN DATE) AS
    vsql VARCHAR2(4000);
BEGIN
    vsql := 'MERGE INTO SIIBC_COLLECTIF.TA_LIGNE_DEPENSE l USING SIIBC_COLLECTIF.VA_LIGNE_DEPENSE_INITIALE i ON (l.identifiant = i.identifiant) '
    ||'WHEN NOT MATCHED THEN INSERT '
    ||'(identifiant, version_acte, activite,nature_economique,source_financement,bailleur,ajustement_ae,ajustement_cp,audit_acteur,audit_fonctionnalite,audit_action,audit_date'
    ||') values (i.identifiant,i.version_acte,i.activite,i.nature_economique,i.source_financement,i.bailleur,0,0'
    -- audit
    ||',:1,:2,:3,:4'
    ||')'
    ;
    EXECUTE IMMEDIATE vsql USING audit_acteur,audit_fonctionnalite,audit_action,audit_date;
    COMMIT;
END;