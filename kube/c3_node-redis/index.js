const express = require('express');
const redis = require('redis');
const process = require('process');

const app = express();
const client = redis.createClient({
  host: 'redis-server', // server name exposed by docker-compose
  port: 6379            // default port
});

client.set('visits', 0);

app.get('/', (req, res) => {
  client.get('visits', (err, visits) => {
    const v = parseInt(visits);
    if (v % 2 === 1) process.exit(1);
    res.send('Number of visits is ' + v);
    client.set('visits', v + 1);
  });
});

app.listen(8081, () => {
  console.log('Listening on port 8081');
});
