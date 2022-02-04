-- Liste des dépenses importées
CREATE OR REPLACE VIEW VA_DEPENSE_IMPORTEE AS
SELECT depense.*
FROM VMA_DEPENSE depense
JOIN TA_DEPENSE tdepense ON tdepense.identifiant = depense.identifiant;

-- Liste des dépenses importables
CREATE OR REPLACE VIEW VA_DEPENSE_IMPORTABLE AS
SELECT depense.*
FROM VMA_DEPENSE depense
WHERE depense.identifiant NOT IN (SELECT depense_importee.identifiant FROM VA_DEPENSE_IMPORTEE depense_importee);

-------------------------------------------------------------------------------------------------------------------------------------------------------------

-- Liste des recettes importables
CREATE OR REPLACE VIEW VA_RECETTE_IMPORTABLE AS
SELECT l.*
FROM VMA_RECETTE l
WHERE l.identifiant NOT IN (SELECT a.identifiant FROM ta_recette a) AND l.activite_identifiant IS NOT NULL AND l.nature_economique_identifiant IS NOT NULL;