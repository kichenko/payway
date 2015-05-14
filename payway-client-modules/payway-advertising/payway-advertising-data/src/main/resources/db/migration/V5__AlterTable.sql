--agent_file
alter table agent_file add CONSTRAINT configuration_id_fkey FOREIGN KEY (configuration_id)
      REFERENCES configuration (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

--agent_file
alter table agent_file add CONSTRAINT agent_configuration_name_key UNIQUE (configuration_id, name);

--agent_file
ALTER TABLE agent_file ALTER COLUMN kind SET NOT NULL;