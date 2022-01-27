INSERT INTO VMA_EXERCICE(IDENTIFIANT,CODE,LIBELLE,ANNEE) VALUES ('2021','2021','Exercice budgétaire 2021',2021);
INSERT INTO TA_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,EXERCICE,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('1','1','1','2021','christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_VERSION_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,COLLECTIF,NUMERO,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('1','1','1','1',1,'christian','CREATION', DATE '2000-1-1','CREATION');

INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('1','1','1','1','1','1',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2','2','1','1','1','1',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('3','3','1','1','1','1',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('4','4','1','1','1','1',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('5','5','1','1','1','1',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');

INSERT INTO VMA_DEPENSE(IDENTIFIANT,BUDGET_INITIAL_AE,BUDGET_ACTUEL_AE) VALUES ('1',1,2);
INSERT INTO VMA_DEPENSE(IDENTIFIANT,BUDGET_INITIAL_AE,BUDGET_ACTUEL_AE) VALUES ('3',10,17);
INSERT INTO VA_DEPENSE_DISPONIBLE(IDENTIFIANT,AE) VALUES ('1',4);
INSERT INTO VA_DEPENSE_DISPONIBLE(IDENTIFIANT,AE) VALUES ('3',5);

-- Acte de gestion

INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('1','1','1',2021,0,0);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,EXERCICE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('1_1','1',2021,'1','1','1','1',-15,-15);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,EXERCICE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('1_2','1',2021,'2','1','1','1',15,15);

INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('2','2','2',2021,0,0);
INSERT INTO TA_ACTE_GESTION_V_COLLECTIF(IDENTIFIANT,ACTE_GESTION,VERSION_COLLECTIF,INCLUS,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('1','2','1',1,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,EXERCICE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2_1','2',2021,'3','1','1','1',-25,-25);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,EXERCICE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2_2','2',2021,'4','1','1','1',20,20);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,EXERCICE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2_3','2',2021,'5','1','1','1',5,5);

INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('3','3','3',2021,0,0);
INSERT INTO TA_ACTE_GESTION_V_COLLECTIF(IDENTIFIANT,ACTE_GESTION,VERSION_COLLECTIF,INCLUS,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2','3','1',0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,EXERCICE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('3_1','3',2021,'3','1','1','1',100,100);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,EXERCICE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('3_2','3',2021,'5','1','1','1',-100,-100);