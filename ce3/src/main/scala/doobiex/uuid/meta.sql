CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table if not exists uuids
(
    id  uuid default uuid_generate_v4() not null constraint uuids_pk primary key
);