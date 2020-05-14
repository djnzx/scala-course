CREATE FUNCTION upd_current_rank() RETURNS trigger AS $$
BEGIN
    if (TG_OP = 'INSERT') then
        insert into ranks_current (student, rank, score, date)
        values (new.student, new.rank, new.score, new.date)
        on conflict (student) do
            update set rank=new.rank, score=new.score, date=new.date;
    end if;
    return new;
END;
$$ LANGUAGE plpgsql;

create trigger ranks_ai
    after insert
    on ranks
    for each row
execute procedure upd_current_rank();
