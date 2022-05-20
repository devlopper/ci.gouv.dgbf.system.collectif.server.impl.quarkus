CREATE OR REPLACE VIEW VT_DEPENSE_AJUSTEE AS
SELECT v.*,d.ajustement_ae,d.ajustement_cp
FROM TA_DEPENSE d
LEFT JOIN VMA_DEPENSE v ON v.identifiant = d.identifiant
WHERE d.ajustement_ae <> 0 or d.ajustement_cp <> 0;