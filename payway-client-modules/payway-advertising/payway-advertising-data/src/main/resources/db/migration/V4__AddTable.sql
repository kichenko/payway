--users
create table users (
    id bigserial not null,
    login varchar(25) not null unique,
    constraint pk_user_pkey primary key (id)
);

--configuration
create table configurations (
    id bigserial not null,
    name varchar(50) not null unique,
    user_id bigint references users not null,
    constraint pk_configurations_pkey primary key (id)
);

--agent_file
alter table agent_files add column configuration_id bigint;