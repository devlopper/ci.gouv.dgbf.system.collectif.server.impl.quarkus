DROP ALIAS IF EXISTS AP_ACTUALIZE_MV;
CREATE ALIAS AP_ACTUALIZE_MV FOR "ci.gouv.dgbf.system.collectif.server.impl.LegislativeActVersionOnCreationAfterTest.PA_ACTUALISER_VM";

INSERT INTO VMA_EXERCICE(IDENTIFIANT,CODE,LIBELLE,ANNEE) VALUES ('2022','2022','Exercice budgétaire 2022',2022);
INSERT INTO TA_COLLECTIF(IDENTIFIANT,CODE,LIBELLE,EXERCICE,AUDIT_IDENTIFIANT,AUDIT_ACTEUR,AUDIT_ACTION,AUDIT_DATE,AUDIT_FONCTIONNALITE,EN_COURS,NUMERO,DATE_) VALUES ('2022_1','2022_1','2022_1','2022','1','christian','CREATION', DATE '2000-1-1','CREATION',1,1, DATE '2000-1-1');
INSERT INTO VA_DEPENSE_IMPORTABLE(IDENTIFIANT,VERSION_COLLECTIF,ACTIVITE_IDENTIFIANT,NATURE_ECONOMIQUE_IDENTIFIANT,SOURCE_FINANCEMENT_IDENTIFIANT,BAILLEUR_IDENTIFIANT) VALUES ('2022_1_1_1','2022_1_1','1','1','1','1');
INSERT INTO VA_DEPENSE_IMPORTABLE(IDENTIFIANT,VERSION_COLLECTIF,ACTIVITE_IDENTIFIANT,NATURE_ECONOMIQUE_IDENTIFIANT,SOURCE_FINANCEMENT_IDENTIFIANT,BAILLEUR_IDENTIFIANT) VALUES ('2022_1_1_2','2022_1_1','2','1','1','1');