select n2
from list
right outer join (select n+1 n2 from list) list2 on (list2.n2 = list.n)
where n is null
order by n2
limit 1
