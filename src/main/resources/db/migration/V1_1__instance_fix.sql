ALTER TABLE package_instance_license_map drop COLUMN packge_instance_id;
ALTER TABLE package_instance_license_map add column package_instance_id INT REFERENCES package_instance (id);