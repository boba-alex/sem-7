CREATE OR REPLACE TRIGGER t
  BEFORE
    INSERT OR
    UPDATE OF AMOUNT OR
    DELETE
  ON OPERATIONS
BEGIN
  CASE
    WHEN INSERTING THEN
      DBMS_OUTPUT.PUT_LINE('Inserting');
    WHEN UPDATING THEN
      DBMS_OUTPUT.PUT_LINE('Updating');
    WHEN DELETING THEN
      DBMS_OUTPUT.PUT_LINE('Deleting');
  END CASE;
END;

ALTER TABLE OPERATIONS 
ADD CONSTRAINT "OPERATIONS_CK_DATE" CHECK ("date" > TO_DATE('01-JAN-1900', 'DD-MON-YYYY'));

ALTER TABLE FAMILY_MEMBERS 
ADD CONSTRAINT "FAM_MEMB_CK_DATE" CHECK ("BIRTHDAY" > TO_DATE('01-JAN-1900', 'DD-MON-YYYY'));

ALTER TABLE OPER_TYPES_TO_CUR_TYPES 
ADD CONSTRAINT "OP_T_TO_CUR_T_CK_EXCH_RATE" CHECK ("EXCHANGE_RATE" > 0);

CREATE OR REPLACE TRIGGER  "BIOU_OPERATION" 
  before insert or update on "OPERATIONS"               
  for each row
declare any_rows_found number;
age date;
begin
    SELECT COUNT(oper_t."ID") INTO any_rows_found
    FROM "OPERATION_TYPES" oper_t
    JOIN "MAIN_OPERATION_TYPES" main_oper_t ON oper_t."MAIN_OPERATION_TYPES_ID" = main_oper_t."ID" 
    JOIN "TOP_OPERATION_TYPES" top_oper_t ON main_oper_t."TOP_OPERATION_TYPES_ID" = top_oper_t."ID" 
    WHERE top_oper_t."NAME" = 'Расход' AND oper_t."ID" = :NEW."OPERATION_TYPES_ID";
    SELECT family."BIRTHDAY" INTO age FROM "FAMILY_MEMBERS" family WHERE family."ID" = :NEW."FAMILY_MEMBERS_ID";
  if :NEW."AMOUNT" > 10 AND ((SYSDATE - age) / 365 < 14) AND (any_rows_found > 0)
  THEN RAISE_APPLICATION_ERROR(-20000, 'Ошибка: Если вам меньше 14 и вы тратите больше 10 рублей');
  end if; 
end; 

CREATE OR REPLACE TRIGGER  "BIOU2_OPERATION" 
  before insert or update on "OPERATIONS"               
  for each row
declare any_rows_found number;
age date;
begin
    SELECT COUNT(oper_t."ID") INTO any_rows_found
    FROM "OPERATION_TYPES" oper_t
    JOIN "MAIN_OPERATION_TYPES" main_oper_t ON oper_t."MAIN_OPERATION_TYPES_ID" = main_oper_t."ID" 
    JOIN "TOP_OPERATION_TYPES" top_oper_t ON main_oper_t."TOP_OPERATION_TYPES_ID" = top_oper_t."ID" 
    WHERE top_oper_t."NAME" = 'Доход' AND oper_t."ID" = :NEW."OPERATION_TYPES_ID";
    SELECT family."BIRTHDAY" INTO age FROM "FAMILY_MEMBERS" family WHERE family."ID" = :NEW."FAMILY_MEMBERS_ID";
  if :NEW."AMOUNT" > 10 AND ((SYSDATE - age) / 365 < 14) AND (any_rows_found > 0)
  THEN RAISE_APPLICATION_ERROR(-20000, 'Ошибка: Если вам меньше 14, то вы не можете зарабатывать легально');
  end if; 
end;