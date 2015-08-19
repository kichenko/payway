
--agent_files
alter table agent_files add CONSTRAINT file_name_unique UNIQUE (name);