INSERT INTO TA_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,EXERCICE,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('LFR1','LFR1','LFR1',2021,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_VERSION_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,ACTE,NUMERO) VALUES ('LFR1V1','LFR1V1','LFR1V1','LFR1',1);

INSERT INTO TA_LIGNE_RECETTE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,VERSION_ACTE,AJUSTEMENT,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('1','A1','NE1','LFR1V1',3,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO VMA_LIGNE_RECETTE(IDENTIFIANT,BUDGET_INITIAL,BUDGET_ACTUEL) VALUES ('1',1,2);
INSERT INTO TA_LIGNE_RECETTE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,VERSION_ACTE,AJUSTEMENT,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2','A2','NE1','LFR1V1',0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_LIGNE_RECETTE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,VERSION_ACTE,AJUSTEMENT,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('3','A3','NE1','LFR1V1',0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_LIGNE_RECETTE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,VERSION_ACTE,AJUSTEMENT,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('4','A4','NE1','LFR1V1',0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');

INSERT INTO VMA_NATURE_ECONOMIQUE(IDENTIFIANT,CODE,LIBELLE) VALUES ('NE1','NE1','NE1');
INSERT INTO VMA_SECTION(IDENTIFIANT,CODE,LIBELLE) VALUES ('S1','S1','S1');
INSERT INTO VMA_USB(IDENTIFIANT,CODE,LIBELLE,SECTION_IDENTIFIANT) VALUES ('USB1','USB1','USB1','S1');
INSERT INTO VMA_ACTIVITE_RECETTE(IDENTIFIANT,CODE,LIBELLE,SECTION_IDENTIFIANT,SECTION_CODE,SECTION_CODE_LIBELLE,USB_IDENTIFIANT,USB_CODE,USB_CODE_LIBELLE) VALUES ('A1','A1','A1','S1','S1','S1','USB1','USB1','USB1');
INSERT INTO VMA_ACTIVITE_RECETTE(IDENTIFIANT,CODE,LIBELLE,SECTION_IDENTIFIANT,SECTION_CODE,SECTION_CODE_LIBELLE,USB_IDENTIFIANT,USB_CODE,USB_CODE_LIBELLE) VALUES ('A2','A2','A2','S1','S1','S1','USB1','USB1','USB1');
INSERT INTO VMA_ACTIVITE_RECETTE(IDENTIFIANT,CODE,LIBELLE,SECTION_IDENTIFIANT,SECTION_CODE,SECTION_CODE_LIBELLE,USB_IDENTIFIANT,USB_CODE,USB_CODE_LIBELLE) VALUES ('A3','A3','A3','S1','S1','S1','USB1','USB1','USB1');