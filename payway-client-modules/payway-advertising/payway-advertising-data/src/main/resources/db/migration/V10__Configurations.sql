
--configurations
drop table if exists configurations cascade;
create table configurations
(
  id bigserial not null,
  key varchar(256) not null unique,
  value varchar(512),

  constraint pk_configurations_pkey primary key (id)
);