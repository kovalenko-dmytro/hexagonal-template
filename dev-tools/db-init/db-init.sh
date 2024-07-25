#!/bin/bash

set -e
set -u

function create_database() {
	local database=$1
	echo "  Creating user and database '$database'"
	psql -v ON_ERROR_STOP=1 --username "$DB_USERNAME" <<-EOSQL
	    CREATE DATABASE $database;
EOSQL
}

if [ -n "$DB_NAME" ]; then
	echo "Database creation requested: $DB_NAME"
		create_database $DB_NAME
	echo "Database $DB_NAME created"
fi