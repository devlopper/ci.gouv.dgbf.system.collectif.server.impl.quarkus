INSERT INTO TA_ACTE(IDENTIFIANT,CODE,LIBELLE,EXERCICE) VALUES ('1','1','1',2021);
INSERT INTO TA_VERSION_ACTE(IDENTIFIANT,CODE,LIBELLE,ACTE,NUMERO,DATE_CREATION) VALUES ('1','1','1','1',1, DATE '2021-01-01');
INSERT INTO TA_VERSION_ACTE(IDENTIFIANT,CODE,LIBELLE,ACTE,NUMERO,DATE_CREATION) VALUES ('2','2','2','1',2, DATE '2021-01-01');

--		Inclusion

-- Acte de gestion intégré marqué à vrai
INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('include_included_true','1','1',2021,0,0);
INSERT INTO TA_ACTE_GESTION_VERSION_ACTE(IDENTIFIANT,ACTE_GESTION,VERSION_ACTE,INCLUS,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('1','include_included_true','1',1,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');

-- Acte de gestion non intégré marqué à faux
INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('include_included_false','2','2',2021,0,0);
INSERT INTO TA_ACTE_GESTION_VERSION_ACTE(IDENTIFIANT,ACTE_GESTION,VERSION_ACTE,INCLUS,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2','include_included_false','1',0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');

-- Acte de gestion non intégré marqué à null
INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('include_included_null','3','3',2021,0,0);
INSERT INTO TA_ACTE_GESTION_VERSION_ACTE(IDENTIFIANT,ACTE_GESTION,VERSION_ACTE,INCLUS,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('3','include_included_null','1',0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');

-- Acte de gestion non intégré non marqué
INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('not_marked0','4_0','4_0',2021,0,0);
INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('not_marked1','4_1','4_1',2021,0,0);

--		Exclusion

INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('exclude_included_false','5','5',2021,0,0);
INSERT INTO TA_ACTE_GESTION_VERSION_ACTE(IDENTIFIANT,ACTE_GESTION,VERSION_ACTE,INCLUS,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('4','exclude_included_false','1',0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');

INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('exclude_included_null','6','6',2021,0,0);
INSERT INTO TA_ACTE_GESTION_VERSION_ACTE(IDENTIFIANT,ACTE_GESTION,VERSION_ACTE,INCLUS,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('5','exclude_included_null','1',0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');

INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('exclude_included_true','7','7',2021,0,0);
INSERT INTO TA_ACTE_GESTION_VERSION_ACTE(IDENTIFIANT,ACTE_GESTION,VERSION_ACTE,INCLUS,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('6','exclude_included_true','1',1,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');