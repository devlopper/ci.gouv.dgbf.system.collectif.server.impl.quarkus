DROP ALIAS IF EXISTS PA_ACTUALISER_VM;
CREATE ALIAS PA_ACTUALISER_VM FOR "ci.gouv.dgbf.system.collectif.server.impl.LegislativeActTest.PA_ACTUALISER_VM";

INSERT INTO VMA_EXERCICE(IDENTIFIANT,CODE,LIBELLE,ANNEE) VALUES ('2020','2020','Exercice budgétaire 2020',2020);
INSERT INTO TA_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,EXERCICE,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE,EN_COURS,NUMERO) VALUES ('2020_1','2020_1','2020_1','2020','christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT',0,1);
INSERT INTO TA_VERSION_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,COLLECTIF,NUMERO,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2020_1_1','2020_1_1','1','2020_1',1,'christian','CREATION', DATE '2000-1-1','CREATION');

INSERT INTO VMA_EXERCICE(IDENTIFIANT,CODE,LIBELLE,ANNEE) VALUES ('2021','2021','Exercice budgétaire 2021',2021);
INSERT INTO TA_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,EXERCICE,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE,EN_COURS,AJUSTEMENT_AE_ATTENDU,AJUSTEMENT_CP_ATTENDU,NUMERO) VALUES ('2021_1','2021_1','2021_1','2021','christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT',1,10,20,1);
INSERT INTO TA_VERSION_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,COLLECTIF,NUMERO,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2021_1_1','2021_1_1','2021_1_1','2021_1',1,'christian','CREATION', DATE '2000-1-1','CREATION');
UPDATE TA_COLLECTIF SET VERSION_PAR_DEFAUT = '2021_1_1' WHERE IDENTIFIANT = '2021_1';
INSERT INTO TA_VERSION_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,COLLECTIF,NUMERO,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2021_1_2','2021_1_2','2021_1_2','2021_1',2,'christian','CREATION', DATE '2000-1-1','CREATION');

INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2021_1_1_1','1','1','1','1','2021_1_1',0,7,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2021_1_1_2','2','1','1','1','2021_1_1',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2021_1_1_3','3','1','1','1','2021_1_1',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2021_1_1_4','4','1','1','1','2021_1_1',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2021_1_1_5','5','1','1','1','2021_1_1',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');

INSERT INTO VMA_DEPENSE(IDENTIFIANT,BUDGET_INITIAL_AE,BUDGET_INITIAL_CP,BUDGET_ACTUEL_AE,BUDGET_ACTUEL_CP) VALUES ('2021_1_1_1',1,3,2,5);
INSERT INTO VMA_DEPENSE(IDENTIFIANT,BUDGET_INITIAL_AE,BUDGET_ACTUEL_AE) VALUES ('2021_1_1_3',10,17);
INSERT INTO VA_DEPENSE_DISPONIBLE(IDENTIFIANT,AE,CP) VALUES ('2021_1_1_1',4,5);
INSERT INTO VA_DEPENSE_DISPONIBLE(IDENTIFIANT,AE) VALUES ('2021_1_1_3',5);

INSERT INTO VMA_EXERCICE(IDENTIFIANT,CODE,LIBELLE,ANNEE) VALUES ('2022','2022','Exercice budgétaire 2022',2022);
INSERT INTO TA_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,EXERCICE,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE,EN_COURS,NUMERO) VALUES ('2022_1','2022_1','2022_1','2022','christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT',1,1);
INSERT INTO TA_VERSION_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,COLLECTIF,NUMERO,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2022_1_1','2022_1_1','2022_1_1','2022_1',1,'christian','CREATION', DATE '2000-1-1','CREATION');
UPDATE TA_COLLECTIF SET VERSION_PAR_DEFAUT = '2022_1_1' WHERE IDENTIFIANT = '2022_1';

INSERT INTO VMA_EXERCICE(IDENTIFIANT,CODE,LIBELLE,ANNEE) VALUES ('2023','2023','Exercice budgétaire 2023',2023);

-- Acte de gestion

INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('1','1','1',2021,0,0);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,EXERCICE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('1_1','1',2021,'1','1','1','1',-15,-15);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,EXERCICE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('1_2','1',2021,'2','1','1','1',15,15);

INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('2','2','2',2021,0,0);
INSERT INTO TA_ACTE_GESTION_V_COLLECTIF(IDENTIFIANT,ACTE_GESTION,VERSION_COLLECTIF,INCLUS,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('1','2','2021_1_1',1,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,EXERCICE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2_1','2',2021,'3','1','1','1',-25,-25);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,EXERCICE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2_2','2',2021,'4','1','1','1',20,20);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,EXERCICE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2_3','2',2021,'5','1','1','1',5,5);

INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('3','3','3',2021,0,0);
INSERT INTO TA_ACTE_GESTION_V_COLLECTIF(IDENTIFIANT,ACTE_GESTION,VERSION_COLLECTIF,INCLUS,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2','3','2021_1_1',0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,EXERCICE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('3_1','3',2021,'3','1','1','1',90,90);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,EXERCICE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('3_2','3',2021,'5','1','1','1',-90,-90);