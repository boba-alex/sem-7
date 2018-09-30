--CREATE OR REPLACE TRIGGER t
--  BEFORE
--    INSERT OR
--    UPDATE OF AMOUNT OR
--    DELETE
--  ON OPERATIONS
--BEGIN
--  CASE
--    WHEN INSERTING THEN
--      DBMS_OUTPUT.PUT_LINE('Inserting');
--    WHEN UPDATING('AMOUNT') THEN
--      DBMS_OUTPUT.PUT_LINE('Updating amount');
--    WHEN UPDATING('department_id') THEN
--      DBMS_OUTPUT.PUT_LINE('Updating department ID');
--    WHEN DELETING THEN
--      DBMS_OUTPUT.PUT_LINE('Deleting');
--  END CASE;
--END;

ALTER TABLE OPERATIONS 
ADD CONSTRAINT "OPERATIONS_CK_DATE" CHECK ("date" > TO_DATE('01-JAN-1900', 'DD-MON-YYYY'));

CREATE OR REPLACE TRIGGER  "BIOU_OPERATION" 
  before insert or update on "OPERATIONS"               
  for each row
begin   
  if :NEW."AMOUNT" > 10 AND (SYS_DATE - :NEW."date" < 14) AND (EXISTS(SELECT oper_t."ID" FROM "OPERATION_TYPES" oper_t INNER JOIN "MAIN_OPERATION_TYPES" main_oper_t ON oper_t."MAIN_OPERATION_TYPES_ID" = main_oper_t."ID" INNER JOIN "TOP_OPERATION_TYPES" top_oper_t ON main_oper_t."TOP_OPERATION_TYPES_ID" = top_oper_t."ID" WHERE top_oper_t."NAME" = 'Расход' AND oper_t."ID" = :NEW."OPERATION_TYPES_ID"))

  THEN RAISE_APPLICATION_ERROR(-20000, 'Ошибка: Если вам меньше 14 и вы тратите больше 10 рублей');
  end if; 
end; 

--begin
--if EXISTS(SELECT oper_t."ID" FROM "OPERATION_TYPES" oper_t INNER JOIN "MAIN_OPERATION_TYPES" main_oper_t ON oper_t."MAIN_OPERATION_TYPES_ID" = main_oper_t."ID" INNER JOIN "TOP_OPERATION_TYPES" top_oper_t ON main_oper_t."TOP_OPERATION_TYPES_ID" = top_oper_t."ID" WHERE top_oper_t."NAME" = 'Расход' AND oper_t."ID" = :NEW."OPERATION_TYPES_ID")
--THEN RAISE_APPLICATION_ERROR(-20000, 'Ошибка: Если вам меньше 14 и вы тратите больше 10 рублей');
--end if;
--end;


