create table groups (
    id serial
        constraint groups_pk
            primary key,
    name varchar not null
);

create table students (
    id serial
        constraint students_pk
            primary key,
    name varchar not null,
    hacker varchar not null,
    groupp integer
        constraint students_fk1
            references groups
);

create table ranks (
    id bigserial
        constraint ranks_pk
            primary key,
    date timestamp default now(),
    student integer not null
        constraint ranks_fk1
            references students,
    rank integer not null,
    score double precision not null
);

create table ranks_current (
    student integer not null
        constraint ranks_current_pk
            primary key
        constraint ranks_current_fk1
            references students,
    rank integer,
    score double precision,
    date timestamp
);
