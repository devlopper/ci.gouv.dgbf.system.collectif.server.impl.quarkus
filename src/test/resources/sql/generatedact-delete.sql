INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('1','1','1',2021,0,0);
INSERT INTO VMA_ACTE_GESTION_LIGNE_DEPENSE(IDENTIFIANT,ACTE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('1_1','1','1','1','1','1',10,10);
INSERT INTO VMA_ACTE_GESTION_LIGNE_DEPENSE(IDENTIFIANT,ACTE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('1_2','1','2','1','1','1',-10,-10);

INSERT INTO TA_ACTE(IDENTIFIANT,CODE,LIBELLE,EXERCICE) VALUES ('1','1','Collectif budgétaire 2021',2021);

INSERT INTO TA_VERSION_ACTE(IDENTIFIANT,CODE,LIBELLE,ACTE,NUMERO) VALUES ('1','1','Version 1 du Collectif budgétaire 2021','1',1);

INSERT INTO TA_VERSION_ACTE(IDENTIFIANT,CODE,LIBELLE,ACTE,NUMERO) VALUES ('2','2','Version 2 du Collectif budgétaire 2021','1',3);
INSERT INTO TA_ACTE_GENERE(IDENTIFIANT,CODE,LIBELLE,VERSION_COLLECTIF,TYPE,ACTE_SOURCE,APPLIQUE,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2','2','2','2','AJUSTEMENT','2',0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_ACTE_GENERE(IDENTIFIANT,CODE,LIBELLE,VERSION_COLLECTIF,TYPE,ACTE_SOURCE,APPLIQUE,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('A_3','A_3','A 3','2','ANNULATION','3',0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_ACTE_GENERE(IDENTIFIANT,CODE,LIBELLE,VERSION_COLLECTIF,TYPE,ACTE_SOURCE,APPLIQUE,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('A_4','A_4','A 4','2','ANNULATION','4',0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_ACTE_GENERE(IDENTIFIANT,CODE,LIBELLE,VERSION_COLLECTIF,TYPE,ACTE_SOURCE,APPLIQUE,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('3','3','3','2','REPOSITIONNEMENT','3',0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_ACTE_GENERE(IDENTIFIANT,CODE,LIBELLE,VERSION_COLLECTIF,TYPE,ACTE_SOURCE,APPLIQUE,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('4','4','4','2','REPOSITIONNEMENT','4',0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');

