--agent_file
ALTER TABLE agent_files ALTER COLUMN configuration_id SET NOT NULL;
ALTER TABLE agent_files ALTER COLUMN name SET NOT NULL;
ALTER TABLE agent_files ALTER COLUMN digest SET NOT NULL;
ALTER TABLE agent_files ALTER COLUMN is_count_hits SET NOT NULL;

--agent_file_owner
ALTER TABLE agent_file_owners ALTER COLUMN name SET NOT NULL;
