CREATE TABLE batch_job_execution_params (
	job_execution_id int8 NOT NULL,
	parameter_name varchar(100) NOT NULL,
	parameter_type varchar(100) NOT NULL,
	parameter_value varchar(2500) NULL,
	identifying bpchar(1) NOT NULL,
	CONSTRAINT job_exec_params_fk FOREIGN KEY (job_execution_id) REFERENCES batch_job_execution(job_execution_id)
);