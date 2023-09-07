CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create type message_type AS ENUM('trace', 'debug', 'info', 'warn', 'error', 'fatal');

create table if not exists messages
(
    id  uuid default uuid_generate_v4() not null constraint messages_pk primary key,
    typ message_type,
    msg text
);