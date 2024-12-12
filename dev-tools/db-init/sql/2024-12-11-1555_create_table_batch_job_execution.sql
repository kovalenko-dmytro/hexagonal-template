CREATE TABLE batch_job_execution (
	job_execution_id int8 NOT NULL,
	"version" int8 NULL,
	job_instance_id int8 NOT NULL,
	create_time timestamp NOT NULL,
	start_time timestamp NULL,
	end_time timestamp NULL,
	status varchar(10) NULL,
	exit_code varchar(2500) NULL,
	exit_message varchar(2500) NULL,
	last_updated timestamp NULL,
	CONSTRAINT batch_job_execution_pkey PRIMARY KEY (job_execution_id),
	CONSTRAINT job_inst_exec_fk FOREIGN KEY (job_instance_id) REFERENCES batch_job_instance(job_instance_id)
);