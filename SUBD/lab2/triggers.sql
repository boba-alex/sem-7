--1. Триггер должен запрещать членам семьи, не достигшим 16-летнего возраста, совершать расходные операции на сумму более 10 рублей;
--2. Триггер должен запрещать проведение расходных операций всех членов семьи в месяц на сумму большую, чем полученный всеми членами в месяц доходах;
--3. Триггер должен отслеживать, чтобы в течение года состав семьи изменялся не более чем на одного члена.  

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

--1. Триггер должен запрещать членам семьи, не достигшим 16-летнего возраста, совершать расходные операции на сумму более 10 рублей;
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
  if :NEW."AMOUNT" > 10 AND ((SYSDATE - age) / 365 < 16) AND (any_rows_found > 0)
  THEN RAISE_APPLICATION_ERROR(-20000, 'Ошибка: Если вам меньше 16 , то вы не можете тратить больше 10 рублей');
  end if; 
end; 

--2. Триггер должен запрещать проведение расходных операций всех членов семьи в месяц на сумму большую, чем полученный всеми членами в месяц доходах;
CREATE OR REPLACE TRIGGER  "SPEND_LESS_THAN_EARN" 
  before insert or update on "OPERATIONS"               
  for each row
declare 
any_expense_oper number;
earned_money number;
spent_money number default 0;
begin
    SELECT COUNT(oper_t.id) INTO any_expense_oper
    FROM "OPERATION_TYPES" oper_t
    JOIN "MAIN_OPERATION_TYPES" main_oper_t ON oper_t."MAIN_OPERATION_TYPES_ID" = main_oper_t."ID" 
    JOIN "TOP_OPERATION_TYPES" top_oper_t ON main_oper_t."TOP_OPERATION_TYPES_ID" = top_oper_t."ID" 
    WHERE top_oper_t."NAME" = 'Расход' AND oper_t."ID" = :NEW."OPERATION_TYPES_ID";
    
    SELECT COALESCE(SUM(oper.amount), 0) INTO earned_money
    FROM "OPERATIONS" oper
    JOIN "OPERATION_TYPES" oper_t ON oper.operation_types_id = oper_t.id
    JOIN "MAIN_OPERATION_TYPES" main_oper_t ON oper_t."MAIN_OPERATION_TYPES_ID" = main_oper_t."ID" 
    JOIN "TOP_OPERATION_TYPES" top_oper_t ON main_oper_t."TOP_OPERATION_TYPES_ID" = top_oper_t."ID" 
    WHERE top_oper_t."NAME" = 'Доход' AND EXTRACT(YEAR FROM oper."date") = EXTRACT(YEAR FROM :NEW."date") AND EXTRACT(MONTH FROM oper."date") = EXTRACT(MONTH FROM :NEW."date");
    
    SELECT COALESCE(SUM(oper.amount), 0) INTO spent_money
    FROM "OPERATIONS" oper
    JOIN "OPERATION_TYPES" oper_t ON oper.operation_types_id = oper_t.id
    JOIN "MAIN_OPERATION_TYPES" main_oper_t ON oper_t."MAIN_OPERATION_TYPES_ID" = main_oper_t."ID" 
    JOIN "TOP_OPERATION_TYPES" top_oper_t ON main_oper_t."TOP_OPERATION_TYPES_ID" = top_oper_t."ID" 
    WHERE top_oper_t."NAME" = 'Расход' AND EXTRACT(YEAR FROM oper."date") =  EXTRACT(YEAR FROM :NEW."date") AND EXTRACT(MONTH FROM oper."date") = EXTRACT(MONTH FROM :NEW."date");
    
    if (any_expense_oper > 0) AND (:NEW."AMOUNT" + spent_money) > earned_money
  THEN RAISE_APPLICATION_ERROR(-20000, 'Ошибка: Запрещено расходовать в месяц семье больше, чем заработала!');
  end if; 
end; 

--3. Триггер должен отслеживать, чтобы в течение года состав семьи изменялся не более чем на одного члена.  
CREATE OR REPLACE TRIGGER  "ADD_ONE_FAMILY_MEMBER" 
  before insert on "FAMILY_MEMBERS"         
  for each row
declare 
new_member_in_year_count number;
begin
    SELECT COUNT(family.id) INTO new_member_in_year_count 
    FROM "FAMILY_MEMBERS" family
    WHERE EXTRACT(YEAR FROM family.birthday) =  EXTRACT(YEAR FROM :NEW.birthday);

    if (new_member_in_year_count > 0)
  THEN RAISE_APPLICATION_ERROR(-20000, 'Ошибка: В семье кол-во людей изменяется лишь на один в год!');
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
  if :NEW."AMOUNT" > 10 AND ((SYSDATE - age) / 365 < 16) AND (any_rows_found > 0)
  THEN RAISE_APPLICATION_ERROR(-20000, 'Ошибка: Если вам меньше 16, то вы не можете зарабатывать легально больше 10 рублей');
  end if; 
end;

