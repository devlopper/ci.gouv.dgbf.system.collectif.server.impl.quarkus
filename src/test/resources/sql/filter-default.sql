INSERT INTO VMA_EXERCICE(IDENTIFIANT,CODE,LIBELLE,ANNEE) VALUES ('2020','2020','Exercice budgétaire 2020',2020);
INSERT INTO VMA_EXERCICE(IDENTIFIANT,CODE,LIBELLE,ANNEE) VALUES ('2021','2021','Exercice budgétaire 2021',2021);
INSERT INTO VMA_NATURE_DEPENSE(IDENTIFIANT,CODE,LIBELLE) VALUES ('1','1','Personnel');
INSERT INTO VMA_NATURE_DEPENSE(IDENTIFIANT,CODE,LIBELLE) VALUES ('2','2','Biens e services');
INSERT INTO VMA_NATURE_DEPENSE(IDENTIFIANT,CODE,LIBELLE) VALUES ('3','3','Investissement');
INSERT INTO VMA_NATURE_DEPENSE(IDENTIFIANT,CODE,LIBELLE) VALUES ('4','4','Transfert');

INSERT INTO VMA_SECTION(IDENTIFIANT,CODE,LIBELLE) VALUES ('S01','S01','Section 01');

-- Legislative Act 1
INSERT INTO TA_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,EXERCICE,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE,EN_COURS) VALUES ('AB01','AB01','Acte budgétaire 1','2020','christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT',1);
-- 		Version 1
INSERT INTO TA_VERSION_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,COLLECTIF,NUMERO,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('VAB01_01','VAB01_01','Version 01','AB01',1,'christian','CREATION', DATE '2000-1-1','CREATION');
--			Expenditures
--				Activity A01
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('1','A01','NE01','SF01','B01','VAB01_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO VMA_DEPENSE(IDENTIFIANT,NATURE_DEPENSE_IDENTIFIANT,USB_IDENTIFIANT) VALUES ('1','P','22086');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('2','A01','NE02','SF01','B01','VAB01_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('3','A01','NE03','SF01','B01','VAB01_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('4','A01','NE04','SF01','B01','VAB01_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
--				Activity A02
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('5','A02','NE01','SF01','B01','VAB01_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('6','A02','NE02','SF01','B01','VAB01_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('7','A02','NE03','SF01','B01','VAB01_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('8','A02','NE04','SF01','B01','VAB01_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
--				Activity A03
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('9','A03','NE01','SF02','B01','VAB01_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('10','A03','NE05','SF01','B01','VAB01_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('11','A03','NE06','SF01','B01','VAB01_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('12','A03','NE04','SF03','B01','VAB01_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
--				Activity A04
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('13','A04','NE01','SF02','B02','VAB01_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('14','A04','NE05','SF01','B01','VAB01_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('15','A04','NE06','SF01','B02','VAB01_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('16','A04','NE04','SF03','B02','VAB01_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
-- 		Version 2
INSERT INTO TA_VERSION_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,COLLECTIF,NUMERO,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('VAB01_02','VAB01_02','Version 02','AB01',1,'christian','CREATION', DATE '2000-1-1','CREATION');
--			Expenditures
--				Activity A01
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('17','A01','NE01','SF01','B01','VAB01_02',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('18','A01','NE02','SF01','B01','VAB01_02',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('19','A01','NE03','SF01','B01','VAB01_02',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('20','A01','NE04','SF01','B01','VAB01_02',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
--				Activity A02
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('21','A02','NE01','SF01','B01','VAB01_02',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('22','A02','NE02','SF01','B01','VAB01_02',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('23','A02','NE03','SF01','B01','VAB01_02',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('24','A02','NE04','SF01','B01','VAB01_02',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
--				Activity A03
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('25','A03','NE01','SF02','B01','VAB01_02',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('26','A03','NE05','SF01','B01','VAB01_02',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('27','A03','NE06','SF01','B01','VAB01_02',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('28','A03','NE04','SF03','B01','VAB01_02',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
--				Activity A04
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('29','A04','NE01','SF02','B02','VAB01_02',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('30','A04','NE05','SF01','B01','VAB01_02',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('31','A04','NE06','SF01','B02','VAB01_02',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('32','A04','NE04','SF03','B02','VAB01_02',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
-- Legislative Act 2
INSERT INTO TA_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,EXERCICE,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE,EN_COURS) VALUES ('AB02','AB02','Acte budgétaire 2','2021','christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT',1);
-- 		Version 1
INSERT INTO TA_VERSION_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,COLLECTIF,NUMERO,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('VAB02_01','VAB02_01','Version 01','AB02',1,'christian','CREATION', DATE '2000-1-1','CREATION');
--			Expenditures
--				Activity A01
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('33','A01','NE01','SF01','B01','VAB02_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('34','A01','NE02','SF01','B01','VAB02_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('35','A01','NE03','SF01','B01','VAB02_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('36','A01','NE04','SF01','B01','VAB02_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
--				Activity A02
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('37','A02','NE01','SF01','B01','VAB02_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('38','A02','NE02','SF01','B01','VAB02_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('39','A02','NE03','SF01','B01','VAB02_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('40','A02','NE04','SF01','B01','VAB02_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
--				Activity A03
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('41','A03','NE01','SF02','B01','VAB02_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('42','A03','NE05','SF01','B01','VAB02_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('43','A03','NE06','SF01','B01','VAB02_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('44','A03','NE04','SF03','B01','VAB02_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
--				Activity A04
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('45','A04','NE01','SF02','B02','VAB02_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('46','A04','NE05','SF01','B01','VAB02_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('47','A04','NE06','SF01','B02','VAB02_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');
INSERT INTO TA_DEPENSE(IDENTIFIANT,ACTIVITE,NATURE_ECONOMIQUE,SOURCE_FINANCEMENT,BAILLEUR,VERSION_COLLECTIF,AJUSTEMENT_AE,AJUSTEMENT_CP,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE) VALUES ('48','A04','NE04','SF03','B02','VAB02_01',0,0,'christian','MODIFICATION', DATE '2000-1-1','AJUSTEMENT');