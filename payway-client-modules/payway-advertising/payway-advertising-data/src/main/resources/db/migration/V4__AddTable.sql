--users
create table users (
    id bigserial primary key,
    login varchar not null unique
);

--configuration
create table configuration (
    id bigserial primary key,
    name varchar not null unique,
    user_id bigint references users not null
);

--agent_file
alter table agent_file add column configuration_id bigint;