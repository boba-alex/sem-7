SELECT birthdate FROM emp WHERE empname='Robert Grishuk';

SELECT * FROM emp WHERE birthdate >= to_date('01-01-1980', 'dd-mm-yyyy') AND birthdate <= to_date('31-12-1982', 'dd-mm-yyyy');

SELECT minsalary FROM job WHERE jobname = 'Accountant';

SELECT COUNT(DISTINCT empno) FROM career WHERE startdate <= to_date('31-05-2010', 'dd-mm-yyyy') AND (startdate < enddate OR enddate IS NULL);

SELECT YEAR, max(bonvalue) 
FROM bonus 
WHERE (YEAR > 2013 
AND YEAR < 2018) 
GROUP BY YEAR 
ORDER BY YEAR;

SELECT DISTINCT career.deptid 
FROM career 
INNER JOIN emp ON emp.empno = career.empno 
WHERE (emp.empname = 'Robert Grishuk');

SELECT DISTINCT job.jobname, emp.empname 
FROM career 
INNER JOIN emp ON emp.empno = career.empno 
INNER JOIN job ON job.jobno = career.jobno 
WHERE (emp.empname = 'Vera Rovdo' OR emp.empname = 'Dave Hollander');

SELECT emp.empname, job.jobno, career.startdate, career.enddate 
FROM career 
INNER JOIN emp ON emp.empno = career.empno 
INNER JOIN job ON job.jobno = career.jobno 
WHERE (job.jobname = 'Engineer' OR job.jobname = 'Programmer');

SELECT COALESCE(career.enddate, cast('' AS DATE)) FROM career;
SELECT emp.empname, job.jobname, career.startdate,
COALESCE(career.enddate,'') FROM career 
INNER JOIN emp ON emp.empno = career.empno 
INNER JOIN job ON job.jobno = career.jobno 
WHERE (job.jobname = 'Accountant' OR job.jobname = 'Salesman');

SELECT emp.empname, job.jobname, career.startdate, career.enddate 
FROM career 
INNER JOIN emp ON emp.empno = career.empno 
INNER JOIN job ON job.jobno = career.jobno 
WHERE (job.jobname = 'Accountant' OR job.jobname = 'Salesman');

SELECT COUNT(DISTINCT emp.empname) 
FROM emp 
INNER JOIN career ON emp.empno = career.empno 
WHERE ( deptid = 'B02' AND enddate >= to_date('01.01.2005','dd.mm.yyyy') 
AND (enddate IS NULL OR (enddate <= CURRENT_DATE AND startdate < enddate)));

SELECT DISTINCT emp.empname
FROM emp 
INNER JOIN career ON emp.empno = career.empno 
WHERE ( deptid = 'B02' AND enddate >= to_date('01.01.2005','dd.mm.yyyy') 
AND (enddate IS NULL OR (enddate <= CURRENT_DATE AND startdate < enddate)));

SELECT DISTINCT dept.deptid, dept.deptname 
FROM dept JOIN career ON career.deptid = dept.deptid 
WHERE (enddate >= to_date('01-01-2017', 'dd-mm-yyyy') AND enddate <= to_date('31-12-2017','dd-mm-yyyy')) 
OR (enddate IS NULL and startdate <= to_date('31-12-2017','dd-mm-yyyy')) 
GROUP BY(dept.deptid, dept.deptname) HAVING COUNT(empno) <= 5;

SELECT DISTINCT dept.deptid, dept.deptname from dept where dept.deptid in( 
SELECT DISTINCT career.deptid from career where career.empno in( 
SELECT DISTINCT emp.empno 
FROM emp LEFT OUTER JOIN bonus ON bonus.empno = emp.empno 
WHERE (SELECT count(*) FROM bonus 
WHERE bonus.empno = emp.empno AND bonus.year = 2017 and bonus.month BETWEEN 1 AND 12) = 0));

SELECT count(DISTINCT emp.empno) 
FROM emp LEFT OUTER JOIN career ON career.empno = emp.empno 
INNER JOIN dept ON dept.deptid = career.deptid 
WHERE ((SELECT count(*) FROM career 
WHERE career.empno = emp.empno 
AND (dept.deptname = 'Research') ) = 0 
AND (SELECT count(*) FROM career 
WHERE career.empno = emp.empno 
AND (dept.deptname = 'Support') ) != 0);

SELECT emp.empno, emp.empname 
FROM career 
INNER JOIN emp ON career.empno = emp.empno 
WHERE career.enddate IS NOT NULL GROUP BY emp.empno, emp.empname 
HAVING COUNT(career.deptid) >=2;

SELECT emp.empno, emp.empname FROM emp
WHERE empno in (SELECT empno FROM career WHERE enddate IS NOT NULL GROUP BY empno 
HAVING COUNT(deptid) >= 2);

SELECT emp.empno, empname 
FROM emp 
JOIN career ON emp.empno = career.empno 
GROUP BY emp.empno, empname 
HAVING sum(MONTHS_BETWEEN(NVL(enddate, current_date), startdate)) <= 8 * 12;

SELECT emp.empno, emp.empname FROM emp
WHERE empno in (SELECT empno FROM career WHERE enddate IS NOT NULL);

SELECT DISTINCT emp.empno, 
emp.empname 
FROM emp 
INNER JOIN career ON career.empno = emp.empno 
WHERE 
(( SELECT count(career.empno) 
FROM career 
WHERE career.empno = emp.empno ) >= 2) 
and (career.empno not in (SELECT DISTINCT empno 
FROM career 
WHERE career.enddate is null));
