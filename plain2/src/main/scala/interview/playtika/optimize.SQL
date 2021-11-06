select department_id, avg(salary)
from users
group by department_id
having department_id = 2


