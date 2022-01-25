INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('1','1','1',2021,0,0);
INSERT INTO VMA_ACTE_GESTION_LIGNE_DEPENSE(IDENTIFIANT,ACTE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('1_1','1','1','1','1','1',10,10);
INSERT INTO VMA_ACTE_GESTION_LIGNE_DEPENSE(IDENTIFIANT,ACTE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('1_2','1','2','1','1','1',-10,-10);

INSERT INTO TA_ACTE(IDENTIFIANT,CODE,LIBELLE,EXERCICE) VALUES ('1','1','Collectif budgétaire 2021',2021);

INSERT INTO TA_VERSION_ACTE(IDENTIFIANT,CODE,LIBELLE,ACTE,NUMERO,DATE_CREATION) VALUES ('1','1','Version 1 du Collectif budgétaire 2021','1',1, DATE '2021-01-01');
INSERT INTO TA_LIGNE_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_ACTE,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('1','1','1','1','1','1',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_LIGNE_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_ACTE,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2','2','1','1','1','1',1,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_LIGNE_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_ACTE,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('3','3','1','1','1','1',0,2,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_LIGNE_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_ACTE,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('4','4','1','1','1','1',3,3,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');

INSERT INTO TA_ACTE_GESTION_VERSION_ACTE(IDENTIFIANT,ACTE_GESTION,VERSION_ACTE,INCLUS,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('1','1','1',1,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');

INSERT INTO TA_VERSION_ACTE(IDENTIFIANT,CODE,LIBELLE,ACTE,NUMERO,DATE_CREATION) VALUES ('2','2','Version 2 du Collectif budgétaire 2021','1',2, DATE '2021-01-01');

INSERT INTO TA_VERSION_ACTE(IDENTIFIANT,CODE,LIBELLE,ACTE,NUMERO,DATE_CREATION) VALUES ('3','3','Version 3 du Collectif budgétaire 2021','1',3, DATE '2021-01-01');
INSERT INTO TA_ACTE_GENERE(IDENTIFIANT,CODE,LIBELLE,VERSION_COLLECTIF,TYPE,ACTE_SOURCE,APPLIQUE,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('3','3','3','3','AJUSTEMENT','3',1,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');