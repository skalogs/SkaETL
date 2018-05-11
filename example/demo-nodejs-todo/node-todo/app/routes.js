var Todo = require('./models/todo');
var bunyan = require('bunyan');
var log = bunyan.createLogger({name: "todo-logger", project: "demo-todo", type: "nodejs"});

var clientProm = require('prom-client');

var collectDefaultMetrics = clientProm.collectDefaultMetrics;

// Probe every 5th second.
collectDefaultMetrics({ timeout: 5000 });
var counterAddTodo = new clientProm.Counter({
  name: 'nb_add_todo',
  help: 'number add todo'
});

var counterRemoveTodo = new clientProm.Counter({
  name: 'nb_remove_todo',
  help: 'number remove todo'
});

function getTodos(res) {
    Todo.find(function (err, todos) {

        // if there is an error retrieving, send the error. nothing after res.send(err) will execute
        if (err) {
            res.send(err);
        }

        res.json(todos); // return all todos in JSON format
    });
};

module.exports = function (app) {

    app.get('/metrics', (req, res) => {
      res.set('Content-Type', clientProm.register.contentType)
      res.end(clientProm.register.metrics())
    })

    // api ---------------------------------------------------------------------
    // get all todos
    app.get('/api/todos', function (req, res) {
        // use mongoose to get all todos in the database
        log.info("GET call api todos");
        getTodos(res);
    });

    // create todo and send back all todos after creation
    app.post('/api/todos', function (req, res) {
        counterAddTodo.inc();
        log.info("POST call api todos with parameter "+req.body.text);
        // create a todo, information comes from AJAX request from Angular
        Todo.create({
            text: req.body.text,
            done: false
        }, function (err, todo) {
            if (err)
                res.send(err);

            // get and return all the todos after you create another
            getTodos(res);
        });

    });

    // delete a todo
    app.delete('/api/todos/:todo_id', function (req, res) {
        counterRemoveTodo.inc();
        log.info("DELETE call api todos with id "+req.params.todo_id);
        Todo.remove({
            _id: req.params.todo_id
        }, function (err, todo) {
            if (err)
                res.send(err);

            getTodos(res);
        });
    });

    // application -------------------------------------------------------------
    app.get('*', function (req, res) {
        res.sendFile(__dirname + '/public/index.html'); // load the single view file (angular will handle the page changes on the front-end)
    });
};
