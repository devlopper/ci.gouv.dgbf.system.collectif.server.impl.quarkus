-- Liste des dépenses importables
CREATE OR REPLACE VIEW VA_LIGNE_DEPENSE_IMPORTABLE AS
SELECT l.*
FROM vma_ligne_depense l
WHERE l.identifiant NOT IN (SELECT a.identifiant FROM ta_ligne_depense a) AND l.exercice = (SELECT MAX(a.exercice) FROM vma_ligne_depense a);

-- Liste des recettes importables
CREATE OR REPLACE VIEW VA_LIGNE_RECETTE_IMPORTABLE AS
SELECT l.*
FROM vma_ligne_recette l
WHERE l.identifiant NOT IN (SELECT a.identifiant FROM ta_ligne_recette a) AND l.exercice = (SELECT MAX(a.exercice) FROM vma_ligne_recette a) AND l.activite_identifiant IS NOT NULL AND l.nature_economique_identifiant IS NOT NULL;