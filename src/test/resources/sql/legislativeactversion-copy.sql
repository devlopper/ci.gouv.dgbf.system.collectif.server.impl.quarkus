INSERT INTO TA_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,EXERCICE,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE,EN_COURS) VALUES ('2022_1','2022_1','C2022_1','2021','christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT',1);

INSERT INTO TA_VERSION_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,COLLECTIF,NUMERO,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2022_1_1','2022_1_1','2022_1_1','2022_1',1,'christian','CREATION', DATE '2000-1-1','CREATION');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2022_1_1_1','1','1','1','1','2022_1_1',0,6,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');

INSERT INTO TA_VERSION_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,COLLECTIF,NUMERO,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2022_1_2','2022_1_2','2022_1_2','2022_1',1,'christian','CREATION', DATE '2000-1-1','CREATION');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2022_1_2_1','1','1','1','1','2022_1_2',2,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
