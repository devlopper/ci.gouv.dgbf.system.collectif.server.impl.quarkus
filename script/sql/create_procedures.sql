CREATE OR REPLACE PROCEDURE AP_LOG_TABLE(p_identifier IN VARCHAR2,p_table_name IN VARCHAR2,p_actor IN VARCHAR2,p_action IN VARCHAR2) AUTHID CURRENT_USER AS
ocount NUMBER;
BEGIN
	SELECT COUNT(*) INTO ocount FROM all_tables WHERE table_name = 'AT_TABLE_LOG';
    IF ocount = 1 THEN
    	EXECUTE IMMEDIATE 'INSERT INTO AT_TABLE_LOG (identifier,table_name,actor,action,start_date) VALUES (:1,:2,:3,:4,SYSDATE)' USING p_identifier,p_table_name,p_actor,p_action;
    END IF;
END;

CREATE OR REPLACE PROCEDURE AP_LOG_TABLE_END_DATE(p_identifier IN VARCHAR2) AUTHID CURRENT_USER AS ocount NUMBER;
BEGIN
	SELECT COUNT(*) INTO ocount FROM all_tables WHERE table_name = 'AT_TABLE_LOG';
    IF ocount = 1 THEN
    	EXECUTE IMMEDIATE 'UPDATE AT_TABLE_LOG SET end_date = SYSDATE WHERE identifier = :1' USING p_identifier;
    END IF;
END;

CREATE OR REPLACE PROCEDURE AP_LOG_TABLE_INPUTS(p_identifier IN VARCHAR2,p_inputs IN VARCHAR2) AUTHID CURRENT_USER AS ocount NUMBER;
BEGIN
	SELECT COUNT(*) INTO ocount FROM all_tables WHERE table_name = 'AT_TABLE_LOG';
    IF ocount = 1 THEN
    	EXECUTE IMMEDIATE 'UPDATE AT_TABLE_LOG SET inputs = '''||p_inputs||''' WHERE identifier = :1' USING p_identifier;
    END IF;
END;

CREATE OR REPLACE PROCEDURE AP_LOG_TABLE_EXCEPTION(p_identifier IN VARCHAR2,p_exception IN VARCHAR2) AUTHID CURRENT_USER AS ocount NUMBER;
BEGIN
	SELECT COUNT(*) INTO ocount FROM all_tables WHERE table_name = 'AT_TABLE_LOG';
    IF ocount = 1 THEN
    	EXECUTE IMMEDIATE 'UPDATE AT_TABLE_LOG SET end_date = SYSDATE , exception = '''||p_exception||''' WHERE identifier = :1' USING p_identifier;
    END IF;
END;

CREATE OR REPLACE PROCEDURE AP_DROP_TABLE(p_table_name IN VARCHAR2) AUTHID CURRENT_USER AS 
ocount NUMBER;
uuid VARCHAR2(32) := SYS_GUID();
BEGIN
    AP_LOG_TABLE(uuid,p_table_name,'SYSTEME','DROP');
	SELECT COUNT(*) INTO ocount FROM all_tables WHERE table_name = p_table_name;
    IF ocount = 1 THEN
        EXECUTE IMMEDIATE  'DROP TABLE '||p_table_name||' CASCADE CONSTRAINTS PURGE';
        UPDATE AT_TABLE_LOG SET outputs = 'Dropped' WHERE identifier = uuid;
    ELSE
        UPDATE AT_TABLE_LOG SET outputs = 'Does not exist' WHERE identifier = uuid;
    END IF;
    AP_LOG_TABLE_END_DATE(uuid);
END;

-------------------------------------- CREATE ----------------------------------------

CREATE OR REPLACE PROCEDURE AP_CREATE_TABLE_FROM_QUERY(p_table_name IN VARCHAR2,query_string IN VARCHAR2) AUTHID CURRENT_USER AS
ocount NUMBER;
uuid VARCHAR2(32) := SYS_GUID();
BEGIN
	AP_LOG_TABLE(uuid,p_table_name,'SYSTEME','CREATE');
	SELECT COUNT(*) INTO ocount FROM all_tables WHERE table_name = p_table_name;
    IF ocount = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE '||p_table_name||' AS '||query_string;
        UPDATE AT_TABLE_LOG SET outputs = 'Created' WHERE identifier = uuid;
    ELSE
        UPDATE AT_TABLE_LOG SET outputs = 'Already exist' WHERE identifier = uuid;
    END IF;
	AP_LOG_TABLE_END_DATE(uuid); 
END;

CREATE OR REPLACE PROCEDURE AP_GET_CREATE_TABLE_QUERY(p_table_name IN VARCHAR2,p_query OUT VARCHAR2) AUTHID CURRENT_USER AS
BEGIN
	SELECT query_select INTO p_query FROM AT_MV WHERE name = p_table_name;
END;

CREATE OR REPLACE PROCEDURE AP_CREATE_TABLE(p_table_name IN VARCHAR2) AUTHID CURRENT_USER AS
query_string VARCHAR2(3500);
BEGIN
	AP_GET_CREATE_TABLE_QUERY(p_table_name,query_string);
	AP_CREATE_TABLE_FROM_QUERY(p_table_name,query_string);
END;

CREATE OR REPLACE PROCEDURE AP_RENAME_TABLE(p_table_name_original IN VARCHAR2,p_table_name_final IN VARCHAR2) AUTHID CURRENT_USER AS
uuid VARCHAR2(32) := SYS_GUID();
BEGIN
	AP_LOG_TABLE(uuid,p_table_name_original,'SYSTEME','RENAME');
    AP_LOG_TABLE_INPUTS(uuid,p_table_name_final);
	EXECUTE IMMEDIATE 'RENAME '||p_table_name_original||' TO '||p_table_name_final;
	AP_LOG_TABLE_END_DATE(uuid);
END;

CREATE OR REPLACE PROCEDURE AP_SCRIPT_TABLE(p_table_name IN VARCHAR2) AUTHID CURRENT_USER AS
uuid VARCHAR2(32) := SYS_GUID();
BEGIN
    AP_LOG_TABLE(uuid,p_table_name,'SYSTEME','SCRIPT');
	FOR t IN (SELECT * FROM AT_MV_SCRIPT WHERE mv = p_table_name AND enabled = 1 ORDER BY order_number)
    LOOP
        EXECUTE IMMEDIATE t.script;
    END LOOP;
    AP_LOG_TABLE_END_DATE(uuid);
    EXCEPTION WHEN others THEN AP_LOG_TABLE_EXCEPTION(uuid,SUBSTR( DBMS_UTILITY.format_error_stack || DBMS_UTILITY.format_error_backtrace, 1, 4000)); 
    COMMIT;
END;

CREATE OR REPLACE PROCEDURE AP_CREATE_MV(p_table_name IN VARCHAR2) AUTHID CURRENT_USER AS
v_temp_table_name VARCHAR2(2048);
query_string VARCHAR2(3500);
BEGIN
    -- We will use a temporary table to avoid application to fail in case there is something going wrong
    v_temp_table_name := p_table_name||'_TEMP';
    AP_DROP_TABLE(v_temp_table_name);
    AP_GET_CREATE_TABLE_QUERY(p_table_name,query_string);
    AP_CREATE_TABLE_FROM_QUERY(v_temp_table_name,query_string);
    
    AP_DROP_TABLE(p_table_name);
    AP_RENAME_TABLE(v_temp_table_name,p_table_name);
   	
    AP_SCRIPT_TABLE(p_table_name);
END;

CREATE OR REPLACE PROCEDURE AP_CREATE_ALL_MV AUTHID CURRENT_USER AS
BEGIN
    FOR t IN (SELECT * FROM AT_MV WHERE enabled = 1 ORDER BY order_number)
    LOOP
        AP_CREATE_MV(t.name);
    END LOOP;
END;

--------------------------------------- MERGE ---------------------------------------------

CREATE OR REPLACE PROCEDURE AP_MERGE_TABLE_FROM_QUERY(p_table_name IN VARCHAR2,query_string IN VARCHAR2) AUTHID CURRENT_USER AS
ocount NUMBER;
uuid VARCHAR2(32) := SYS_GUID();
BEGIN
	AP_LOG_TABLE(uuid,p_table_name,'SYSTEME','MERGE');
	SELECT COUNT(*) INTO ocount FROM all_tables WHERE table_name = p_table_name;
	IF query_string IS NOT NULL THEN
		IF ocount = 1 THEN
	        EXECUTE IMMEDIATE query_string;
	        UPDATE AT_TABLE_LOG SET outputs = 'Merged' WHERE identifier = uuid;
	    ELSE
	        UPDATE AT_TABLE_LOG SET outputs = 'Does not exist.' WHERE identifier = uuid;
	    END IF;
	ELSE
		UPDATE AT_TABLE_LOG SET outputs = 'No query has been defined' WHERE identifier = uuid;
	END IF;
	AP_LOG_TABLE_END_DATE(uuid); 
END;

CREATE OR REPLACE PROCEDURE AP_GET_MERGE_TABLE_QUERY(p_table_name IN VARCHAR2,p_query OUT VARCHAR2) AUTHID CURRENT_USER AS
BEGIN
	SELECT query_merge INTO p_query FROM AT_MV WHERE name = p_table_name;
END;

CREATE OR REPLACE PROCEDURE AP_MERGE_TABLE(p_table_name IN VARCHAR2) AUTHID CURRENT_USER AS
query_string VARCHAR2(3500);
BEGIN
	AP_GET_MERGE_TABLE_QUERY(p_table_name,query_string);
	AP_MERGE_TABLE_FROM_QUERY(p_table_name,query_string);
END;

CREATE OR REPLACE PROCEDURE AP_MERGE_ALL_MV AUTHID CURRENT_USER AS
BEGIN
    FOR t IN (SELECT * FROM AT_MV WHERE enabled = 1 ORDER BY order_number)
    LOOP
        AP_MERGE_TABLE(t.name);
    END LOOP;
END;
   
CREATE OR REPLACE PROCEDURE PA_ACTUALISER_VM(nom_table IN VARCHAR2) AUTHID CURRENT_USER AS uuid VARCHAR2(32);
BEGIN
	uuid := SYS_GUID();
    INSERT INTO TT_LOG_SYNCHRONISATION_TA (identifiant,nom_table,date_debut) VALUES (uuid,nom_table,SYSDATE);
    DBMS_MVIEW.REFRESH(nom_table, METHOD => 'C', ATOMIC_REFRESH => FALSE,PARALLELISM => 4);
    UPDATE TT_LOG_SYNCHRONISATION_TA SET date_fin = SYSDATE WHERE identifiant = uuid;
END;