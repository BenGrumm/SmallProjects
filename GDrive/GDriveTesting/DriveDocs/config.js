var config = {

	windowscompdevelopment: {
			  user: 'postgres', // env var: PGUSER
			  database: 'postgres', // env var: PGDATABASE
			  password: '12345', // env var: PGPASSWORD
			  host: 'localhost', // Server hosting the postgres database
			  port: 5432, // env var: PGPORT
			  max: 10, // max number of clients in the pool
			  idleTimeoutMillis: 30000 // how long a client is allowed to remain idle before being closed
	},
	windowslaptopdevelopment: {
			  user: 'postgres', // env var: PGUSER
			  database: 'labs', // env var: PGDATABASE
			  password: 'W1nst0n', // env var: PGPASSWORD
			  host: 'localhost', // Server hosting the postgres database
			  port: 5432, // env var: PGPORT
			  max: 10, // max number of clients in the pool
			  idleTimeoutMillis: 30000 // how long a client is allowed to remain idle before being closed
	},
	linuxdevelopment: {
			  user: '', // env var: PGUSER
			  database: '', // env var: PGDATABASE
			  password: '12345', // env var: PGPASSWORD
			  host: '', // Server hosting the postgres database
			  port: 5432, // env var: PGPORT
			  max: 10, // max number of clients in the pool
			  idleTimeoutMillis: 30000 // how long a client is allowed to remain idle before being closed
	},
	production: {
			  user: 'wzv19xsu', // env var: PGUSER  - YOUR UEA username
			  database: 'wzv19xsu', // env var: PGDATABASE  - YOUR UEA username
			  password: 'SsytycthfhBsfpCyttgffacsr0', // env var: PGPASSWORD  - YOUR UEA password
			  host: 'cmpstudb-01.cmp.uea.ac.uk', // Server hosting the postgres database
			  port: 5432, // env var: PGPORT
			  max: 10, // max number of clients in the pool
			  idleTimeoutMillis: 30000 // how long a client is allowed to remain idle before being closed
	},
	
};
module.exports = config;