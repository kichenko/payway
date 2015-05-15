--agent_file
ALTER TABLE agent_file ALTER COLUMN configuration_id SET NOT NULL;
ALTER TABLE agent_file ALTER COLUMN name SET NOT NULL;
ALTER TABLE agent_file ALTER COLUMN digest SET NOT NULL;
ALTER TABLE agent_file ALTER COLUMN is_count_hits SET NOT NULL;

--agent_file_owner
ALTER TABLE agent_file_owner ALTER COLUMN name SET NOT NULL;
