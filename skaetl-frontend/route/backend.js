const serviceApi = require('../service/serviceApi');

module.exports = function setupRoutes(app) {

    app.get('*', function (req, res, next) {
        serviceApi.getService(req, function (err, result) {
            if (err) {
                res.type('application/json').status(500).send(err);
            } else {
                res.type('application/json').send(result);
            }
        });
    });

    app.post('*', function (req, res, next) {
        serviceApi.postService(req, function (err, result) {
            if (err) {
                res.type('application/json').status(500).send(err);
            } else {
                res.type('application/json').send(result);
            }
        });
    });

    app.delete('*', function (req, res, next) {
      serviceApi.deleteService(req, function (err, result) {
        if (err) {
          res.type('application/json').status(500).send(err);
        } else {
          res.type('application/json').send(result);
        }
      });
    });
}
