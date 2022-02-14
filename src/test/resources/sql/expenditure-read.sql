INSERT INTO VMA_EXERCICE(IDENTIFIANT,CODE,LIBELLE,ANNEE) VALUES ('2021','2021','2021',2021);
INSERT INTO VMA_EXERCICE(IDENTIFIANT,CODE,LIBELLE,ANNEE) VALUES ('2022','2022','2022',2022);
INSERT INTO TA_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,EXERCICE,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE,EN_COURS) VALUES ('2021_1','2021_1','2021_1','2021','christian','CREATION', DATE '2000-1-1','CREATION',1);
INSERT INTO TA_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,EXERCICE,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE,EN_COURS) VALUES ('2022_1','2022_1','2022_1','2022','christian','CREATION', DATE '2000-1-1','CREATION',1);
INSERT INTO TA_VERSION_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,COLLECTIF,NUMERO,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2021_1_1','2021_1_1','2021_1_1','2021_1',1,'christian','CREATION', DATE '2000-1-1','CREATION');
INSERT INTO TA_VERSION_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,COLLECTIF,NUMERO,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2022_1_1','2022_1_1','2022_1_1','2022_1',1,'christian','CREATION', DATE '2000-1-1','CREATION');

INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2021_1_1_1','1','1','1','1','2021_1_1',100,100,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2021_1_1_2','1','1','1','2','2021_1_1',200,200,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2021_1_1_3','1','1','1','3','2021_1_1',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2021_1_1_4','1','1','1','4','2021_1_1',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2021_1_1_5','1','1','1','5','2021_1_1',-10,-10,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2021_1_1_6','1','1','1','6','2021_1_1',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2021_1_1_7','1','1','1','7','2021_1_1',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');

INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2022_1_1_1','1','1','1','1','2022_1_1',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2022_1_1_2','1','1','1','2','2022_1_1',2,2,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2022_1_1_3','1','1','1','3','2022_1_1',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2022_1_1_4','1','1','1','4','2022_1_1',5,5,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2022_1_1_5','1','1','1','5','2022_1_1',-1,-1,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2022_1_1_6','1','1','1','6','2022_1_1',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2022_1_1_7','1','1','1','7','2022_1_1',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');

INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('2021_1','2021_1','2021_1',2021,0,0);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2021_1_1','2021_1','1','1','1','3',0,0);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2021_1_2','2021_1','1','1','1','4',0,0);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2021_1_3','2021_1','1','1','1','6',100,100);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2021_1_4','2021_1','1','1','1','7',0,0);

INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('2021_2','2021_2','2021_2',2021,0,0);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2021_2_1','2021_2','1','1','1','3',0,0);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2021_2_2','2021_2','1','1','1','4',0,0);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2021_2_3','2021_2','1','1','1','6',0,0);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2021_2_4','2021_2','1','1','1','7',15,15);

INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('2022_1','2022_1','2022_1',2022,0,0);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2022_1_1','2022_1','1','1','1','1',0,0);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2022_1_2','2022_1','1','1','1','2',0,0);

INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('2022_2','2022_2','2022_2',2022,0,0);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2022_2_1','2022_2','1','1','1','3',0,0);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2022_2_2','2022_2','1','1','1','4',0,0);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2022_2_3','2022_2','1','1','1','6',8,8);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2022_2_4','2022_2','1','1','1','7',0,0);

INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('2022_3','2022_3','2022_3',2022,0,0);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2022_3_1','2022_3','1','1','1','3',0,0);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2022_3_2','2022_3','1','1','1','4',0,0);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2022_3_3','2022_3','1','1','1','6',0,0);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2022_3_4','2022_3','1','1','1','7',1,1);

INSERT INTO TA_ACTE_GESTION_V_COLLECTIF(IDENTIFIANT,ACTE_GESTION,VERSION_COLLECTIF,INCLUS,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('1','2021_2','2021_1_1',1,'christian','MODIFICATION', DATE '2000-1-1','INCLUSION');
INSERT INTO TA_ACTE_GESTION_V_COLLECTIF(IDENTIFIANT,ACTE_GESTION,VERSION_COLLECTIF,INCLUS,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2','2022_2','2022_1_1',1,'christian','MODIFICATION', DATE '2000-1-1','INCLUSION');