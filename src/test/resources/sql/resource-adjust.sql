INSERT INTO TA_ACTE(IDENTIFIANT,CODE,LIBELLE,EXERCICE) VALUES ('1','1','1',2021);
INSERT INTO TA_VERSION_ACTE(IDENTIFIANT,CODE,LIBELLE,ACTE,NUMERO,DATE_CREATION) VALUES ('1','1','1','1',1, DATE '2021-01-01');
INSERT INTO TA_LIGNE_RECETTE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,VERSION_ACTE,AJUSTEMENT,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('1','1','1','1',0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_LIGNE_RECETTE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,VERSION_ACTE,AJUSTEMENT,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2','2','1','1',0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
-- identifier not exist
-- identifier available not enough
INSERT INTO TA_LIGNE_RECETTE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,VERSION_ACTE,AJUSTEMENT,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('identifier_available_not_enough','3','1','1',0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');