--Liste des dépense
CREATE OR REPLACE VIEW VA_DEPENSE AS
SELECT depense.*,exercice.annee AS "EXERCICE"
FROM TA_DEPENSE depense
JOIN TA_VERSION_COLLECTIF version_collectif ON version_collectif.identifiant = depense.version_collectif
JOIN TA_COLLECTIF collectif ON collectif.identifiant = version_collectif.collectif
JOIN VMA_EXERCICE exercice ON exercice.identifiant = collectif.exercice;

-- Liste des dépenses importables
CREATE OR REPLACE VIEW VA_DEPENSE_IMPORTABLE AS
SELECT l.*
FROM VMA_DEPENSE l
WHERE l.identifiant NOT IN (SELECT a.identifiant FROM ta_depense a);

-- Liste des recettes importables
CREATE OR REPLACE VIEW VA_RECETTE_IMPORTABLE AS
SELECT l.*
FROM VMA_RECETTE l
WHERE l.identifiant NOT IN (SELECT a.identifiant FROM ta_recette a) AND l.activite_identifiant IS NOT NULL AND l.nature_economique_identifiant IS NOT NULL;