--params
set statement_timeout = 0;
set client_encoding = 'utf8';
set standard_conforming_strings = on;
set check_function_bodies = false;
set client_min_messages = warning;
set search_path = public, pg_catalog;
set default_tablespace = '';
set default_with_oids = false;


--agent_file_owners
drop table if exists agent_file_owners cascade;
create table agent_file_owners
(
  id bigint not null,
  name varchar(256),
  description varchar(512),

  constraint pk_agent_file_owners_pkey primary key (id)
);

--seq_agent_file_owners
drop sequence if exists seq_agent_file_owners;
create sequence seq_agent_file_owners;


--agent_files
drop table if exists agent_files cascade;
create table agent_files
(
  id bigint not null,
  name varchar(1024),
  expression varchar(2048),
  kind varchar(12),
  agent_file_owner_id bigint,
  constraint pk_agent_file_pkey primary key (id),
  constraint fk_aget_file_owners foreign key (agent_file_owner_id)
    references agent_file_owners(id)
    on update cascade on delete cascade

);

--seq_agent_files
drop sequence if exists seq_agent_files;
create sequence seq_agent_files;

--index
create index idx_fk_agent_file_owner_id
  on agent_files
  using btree
  (agent_file_owner_id);
