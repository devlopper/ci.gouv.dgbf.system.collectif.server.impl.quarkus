INSERT INTO VMA_EXERCICE(IDENTIFIANT,CODE,LIBELLE,ANNEE) VALUES ('2021','2021','Exercice budgétaire 2021',2021);
INSERT INTO VMA_SECTION(IDENTIFIANT,CODE,LIBELLE) VALUES ('101','101','Assemblée nationale');
INSERT INTO VMA_SECTION(IDENTIFIANT,CODE,LIBELLE) VALUES ('323','323','Intérieur');
INSERT INTO VMA_SECTION(IDENTIFIANT,CODE,LIBELLE) VALUES ('327','327','Budget');

INSERT INTO VMA_USB(IDENTIFIANT,CODE,LIBELLE) VALUES ('22086','22086','Budget');

INSERT INTO VMA_NATURE_ECONOMIQUE(IDENTIFIANT,CODE,LIBELLE) VALUES ('1','1','Nature Eco 01');

INSERT INTO TA_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,EXERCICE,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE,EN_COURS) VALUES ('1','1','1','2021','christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT',1);
INSERT INTO TA_VERSION_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,COLLECTIF,NUMERO,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('1','1','1','1',1,'christian','CREATION', DATE '2000-1-1','CREATION');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('1','activity01','1','1','1','1',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2','1','2','1','1','1',3,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');

INSERT INTO VMA_ACTIVITE(IDENTIFIANT,CODE,LIBELLE,NATURE_DEPENSE_IDENTIFIANT,NATURE_DEPENSE_CODE,NATURE_DEPENSE_LIBELLE,NATURE_DEPENSE_CODE_LIBELLE,SECTION_IDENTIFIANT,SECTION_CODE,SECTION_LIBELLE,SECTION_CODE_LIBELLE,UA_IDENTIFIANT,UA_CODE,UA_LIBELLE,UA_CODE_LIBELLE,USB_IDENTIFIANT,USB_CODE,USB_LIBELLE,USB_CODE_LIBELLE,ACTION_IDENTIFIANT,ACTION_CODE,ACTION_LIBELLE,ACTION_CODE_LIBELLE) 
VALUES ('activity01','activity01','activity 01','2','2','Biens et services','2 Biens et services','S01','S01','Section 01','S01 Section 01','UA01','UA01','UA 01','UA01 UA 01','USB01','USB01','USB 01','USB01 USB 01','A01','A01','Action 01','A01 Action 01');