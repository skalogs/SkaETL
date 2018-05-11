const express = require('express');
const history = require('connect-history-api-fallback');
const backend = require('./route/backend');
const bodyParser = require('body-parser');


const app = express();
app.use(bodyParser.json()); // for parsing application/json
app.use(bodyParser.urlencoded({ extended: true })); // for parsing application/x-www-form-urlencoded

const staticFileMiddleware = express.static('dist');
app.use(staticFileMiddleware);
app.use(history({
  disableDotRule: true,
  verbose: true
}));
app.use(staticFileMiddleware);

backend(app);

const port = 5555;

app.listen(port, () => {
  console.log(`Example app listening on port ${5555}!`);
});
