--users
create table users (
    id bigint not null,
    login varchar not null unique,
    constraint pk_user_pkey primary key (id)
);

--seq_user
drop sequence if exists seq_user;
create sequence seq_user;


--configuration
create table configuration (
    id bigint not null,
    name varchar not null unique,
    user_id bigint references users not null,
    constraint pk_configuration_pkey primary key (id)
);

--seq_configuration
drop sequence if exists seq_configuration;
create sequence seq_configuration;

--agent_file
alter table agent_file add column configuration_id bigint;