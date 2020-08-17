const keys = require('./keys');

// express
const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();
app.use(cors());
app.use(bodyParser.json())

// postgres
const { Pool } = require('pg');
const pgClient = new Pool({
  host: keys.pgHost,
  port: keys.pgPort,
  database: keys.pgDatabase,
  user: keys.pgUser,
  password: keys.pgPassword
});
pgClient.on('error', () => console.log('Lost PG connection'));
pgClient.query('CREATE TABLE IF NOT EXISTS values (number INT)')
    .catch(err => console.log(err));

// redis
const redis = require('redis');
const redisClient = redis.createClient({
  host: keys.redisHost,
  port: keys.redisPort,
  retry_strategy: () => 1000
});
const redisPublisher = redisClient.duplicate();

// express route handlers

app.get('/', (rq, rs) => {
  rs.send('Hi');
});

app.get('/value/all', async (rq, rs) => {
  const values = await pgClient.query('SELECT * from values');
  rs.send(values.rows);
});

app.get('/values/current', async (rq, rs) => {
  redisClient.hgetall('values', (err, values) => {
    rs.send(values);
  })
});

app.post('', async (rq, rs) => {
  const index = rq.body.index;

  if (parseInt(index) > 40) {
    return rs.status(422).send(`index ${index} too high!`);
  }

  redisClient.hset('values', index, 'Nothing yet!');
  redisPublisher.publish('insert', index);
  pgClient.query('INSERT INTO values(nuimber) VALUES($1)', [index]);
  rs.send({ working: true });
})

app.listen(5000, err => {
  console.log('Listening');
})
