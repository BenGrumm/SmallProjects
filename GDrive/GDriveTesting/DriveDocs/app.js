const env = process.env.NODE_ENV || 'windowslaptopdevelopment';
const config = require('./config.js')[env];
const express=require('express');
const bodyParser = require('body-parser');
const pug = require('pug');
const app=express();
const path = require('path');
const pg = require('pg');


// parse application/json
app.use(bodyParser.json());

// parse application/x-www-form-urlencoded
app.use(bodyParser.urlencoded({ extended: true }));

//Set where to look for templates such as css and js files here

app.use(express.static(__dirname + '/public'));

//set pug view engine here

app.set('view engine', 'pug')

// home page
app.get('/',function(req,res)
{  
  res.render('index', {title:'Postgres Demo', message:'Postgres NodeJS and JSON processing demo'});
});


// add new person form
app.get('/form',function(req,res)
{  
	res.render('form', {title:'Add new Person form', message:'Node Postgres Demo'});
});
	
 
// search project form
app.get('/project',function(req,res)
	{  
	  res.render('project_form', {title:'Search Project information', message:'Enter project code'});
	});

//process search a project 
app.post('/search_project', async function(req,res, next)
	{  
		// to be completed here
	});	

// query 1
app.get('/qa', async(req,res)=> {
	let data = {};
	const pool = new pg.Pool(config);
	const client = await pool.connect();
	const qa = 'select * from demo.emp;';
	let results = await client.query(qa);
	client.release();
	data = results.rows;
	count = results.rows.length;
	console.log(data);
	console.log(count);
	res.render('qa', {title:'Postgres Demo', message:'Testing Qa results...', total:count, data:data});
});

// query 2
app.get('/qb', async function(req,res)
{   
	let data = {};
	const pool = new pg.Pool(config);
	const client = await pool.connect();
	const qb = 'select name, hourly_rate, title from demo.emp where title=\'Analyst\' order by name;';
	let results = await client.query(qb);
	client.release();
	data = results.rows;
	count = results.rows.length;
	console.log(data);
	console.log(count);
	res.render('qb', {title:'Postgres Demo', message:'Testing Qb results...', total:count, data:data});
});

// query 3 - to be completed
app.get('/qc', async function(req,res)
{   
	let data = {};
	const pool = new pg.Pool(config);
	const client = await pool.connect();
	const qc = 'select distinct title from demo.emp;';
	let results = await client.query(qc);
	client.release();
	data = results.rows;
	count = results.rows.length;
	console.log(data);
	console.log(count);
	res.render('qc', {title:'Node JS & Postgres Demo - Query results', message:'Testing Qc results...', total:count, data:data});
});

// query 4 - to be completed
app.get('/qd', async function(req,res)
{   
	let data = {};
	const pool = new pg.Pool(config);
	const client = await pool.connect();
	const qc = 'select name, (hourly_rate * 40) from demo.emp;';
	let results = await client.query(qc);
	client.release();
	data = results.rows;
	count = results.rows.length;
	console.log(data);
	console.log(count);
	res.render('qd', {title:'Node JS & Postgres Demo - Query results', message:'Testing Qd results...', total:count, data:data});
});

//query 5 - to be completed
app.get('/qe', async function(req,res)
{   
	let data = {};
	const pool = new pg.Pool(config);
	const client = await pool.connect();
	const qe = 'select name, (hourly_rate * 40) AS weekly_cost from demo.emp;';
	let results = await client.query(qe);
	client.release();
	data = results.rows;
	count = results.rows.length;
	console.log(data);
	console.log(count);
	res.render('qe', {title:'Node JS & Postgres Demo - Query results', message:'Testing Qe results...', total:count, data:data});
});

//query 6 - to be completed
app.get('/qf', async function(req,res)
{   
	let data = {};
	const pool = new pg.Pool(config);
	const client = await pool.connect();
	const qf = 'select name, (hourly_rate * 40) AS weekly_cost from demo.emp;';
	let results = await client.query(qf);
	client.release();
	data = results.rows;
	count = results.rows.length;
	console.log(data);
	console.log(count);
	res.render('qf', {title:'Node JS & Postgres Demo - Query results', message:'Testing Qf results...', total:count, data:data});
});

app.post('/new', async function(req,res, next)
{
	let results;
	const pool = new pg.Pool(config);
	const client = await pool.connect();
	let name = req.body.name;
	let title = req.body.jtitle;
	let hour = req.body.hrate;
	let idResult = await client.query('SELECT emp_id FROM demo.emp ORDER BY emp_id DESC;');
	let maxID = idResult.rows[0].emp_id;
	const q = `INSERT INTO demo.emp(emp_id, name, title, hourly_rate) VALUES(${maxID + 1}, '${name}', '${title}', ${hour});`;
	// callback
	client.query(q, (err, results) => {
		if (err) {
			console.log(err.stack)
			res.render('form', {title:'Postgres Demo',
			message:'Sorry something went wrong! The data has not been processed'});
		} else {
			client.release();
			res.render('form', {title:'Postgres Demo', message:'demois successfull!', name:req.body.name, title:req.body.title, hour:req.body.hour });
		}
	});
});

var server = app.listen(3000,function() {
	console.log('Postgres demo app listening on port 3000!')
});