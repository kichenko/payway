--params
set statement_timeout = 0;
set client_encoding = 'utf8';
set standard_conforming_strings = on;
set check_function_bodies = false;
set client_min_messages = warning;
set search_path = public, pg_catalog;
set default_tablespace = '';
set default_with_oids = false;


--agent_file_owner
drop table if exists agent_file_owner cascade;
create table agent_file_owner
(
  id bigint not null,
  name varchar(1024),
  description varchar(1024),

  constraint pk_agent_file_owner_pkey primary key (id)
);

--seq_agent_file_owner
drop sequence if exists seq_agent_file_owner;
create sequence seq_agent_file_owner;


--agent_file
drop table if exists agent_file cascade;
create table agent_file
(
  id bigint not null,
  name varchar(1024),
  expression varchar(1024),
  kind varchar(12),
  agent_file_owner_id bigint,
  constraint pk_agent_file_pkey primary key (id),
  constraint fk_aget_file_owner foreign key (agent_file_owner_id)
    references agent_file_owner(id)
    on update cascade on delete cascade

);

--seq_agent_file
drop sequence if exists seq_agent_file;
create sequence seq_agent_file;

--index
create index idx_fk_agent_file_owner_id
  on agent_file
  using btree
  (agent_file_owner_id);
