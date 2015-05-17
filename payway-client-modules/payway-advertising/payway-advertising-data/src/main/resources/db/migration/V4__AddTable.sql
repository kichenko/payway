--users
create table users (
    id bigint not null,
    login varchar(25) not null unique,
    constraint pk_user_pkey primary key (id)
);

--seq_user
drop sequence if exists seq_users;
create sequence seq_users;


--configuration
create table configurations (
    id bigint not null,
    name varchar(50) not null unique,
    user_id bigint references users not null,
    constraint pk_configurations_pkey primary key (id)
);

--seq_configuration
drop sequence if exists seq_configurations;
create sequence seq_configurations;

--agent_file
alter table agent_files add column configuration_id bigint;