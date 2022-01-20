INSERT INTO TA_ACTE(IDENTIFIANT,CODE,LIBELLE,EXERCICE) VALUES ('A001','A001','Acte 2021',2021);
INSERT INTO TA_VERSION_ACTE(IDENTIFIANT,CODE,LIBELLE,ACTE,NUMERO) VALUES ('V001','V001','Version 001','A001',1);
INSERT INTO TA_LIGNE_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_ACTE,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('1','activite02','1','1','1','V001',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_LIGNE_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_ACTE,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2','1','1','1','1','V001',3,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');

INSERT INTO VMA_NATURE_DEPENSE(IDENTIFIANT,CODE,LIBELLE) VALUES ('1','1','Personnel');
INSERT INTO VMA_NATURE_DEPENSE(IDENTIFIANT,CODE,LIBELLE) VALUES ('2','2','Biens e services');
INSERT INTO VMA_NATURE_DEPENSE(IDENTIFIANT,CODE,LIBELLE) VALUES ('3','3','Investissement');
INSERT INTO VMA_NATURE_DEPENSE(IDENTIFIANT,CODE,LIBELLE) VALUES ('4','4','Transfert');

INSERT INTO VMA_SECTION(IDENTIFIANT,CODE,LIBELLE) VALUES ('101','101','Assemblée Nationale');
INSERT INTO VMA_SECTION(IDENTIFIANT,CODE,LIBELLE) VALUES ('327','327','Budget');
INSERT INTO VMA_SECTION(IDENTIFIANT,CODE,LIBELLE) VALUES ('323','323','Intérieur');

INSERT INTO VMA_NATURE_ECONOMIQUE(IDENTIFIANT,CODE,LIBELLE) VALUES ('1','1','Nature Eco 01');

INSERT INTO VMA_USB(IDENTIFIANT,CODE,LIBELLE) VALUES ('22086','22086','Budget');

INSERT INTO VMA_ACTIVITE(IDENTIFIANT,CODE,LIBELLE,ACTION_IDENTIFIANT) VALUES ('activite01','activite01','Activité 01','action01');

INSERT INTO VMA_ACTIVITE(IDENTIFIANT,CODE,LIBELLE,NATURE_DEPENSE_IDENTIFIANT,NATURE_DEPENSE_CODE,NATURE_DEPENSE_LIBELLE,NATURE_DEPENSE_CODE_LIBELLE,SECTION_IDENTIFIANT,SECTION_CODE,SECTION_LIBELLE,SECTION_CODE_LIBELLE,UA_IDENTIFIANT,UA_CODE,UA_LIBELLE,UA_CODE_LIBELLE,USB_IDENTIFIANT,USB_CODE,USB_LIBELLE,USB_CODE_LIBELLE,ACTION_IDENTIFIANT,ACTION_CODE,ACTION_LIBELLE,ACTION_CODE_LIBELLE) 
VALUES ('activite02','activite02','activite 02','2','2','Biens et services','2 Biens et services','S01','S01','Section 01','S01 Section 01','UA01','UA01','UA 01','UA01 UA 01','USB01','USB01','USB 01','USB01 USB 01','A01','A01','Action 01','A01 Action 01');

INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('1','1','1',2021,0,0);
INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('2','2','1',2021,1,0);
INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('3','3','1',2021,0,1);
INSERT INTO VMA_ACTE_GESTION(IDENTIFIANT,CODE,LIBELLE,EXERCICE,MONTANT_AE,MONTANT_CP) VALUES ('4','4','1',2021,2,2);

