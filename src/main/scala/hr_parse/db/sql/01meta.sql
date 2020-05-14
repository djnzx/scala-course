create table groups
(
    id serial primary key,
    name varchar not null
);

create table students
(
    id serial primary key,
    name varchar not null,
    hacker varchar,
    groupp integer constraint students_fk1
            references groups
);

alter table students owner to postgres;

create table ranks
(
    id bigserial not null
        constraint ranks_pk
            primary key,
    date timestamp default now(),
    student integer not null
        constraint ranks_fk1
            references students,
    rank integer not null,
    score double precision not null
);

alter table ranks owner to postgres;

create table ranks_current
(
    student integer not null
        constraint ranks_current_pk
            primary key
        constraint ranks_current_fk1
            references students,
    rank integer,
    score double precision,
    date timestamp
);

alter table ranks_current owner to postgres;

create view v_ranks_current(sid, sname, gid, gname, hacker, rank, score, date) as
SELECT s.id   AS sid,
       s.name AS sname,
       g.id   AS gid,
       g.name AS gname,
       s.hacker,
       rc.rank,
       rc.score,
       rc.date
FROM students s
         LEFT JOIN groups g ON s.groupp = g.id
         LEFT JOIN ranks_current rc ON s.id = rc.student;

alter table v_ranks_current owner to postgres;

create function upd_current_rank() returns trigger
    language plpgsql
as $$
BEGIN
    if (TG_OP = 'INSERT') then
        insert into ranks_current (student, rank, score, date)
        values (new.student, new.rank, new.score, new.date)
        on conflict (student) do
            update set rank=new.rank, score=new.score, date=new.date;
    end if;
    return new;
END;
$$;

alter function upd_current_rank() owner to postgres;

create trigger ranks_ai
    after insert
    on ranks
    for each row
execute procedure upd_current_rank();

