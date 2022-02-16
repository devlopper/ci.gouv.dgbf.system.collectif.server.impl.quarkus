INSERT INTO VMA_EXERCICE(IDENTIFIANT,CODE,LIBELLE,ANNEE) VALUES ('2021','2021','Exercice budgétaire 2021',2021);
INSERT INTO TA_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,EXERCICE,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE,EN_COURS) VALUES ('2021_1','2021_1','2021_1','2021','christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT',1);
INSERT INTO TA_VERSION_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,COLLECTIF,NUMERO,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2021_1_1','2021_1_1','2021_1_1','2021_1',1,'christian','CREATION', DATE '2000-1-1','CREATION');
INSERT INTO TA_VERSION_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,COLLECTIF,NUMERO,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2021_1_2','2021_1_2','2021_1_2','2021_1',2,'christian','CREATION', DATE '2000-1-1','CREATION');

INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('1','1','1',2021,0,0);
INSERT INTO VMA_ACTE_GESTION_DEPENSE(IDENTIFIANT,ACTE,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,MONTANT_AE,MONTANT_CP) VALUES ('2021_1_1_1','1','1','1','1','1',0,0);

INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('2','2','1',2021,1,0);
INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('3','3','1',2021,0,1);
INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('4','4','1',2021,2,2);

-- Marquage

INSERT INTO TA_ACTE_GESTION_V_COLLECTIF(IDENTIFIANT,ACTE_GESTION,VERSION_COLLECTIF,INCLUS,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('1','1','2021_1_1',0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_ACTE_GESTION_V_COLLECTIF(IDENTIFIANT,ACTE_GESTION,VERSION_COLLECTIF,INCLUS,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2','2','2021_1_1',1,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_ACTE_GESTION_V_COLLECTIF(IDENTIFIANT,ACTE_GESTION,VERSION_COLLECTIF,INCLUS,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('3','3','2021_1_1',0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');

--		Inclusion

-- Acte de gestion intégré marqué à vrai
INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('include_included_true','include_included_true','include_included_true',2021,0,0);
INSERT INTO TA_ACTE_GESTION_V_COLLECTIF(IDENTIFIANT,ACTE_GESTION,VERSION_COLLECTIF,INCLUS,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('i1','include_included_true','2021_1_1',1,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');

-- Acte de gestion non intégré marqué à faux
INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('include_included_false','include_included_false','include_included_false',2021,0,0);
INSERT INTO TA_ACTE_GESTION_V_COLLECTIF(IDENTIFIANT,ACTE_GESTION,VERSION_COLLECTIF,INCLUS,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('i2','include_included_false','2021_1_1',0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');

-- Acte de gestion non intégré marqué à null
INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('include_included_null','include_included_null','include_included_null',2021,0,0);
INSERT INTO TA_ACTE_GESTION_V_COLLECTIF(IDENTIFIANT,ACTE_GESTION,VERSION_COLLECTIF,INCLUS,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('i3','include_included_null','2021_1_1',0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');

-- Acte de gestion non intégré non marqué
INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('not_marked0','4_0','4_0',2021,0,0);
INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('not_marked1','4_1','4_1',2021,0,0);

--		Exclusion

INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('exclude_included_false','exclude_included_false','exclude_included_false',2021,0,0);
INSERT INTO TA_ACTE_GESTION_V_COLLECTIF(IDENTIFIANT,ACTE_GESTION,VERSION_COLLECTIF,INCLUS,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('e4','exclude_included_false','2021_1_1',0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');

INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('exclude_included_null','exclude_included_null','exclude_included_null',2021,0,0);
INSERT INTO TA_ACTE_GESTION_V_COLLECTIF(IDENTIFIANT,ACTE_GESTION,VERSION_COLLECTIF,INCLUS,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('e5','exclude_included_null','2021_1_1',0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');

INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('exclude_included_true','exclude_included_true','exclude_included_true',2021,0,0);
INSERT INTO TA_ACTE_GESTION_V_COLLECTIF(IDENTIFIANT,ACTE_GESTION,VERSION_COLLECTIF,INCLUS,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('e6','exclude_included_true','2021_1_1',1,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');

