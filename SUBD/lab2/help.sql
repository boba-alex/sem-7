--used to build correct queries.sql
SELECT oper_t."ID" FROM "OPERATION_TYPES" oper_t INNER JOIN "MAIN_OPERATION_TYPES" main_oper_t ON oper_t."MAIN_OPERATION_TYPES_ID" = main_oper_t."ID" INNER JOIN "TOP_OPERATION_TYPES" top_oper_t ON main_oper_t."TOP_OPERATION_TYPES_ID" = top_oper_t."ID" WHERE top_oper_t."NAME" = 'Расход';
--AND oper_t."ID" = :NEW."OPERATION_TYPES_ID"


SELECT COUNT(oper_t."ID")
    FROM "OPERATION_TYPES" oper_t
    JOIN "MAIN_OPERATION_TYPES" main_oper_t ON oper_t."MAIN_OPERATION_TYPES_ID" = main_oper_t."ID" 
    JOIN "TOP_OPERATION_TYPES" top_oper_t ON main_oper_t."TOP_OPERATION_TYPES_ID" = top_oper_t."ID" 
    WHERE top_oper_t."NAME" = 'Расход' AND oper_t."ID" = 44;

begin
dbms_output.put_line((TO_DATE('03-12-1901', 'DD-MM-YYYY') - TO_DATE('02-12-1900', 'DD-MM-YYYY')) / 365);
end;
declare
strTime1 varchar2(50) := '02/08/2013 01:09:42 PM';
strTime2 varchar2(50) := '02/08/2013 11:09:00 PM';
v_date1 date := to_date(strTime1,'DD/MM/YYYY HH:MI:SS PM');
v_date2 date := to_date(strTime2,'DD/MM/YYYY HH:MI:SS PM');
difrence_In_Hours number;
difrence_In_minutes number;
difrence_In_seconds number;
begin
    difrence_In_Hours   := (v_date2 - v_date1) * 24;
    difrence_In_minutes := difrence_In_Hours * 60;
    difrence_In_seconds := difrence_In_minutes * 60;

    dbms_output.put_line(strTime1);        
    dbms_output.put_line(strTime2);
    dbms_output.put_line('*******');
    dbms_output.put_line('difrence_In_Hours  : ' || difrence_In_Hours);
    dbms_output.put_line('difrence_In_minutes: ' || difrence_In_minutes);
    dbms_output.put_line('difrence_In_seconds: ' || difrence_In_seconds);        
end ;