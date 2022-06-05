explain with missing as (
select l1.n + 1 as x
from list l1
left outer join list l2 on l2.n = (l1.n + 1)
where l2.n is null
)
select * from missing
order by x
    limit (select count(*) from missing) - 1