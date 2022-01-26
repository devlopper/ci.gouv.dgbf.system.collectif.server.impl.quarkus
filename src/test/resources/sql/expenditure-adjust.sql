INSERT INTO TA_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,EXERCICE,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('1','1','1',2021,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_VERSION_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,ACTE,NUMERO,DATE_CREATION) VALUES ('1','1','1','1',1, DATE '2021-01-01');
INSERT INTO TA_LIGNE_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_ACTE,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('1','1','1','1','1','1',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_LIGNE_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_ACTE,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2','2','1','1','1','1',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
-- identifier not exist
-- identifier available not enough
INSERT INTO TA_LIGNE_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_ACTE,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('identifier_available_not_enough','3','1','1','1','1',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT into VA_LIGNE_DEPENSE_DISPONIBLE (IDENTIFIANT,AE,CP) values ('identifier_available_not_enough',0,0);