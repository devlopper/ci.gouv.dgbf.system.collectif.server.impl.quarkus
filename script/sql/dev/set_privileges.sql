-- Grant privileges to SIIBC_COLLECTIF
-- CA
BEGIN
   FOR R IN (SELECT owner, table_name FROM all_tables WHERE owner='SIIBC_CA') LOOP
      EXECUTE IMMEDIATE 'grant SELECT,ON COMMIT REFRESH on '||R.owner||'.'||R.table_name||' to SIIBC_COLLECTIF';
   END LOOP;
-- CPP
   FOR R IN (SELECT owner, table_name FROM all_tables WHERE owner='SIIBC_CPP') LOOP
      EXECUTE IMMEDIATE 'grant SELECT,ON COMMIT REFRESH on '||R.owner||'.'||R.table_name||' to SIIBC_COLLECTIF';
   END LOOP;
-- ADS
   FOR R IN (SELECT owner, table_name FROM all_tables WHERE owner='SIIBC_ADS') LOOP
      EXECUTE IMMEDIATE 'grant SELECT,ON COMMIT REFRESH on '||R.owner||'.'||R.table_name||' to SIIBC_COLLECTIF';
   END LOOP;
-- NEC
   FOR R IN (SELECT owner, table_name FROM all_tables WHERE owner='SIIBC_NEC') LOOP
      EXECUTE IMMEDIATE 'grant SELECT,ON COMMIT REFRESH on '||R.owner||'.'||R.table_name||' to SIIBC_COLLECTIF';
   END LOOP;
-- FINEXT
   FOR R IN (SELECT owner, table_name FROM all_tables WHERE owner='SIIBC_FINEXT') LOOP
      EXECUTE IMMEDIATE 'grant SELECT,ON COMMIT REFRESH on '||R.owner||'.'||R.table_name||' to SIIBC_COLLECTIF';
   END LOOP;
-- BUDGET
   FOR R IN (SELECT owner, table_name FROM all_tables WHERE owner='SIIBC_BUDGET') LOOP
      EXECUTE IMMEDIATE 'grant SELECT,ON COMMIT REFRESH on '||R.owner||'.'||R.table_name||' to SIIBC_COLLECTIF';
   END LOOP;
-- MEA
   FOR R IN (SELECT owner, table_name FROM all_tables WHERE owner='SIIBC_MEA') LOOP
      EXECUTE IMMEDIATE 'grant SELECT,ON COMMIT REFRESH on '||R.owner||'.'||R.table_name||' to SIIBC_COLLECTIF';
   END LOOP;
END;