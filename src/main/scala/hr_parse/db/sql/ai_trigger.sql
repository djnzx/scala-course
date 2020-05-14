-- my code. DataGrip generates a bit different one

CREATE FUNCTION upd_current_rank() RETURNS trigger AS $upd_current_rank$
BEGIN
    if (TG_OP = 'INSERT') then
        insert into ranks_current (student, rank, score, date)
        values (new.student, new.rank, new.score, new.date)
        on conflict (student) do
            update set rank=new.rank, score=new.score, date=new.date;
    end if;
    return new;
END;
$upd_current_rank$ LANGUAGE plpgsql;

CREATE TRIGGER ranks_ai AFTER INSERT ON ranks
    FOR EACH ROW EXECUTE PROCEDURE upd_current_rank();
