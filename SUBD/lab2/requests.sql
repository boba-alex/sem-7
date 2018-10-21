--Клиентское приложение должно обрабатывать следующие запросы:
--  найти сведения обо всех расходах, выполненных по конкретному направлению в текущем году (направление расходов – параметр запроса);
--	найти сведения обо всех доходных и расходных операциях с отрицательной суммой;
--	найти сведения о пяти статьях расходов, по которым были выполнены самые большие суммарные расходы в текущем году. Отсортировать результат запроса по убыванию этих сумм.

--  найти сведения обо всех расходах, выполненных по конкретному направлению в текущем году (направление расходов – параметр запроса);
SELECT oper.id, oper."date", oper.description, oper.cash, oper.amount, fam_m.name, fam_m.surname, oper_t.name AS Поднаправление, main_oper_t.name AS Направление, top_oper_t.name AS ДоходРасход 
FROM operations oper
    JOIN family_members fam_m ON oper.family_members_id = fam_m.id
    JOIN operation_types oper_t ON oper.operation_types_id = oper_t.id
    JOIN main_operation_types main_oper_t ON oper_t.main_operation_types_id = main_oper_t.id 
    JOIN top_operation_types top_oper_t ON main_oper_t.top_operation_types_id = top_oper_t.id
    WHERE top_oper_t.name = 'Расход'
    AND main_oper_t.name = 'Красота'
    AND oper."date" BETWEEN trunc(sysdate, 'YEAR') AND add_months(trunc(sysdate, 'YEAR'), 12)-1/24/60/60;
    
--	найти сведения обо всех доходных и расходных операциях с отрицательной суммой;
SELECT oper.id, oper."date", oper.description, oper.cash, oper.amount, fam_m.name, fam_m.surname, oper_t.name AS Поднаправление, main_oper_t.name AS Направление, top_oper_t.name AS ДоходРасход 
FROM operations oper
    JOIN family_members fam_m ON oper.family_members_id = fam_m.id
    JOIN operation_types oper_t ON oper.operation_types_id = oper_t.id
    JOIN main_operation_types main_oper_t ON oper_t.main_operation_types_id = main_oper_t.id 
    JOIN top_operation_types top_oper_t ON main_oper_t.top_operation_types_id = top_oper_t.id
    WHERE oper.amount < 0;
    
-- вариант б) а тут если общая сумма по типу операции
SELECT oper_t.name, top_oper_t.name AS ДоходРасход, SUM(oper.amount) AS ОбщаяСумма
FROM operations oper
    JOIN family_members fam_m ON oper.family_members_id = fam_m.id
    JOIN operation_types oper_t ON oper.operation_types_id = oper_t.id
    JOIN main_operation_types main_oper_t ON oper_t.main_operation_types_id = main_oper_t.id 
    JOIN top_operation_types top_oper_t ON main_oper_t.top_operation_types_id = top_oper_t.id
    GROUP BY oper_t.name, top_oper_t.name HAVING SUM(oper.amount) < 0;
    
--	найти сведения о пяти статьях расходов, по которым были выполнены самые большие суммарные расходы в текущем году. Отсортировать результат запроса по убыванию этих сумм.
SELECT * FROM(SELECT main_oper_t.name, SUM(oper.amount) AS ОбщаяСумма
FROM operations oper
    JOIN family_members fam_m ON oper.family_members_id = fam_m.id
    JOIN operation_types oper_t ON oper.operation_types_id = oper_t.id
    JOIN main_operation_types main_oper_t ON oper_t.main_operation_types_id = main_oper_t.id 
    JOIN top_operation_types top_oper_t ON main_oper_t.top_operation_types_id = top_oper_t.id
    WHERE top_oper_t.name = 'Расход'
    AND oper."date" BETWEEN trunc(sysdate, 'YEAR') AND add_months(trunc(sysdate, 'YEAR'), 12)-1/24/60/60
    GROUP BY main_oper_t.name ORDER BY ОбщаяСумма DESC)
    WHERE ROWNUM <=5;
