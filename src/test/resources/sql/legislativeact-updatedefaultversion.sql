INSERT INTO TA_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,EXERCICE,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE,EN_COURS) VALUES ('1','1','Collectif Budgétaire 2021 du 01/01/2021','2021','christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT',1);
INSERT INTO TA_VERSION_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,COLLECTIF,NUMERO,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('1_1','1_1','Version du Collectif Budgétaire 2021 du 01/01/2021','1',1,'christian','CREATION', DATE '2000-1-1','CREATION');
INSERT INTO TA_VERSION_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,COLLECTIF,NUMERO,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('1_2','1_2','2','1',2,'christian','CREATION', DATE '2000-1-1','CREATION');

INSERT INTO TA_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,EXERCICE,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE,EN_COURS) VALUES ('2','2','Collectif Budgétaire 2021 du 01/07/2021','2021','christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT',1);
INSERT INTO TA_VERSION_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,COLLECTIF,NUMERO,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2_1','2_1','1','2',1,'christian','CREATION', DATE '2000-1-1','CREATION');

UPDATE TA_COLLECTIF SET VERSION_PAR_DEFAUT = '1_1' WHERE IDENTIFIANT = '1';
UPDATE TA_COLLECTIF SET VERSION_PAR_DEFAUT = '2_1' WHERE IDENTIFIANT = '2'; 