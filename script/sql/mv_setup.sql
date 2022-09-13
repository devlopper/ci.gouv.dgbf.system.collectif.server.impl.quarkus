---------------------------------------------------------------------------------------------------------------------------------------------------------
-- TABLES
---------------------------------------------------------------------------------------------------------------------------------------------------------
-- Create
CREATE TABLE "AT_MV" 
(	"NAME" VARCHAR2(30 BYTE) NOT NULL ENABLE, 
	"QUERY" VARCHAR2(4000 BYTE) NOT NULL ENABLE,
	"DELETABLE_COLUMN" VARCHAR2(30 BYTE) NOT NULL ENABLE,
	"DELETABLE_MARKER" VARCHAR2(10 BYTE) NOT NULL ENABLE,
	"ORDER_NUMBER" NUMBER(2,0), 
	"ENABLED" NUMBER(1,0) NOT NULL ENABLE, 
	CONSTRAINT "AT_MV_PK" PRIMARY KEY ("NAME") ENABLE
);

CREATE TABLE "AT_MV_SCRIPT" 
(	"MV" VARCHAR2(30 BYTE) NOT NULL ENABLE, 
	"SCRIPT" VARCHAR2(2048 BYTE) NOT NULL ENABLE, 
	"ORDER_NUMBER" NUMBER(2,0),
	"ENABLED" NUMBER(1,0), 
	 CONSTRAINT "AT_MV_SCRIPT_FK1" FOREIGN KEY ("MV") REFERENCES "AT_MV" ("NAME") ENABLE
);

CREATE TABLE "AT_TABLE_LOG" 
(	"IDENTIFIER" VARCHAR2(32 BYTE) NOT NULL ENABLE, 
	"TABLE_NAME" VARCHAR2(30 BYTE) NOT NULL ENABLE,
	"ACTOR" VARCHAR2(1024 BYTE) NOT NULL ENABLE,  
	"ACTION" VARCHAR2(10 BYTE) NOT NULL ENABLE, 
	"INPUTS" VARCHAR2(1024 BYTE), 
	"START_DATE" DATE NOT NULL ENABLE, 
	"END_DATE" DATE, 
	"OUTPUTS" VARCHAR2(1024 BYTE), 
	"EXCEPTION" VARCHAR2(1024 BYTE), 
	 CONSTRAINT "AT_TABLE_LOG_PK" PRIMARY KEY ("IDENTIFIER") ENABLE
);

---------------------------------------------------------------------------------------------------------------------------------------------------------
-- VIEWS
---------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE OR REPLACE VIEW AV_MV_QUERY_MERGE_TOKENS AS
SELECT mv.name AS "NAME"
,'SELECT '||(LISTAGG('s.'||atc.column_name, ',') WITHIN GROUP (ORDER BY atc.column_name))||',s.'||mv.deletable_column||',s.identifiant FROM ('||query||') s UNION ALL SELECT '
||(LISTAGG('d.'||atc.column_name, ',') WITHIN GROUP (ORDER BY atc.column_name))||','''||mv.deletable_marker||''' AS '||mv.deletable_column||',d.identifiant FROM '||mv.name||' d ' AS "SELECT_"
,'LEFT JOIN ('||mv.query||') s ON s.identifiant = d.identifiant WHERE s.identifiant IS NULL' AS "JOIN_"
,'INSERT('||(LISTAGG(atc.column_name, ',') WITHIN GROUP (ORDER BY atc.column_name))||','||mv.deletable_column||',identifiant) VALUES ('
||(LISTAGG('s.'||atc.column_name, ',') WITHIN GROUP (ORDER BY atc.column_name))||',s.'||mv.deletable_column||',s.identifiant)'AS "INSERT_"
,'UPDATE SET '||((LISTAGG('d.'||atc.column_name||'=s.'||atc.column_name, ',') WITHIN GROUP (ORDER BY atc.column_name)))||',d.'||mv.deletable_column||'=s.'||mv.deletable_column AS "UPDATE_"
,'DELETE WHERE d.'||mv.deletable_column||' = '''||mv.deletable_marker||'''' AS "DELETE_"
FROM at_mv mv
JOIN all_tab_columns atc ON atc.table_name = mv.name
WHERE owner = USER AND LOWER(atc.column_name) <> LOWER(mv.deletable_column) AND LOWER(atc.column_name) <> 'identifiant'
GROUP BY atc.owner,mv.name,mv.query,mv.deletable_column,mv.deletable_marker;

CREATE OR REPLACE VIEW AV_MV_QUERY_MERGE AS
SELECT name,CONCAT(CONCAT('MERGE INTO '||name||' d USING (',TO_CLOB(select_)),CONCAT(join_,CONCAT(') s ON (s.identifiant = d.identifiant) WHEN NOT MATCHED THEN '
,CONCAT(TO_CLOB(insert_),CONCAT(' WHEN MATCHED THEN ',CONCAT(TO_CLOB(update_),CONCAT(' ',TO_CLOB(delete_)))))))) AS "QUERY"
FROM AV_MV_QUERY_MERGE_TOKENS t;


---------------------------------------------------------------------------------------------------------------------------------------------------------
-- PROCEDURES
---------------------------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------- LOG ----------------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE AP_LOG_TABLE(p_identifier IN VARCHAR2,p_table_name IN VARCHAR2,p_actor IN VARCHAR2,p_action IN VARCHAR2) AUTHID CURRENT_USER AS
ocount NUMBER;
BEGIN
	SELECT COUNT(*) INTO ocount FROM all_tables WHERE owner = USER AND table_name = 'AT_TABLE_LOG';
    IF ocount = 1 THEN
    	EXECUTE IMMEDIATE 'INSERT INTO AT_TABLE_LOG (identifier,table_name,actor,action,start_date) VALUES (:1,:2,:3,:4,SYSDATE)' USING p_identifier,p_table_name,p_actor,p_action;
    	COMMIT;
    END IF;
END;
/

CREATE OR REPLACE PROCEDURE AP_LOG_TABLE_END_DATE(p_identifier IN VARCHAR2) AUTHID CURRENT_USER AS ocount NUMBER;
BEGIN
	SELECT COUNT(*) INTO ocount FROM all_tables WHERE owner = USER AND  table_name = 'AT_TABLE_LOG';
    IF ocount = 1 THEN
    	EXECUTE IMMEDIATE 'UPDATE AT_TABLE_LOG SET end_date = SYSDATE WHERE identifier = :1' USING p_identifier;
    	COMMIT;
    END IF;
END;
/

CREATE OR REPLACE PROCEDURE AP_LOG_TABLE_INPUTS(p_identifier IN VARCHAR2,p_inputs IN VARCHAR2) AUTHID CURRENT_USER AS ocount NUMBER;
BEGIN
	SELECT COUNT(*) INTO ocount FROM all_tables WHERE owner = USER AND  table_name = 'AT_TABLE_LOG';
    IF ocount = 1 THEN
    	EXECUTE IMMEDIATE 'UPDATE AT_TABLE_LOG SET inputs = '''||p_inputs||''' WHERE identifier = :1' USING p_identifier;
    	COMMIT;
    END IF;
END;
/

CREATE OR REPLACE PROCEDURE AP_LOG_TABLE_OUTPUTS(p_identifier IN VARCHAR2,p_outputs IN VARCHAR2) AUTHID CURRENT_USER AS ocount NUMBER;
BEGIN
	SELECT COUNT(*) INTO ocount FROM all_tables WHERE owner = USER AND  table_name = 'AT_TABLE_LOG';
    IF ocount = 1 THEN
    	EXECUTE IMMEDIATE 'UPDATE AT_TABLE_LOG SET outputs = '''||p_outputs||''' WHERE identifier = :1' USING p_identifier;
    	COMMIT;
    END IF;
END;
/

CREATE OR REPLACE PROCEDURE AP_LOG_TABLE_EXCEPTION(p_identifier IN VARCHAR2,p_exception IN VARCHAR2) AUTHID CURRENT_USER AS ocount NUMBER;
BEGIN
	SELECT COUNT(*) INTO ocount FROM all_tables WHERE owner = USER AND  table_name = 'AT_TABLE_LOG';
    IF ocount = 1 THEN
    	EXECUTE IMMEDIATE 'UPDATE AT_TABLE_LOG SET end_date = SYSDATE , exception = '''||p_exception||''' WHERE identifier = :1' USING p_identifier;
    	COMMIT;
    END IF;
END;
/

------------------------------------------------ DROP -------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE AP_DROP_TABLE(p_table_name IN VARCHAR2) AUTHID CURRENT_USER AS 
ocount NUMBER;
uuid VARCHAR2(32) := SYS_GUID();
BEGIN
    AP_LOG_TABLE(uuid,p_table_name,'SYSTEME','DROP');
	SELECT COUNT(*) INTO ocount FROM all_tables WHERE owner = USER AND  table_name = p_table_name;
    IF ocount = 1 THEN
        EXECUTE IMMEDIATE  'DROP TABLE '||p_table_name||' CASCADE CONSTRAINTS PURGE';
        AP_LOG_TABLE_OUTPUTS(uuid,'Dropped');
    ELSE
    	AP_LOG_TABLE_OUTPUTS(uuid,'Does not exist');
    END IF;
    AP_LOG_TABLE_END_DATE(uuid);
END;
/

CREATE OR REPLACE PROCEDURE AP_DELETE_MV(p_table_name IN VARCHAR2) AUTHID CURRENT_USER AS 
BEGIN
    AP_DROP_TABLE(p_table_name);
END;
/

CREATE OR REPLACE PROCEDURE AP_DROP_ALL_MV AUTHID CURRENT_USER AS 
v_table_name VARCHAR2(30) := 'AT_MV';
ocount NUMBER;
uuid VARCHAR2(32) := SYS_GUID();
BEGIN
    AP_LOG_TABLE(uuid,v_table_name,'SYSTEME','DROPALL');
	SELECT COUNT(*) INTO ocount FROM all_tables WHERE owner = USER AND table_name = v_table_name;
    IF ocount = 1 THEN
        FOR t IN (SELECT name FROM AT_MV)
    	LOOP
    	    AP_DROP_TABLE(t.name);
    	END LOOP;
    	AP_LOG_TABLE_OUTPUTS(uuid,'All dropped');
    ELSE
        AP_LOG_TABLE_OUTPUTS(uuid,'Does not exist');
    END IF;
    AP_LOG_TABLE_END_DATE(uuid);
END;
/

-------------------------------------- CREATE ----------------------------------------

CREATE OR REPLACE PROCEDURE AP_CREATE_TABLE_FROM_QUERY(p_table_name IN VARCHAR2,query_string IN VARCHAR2) AUTHID CURRENT_USER AS
ocount NUMBER;
uuid VARCHAR2(32) := SYS_GUID();
BEGIN
	AP_LOG_TABLE(uuid,p_table_name,'SYSTEME','CREATE');
	SELECT COUNT(*) INTO ocount FROM all_tables WHERE owner = USER AND table_name = p_table_name;
    IF ocount = 0 THEN
        EXECUTE IMMEDIATE 'CREATE TABLE '||p_table_name||' AS '||query_string;
        AP_LOG_TABLE_OUTPUTS(uuid,'Created');
    ELSE
        AP_LOG_TABLE_OUTPUTS(uuid,'Already exist');
    END IF;
	AP_LOG_TABLE_END_DATE(uuid); 
END;
/

CREATE OR REPLACE PROCEDURE AP_GET_CREATE_TABLE_QUERY(p_table_name IN VARCHAR2,p_query OUT VARCHAR2) AUTHID CURRENT_USER AS
BEGIN
	SELECT query INTO p_query FROM AT_MV WHERE name = p_table_name;
END;
/

CREATE OR REPLACE PROCEDURE AP_CREATE_TABLE(p_table_name IN VARCHAR2) AUTHID CURRENT_USER AS
query_string VARCHAR2(3500);
BEGIN
	AP_GET_CREATE_TABLE_QUERY(p_table_name,query_string);
	AP_CREATE_TABLE_FROM_QUERY(p_table_name,query_string);
END;
/

CREATE OR REPLACE PROCEDURE AP_RENAME_TABLE(p_table_name_original IN VARCHAR2,p_table_name_final IN VARCHAR2) AUTHID CURRENT_USER AS
uuid VARCHAR2(32) := SYS_GUID();
BEGIN
	AP_LOG_TABLE(uuid,p_table_name_original,'SYSTEME','RENAME');
    AP_LOG_TABLE_INPUTS(uuid,p_table_name_final);
	EXECUTE IMMEDIATE 'RENAME '||p_table_name_original||' TO '||p_table_name_final;
	AP_LOG_TABLE_OUTPUTS(uuid,'Renamed');
	AP_LOG_TABLE_END_DATE(uuid);
END;
/

CREATE OR REPLACE PROCEDURE AP_SCRIPT_TABLE(p_table_name IN VARCHAR2) AUTHID CURRENT_USER AS
uuid VARCHAR2(32) := SYS_GUID();
BEGIN
    AP_LOG_TABLE(uuid,p_table_name,'SYSTEME','SCRIPT');
	FOR t IN (SELECT * FROM AT_MV_SCRIPT WHERE mv = p_table_name AND enabled = 1 ORDER BY order_number)
    LOOP
        EXECUTE IMMEDIATE t.script;
    END LOOP;
    AP_LOG_TABLE_OUTPUTS(uuid,'Scripted');
    AP_LOG_TABLE_END_DATE(uuid);
    EXCEPTION WHEN others THEN AP_LOG_TABLE_EXCEPTION(uuid,SUBSTR( DBMS_UTILITY.format_error_stack || DBMS_UTILITY.format_error_backtrace, 1, 4000)); 
END;
/

CREATE OR REPLACE PROCEDURE AP_CREATE_MV(p_table_name IN VARCHAR2) AUTHID CURRENT_USER AS
v_temp_table_name VARCHAR2(2048);
query_string VARCHAR2(3500);
BEGIN
    -- We will use a temporary table to avoid application to fail in case there is something going wrong
    v_temp_table_name := p_table_name||'_T';
    AP_DROP_TABLE(v_temp_table_name);
    AP_GET_CREATE_TABLE_QUERY(p_table_name,query_string);
    AP_CREATE_TABLE_FROM_QUERY(v_temp_table_name,query_string);
    
    AP_DROP_TABLE(p_table_name);
    AP_RENAME_TABLE(v_temp_table_name,p_table_name);
   	
    AP_SCRIPT_TABLE(p_table_name);
END;
/

CREATE OR REPLACE PROCEDURE AP_CREATE_ALL_MV AUTHID CURRENT_USER AS
v_table_name VARCHAR2(30) := 'AT_MV';
ocount NUMBER;
uuid VARCHAR2(32) := SYS_GUID();
BEGIN
    AP_LOG_TABLE(uuid,v_table_name,'SYSTEME','CREATEALL');
	SELECT COUNT(*) INTO ocount FROM all_tables WHERE owner = USER AND table_name = v_table_name;
    IF ocount = 1 THEN
        FOR t IN (SELECT name FROM AT_MV WHERE enabled = 1 ORDER BY order_number)
    	LOOP
    	    AP_CREATE_MV(t.name);
    	END LOOP;
    	AP_LOG_TABLE_OUTPUTS(uuid,'All created');
    ELSE
        AP_LOG_TABLE_OUTPUTS(uuid,'Does not exist');
    END IF;
    AP_LOG_TABLE_END_DATE(uuid);
END;
/

--------------------------------------- MERGE ---------------------------------------------

CREATE OR REPLACE PROCEDURE AP_MERGE_TABLE_FROM_QUERY(p_table_name IN VARCHAR2,query_string IN CLOB) AUTHID CURRENT_USER AS
ocount NUMBER;
uuid VARCHAR2(32) := SYS_GUID();
BEGIN
	AP_LOG_TABLE(uuid,p_table_name,'SYSTEME','MERGE');
	SELECT COUNT(*) INTO ocount FROM all_tables WHERE owner = USER AND table_name = p_table_name;
	IF query_string IS NOT NULL THEN
		IF ocount = 1 THEN
	        EXECUTE IMMEDIATE query_string;
	        COMMIT;
	        AP_LOG_TABLE_OUTPUTS(uuid,'Merged');
	    ELSE
	        AP_LOG_TABLE_OUTPUTS(uuid,'Does not exist');
	    END IF;
	ELSE
		AP_LOG_TABLE_OUTPUTS(uuid,'No query has been defined');
	END IF;
	AP_LOG_TABLE_END_DATE(uuid);
	EXCEPTION WHEN others THEN ROLLBACK; 
END;
/

CREATE OR REPLACE PROCEDURE AP_GET_MERGE_TABLE_QUERY(p_table_name IN VARCHAR2,p_query OUT CLOB) AUTHID CURRENT_USER AS
BEGIN
	SELECT query INTO p_query FROM AV_MV_QUERY_MERGE t WHERE name = p_table_name;
END;
/

CREATE OR REPLACE PROCEDURE AP_GET_MERGE_MV_QUERY(p_table_name IN VARCHAR2,p_query OUT CLOB) AUTHID CURRENT_USER AS
BEGIN
	SELECT query INTO p_query FROM AV_MV_QUERY_MERGE t WHERE name = p_table_name;
END;
/

CREATE OR REPLACE PROCEDURE AP_MERGE_MV(p_table_name IN VARCHAR2) AUTHID CURRENT_USER AS
query_string CLOB;
BEGIN
	AP_GET_MERGE_MV_QUERY(p_table_name,query_string);
	AP_MERGE_TABLE_FROM_QUERY(p_table_name,query_string);
END;
/

CREATE OR REPLACE PROCEDURE AP_UPDATE_MV(p_table_name IN VARCHAR2) AUTHID CURRENT_USER AS
BEGIN
	AP_MERGE_MV(p_table_name);
END;
/

CREATE OR REPLACE PROCEDURE AP_MERGE_ALL_MV AUTHID CURRENT_USER AS
ocount NUMBER;
v_table_name VARCHAR2(30) := 'AT_MV';
uuid VARCHAR2(32) := SYS_GUID();
BEGIN
	AP_LOG_TABLE(uuid,v_table_name,'SYSTEME','MERGEALL');
	SELECT COUNT(*) INTO ocount FROM all_tables WHERE owner = USER AND table_name = v_table_name;
    IF ocount = 1 THEN
        FOR t IN (SELECT name FROM AT_MV WHERE enabled = 1 ORDER BY order_number)
    	LOOP
    	    AP_MERGE_MV(t.name);
    	END LOOP;
    	AP_LOG_TABLE_OUTPUTS(uuid,'All merged');
    ELSE
        AP_LOG_TABLE_OUTPUTS(uuid,'Does not exist');
    END IF;
    AP_LOG_TABLE_END_DATE(uuid);
END;
/

CREATE OR REPLACE PROCEDURE AP_ACTUALIZE_MV(p_table_name IN VARCHAR2) AUTHID CURRENT_USER AS
ocount NUMBER;
v_enabled NUMBER;
uuid VARCHAR2(32) := SYS_GUID();
BEGIN
    --AP_LOG_TABLE(uuid,p_table_name,'SYSTEME','ACTUALIZE');
    SELECT enabled INTO v_enabled FROM AT_MV WHERE name = p_table_name;
	SELECT COUNT(*) INTO ocount FROM all_tables WHERE owner = USER AND table_name = p_table_name;
    IF ocount = 1 AND v_enabled = 1 THEN
        AP_MERGE_MV(p_table_name);
    ELSIF v_enabled = 1 THEN
        AP_CREATE_MV(p_table_name);
    ELSE
        AP_LOG_TABLE(uuid,p_table_name,'SYSTEME','ACTUALIZATION HAS BEEN DISABLED');
    END IF;
    --AP_LOG_TABLE_END_DATE(uuid);
END;
/

CREATE OR REPLACE PROCEDURE AP_ACTUALIZE_MV_CALD_FRM_JOB AUTHID CURRENT_USER AS
BEGIN
    DBMS_OUTPUT.PUT_LINE('Specify materialized view to actualize');
END;
/

---------------------------------------------------------------------------------------------------------------------------------------------------------
-- JOBS
---------------------------------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------ ACTUALIZE MATERIALIZED VIEWS ---------------------------------------------------------------------
BEGIN
    DBMS_SCHEDULER.DROP_JOB(job_name => '"AJ_ACTUALIZE_MV"',defer => false,force => true);
END;
/

BEGIN
    DBMS_SCHEDULER.CREATE_JOB (
            job_name => '"AJ_ACTUALIZE_MV"',
            job_type => 'PLSQL_BLOCK',
            job_action => 'AP_ACTUALIZE_MV_CALD_FRM_JOB;',
            number_of_arguments => 0,
            start_date => NULL,
            repeat_interval => 'FREQ=MINUTELY;INTERVAL=10',
            end_date => NULL,
            enabled => FALSE,
            auto_drop => FALSE,
            comments => 'This job is used to actualize materialized view.It can be splitted if taking too long to run');

    DBMS_SCHEDULER.SET_ATTRIBUTE( 
             name => '"AJ_ACTUALIZE_MV"', 
             attribute => 'logging_level', value => DBMS_SCHEDULER.LOGGING_OFF);
END;
/