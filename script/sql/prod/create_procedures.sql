CREATE OR REPLACE PROCEDURE PT_SUPPRIMER_VMS AS
BEGIN
    -- Delete materialized views
    FOR record IN (SELECT mview_name FROM all_mviews WHERE owner='COLLECTIF' AND mview_name LIKE 'VMA_%' ORDER BY mview_name)
    LOOP
        EXECUTE IMMEDIATE 'DROP MATERIALIZED VIEW '||record.mview_name;
    END LOOP;

	-- Delete materialized views tables
	FOR record IN (SELECT table_name FROM all_tables WHERE owner='COLLECTIF' AND table_name LIKE 'VMA_%')
    LOOP
        EXECUTE IMMEDIATE 'DROP TABLE '||record.table_name;
    END LOOP;
    
    EXCEPTION
        WHEN OTHERS THEN
        IF SQLCODE != -942 THEN RAISE;
        END IF;
END;