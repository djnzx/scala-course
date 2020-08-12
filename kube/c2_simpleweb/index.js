const express = require('express');

const app = express();

app.get('/', (rq, rs) => {
  rs.send('Hi, there, again2!');
});

app.listen(8080, () => {
  console.log('Listening on 8080');
});
