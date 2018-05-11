const request = require('request');

const urlBackend = process.env.BACKEND ? process.env.BACKEND : 'http://localhost:8090';

function getService(req, cb) {
    var headers = {
        'Content-Type': 'application/json'
    }
    var originalUrl = req.originalUrl;
    var get_options = {
        url: urlBackend + originalUrl,
        method: 'GET'
    }
    request.get(get_options, function (err, response, body) {
        if (err) {
            cb(err.message, null);
        } else {
            cb(null, body);
        }
    });
}

function postService(req, cb) {
    var headers = {
        'Content-Type': 'application/json'
    }
    var originalUrl = req.originalUrl;
    var post_options = {
            url: urlBackend + originalUrl,
            method: 'POST'
    }
    if(JSON.stringify(req.body) != '{}'){
      post_options.json= req.body;
    }
    console.log(JSON.stringify(post_options));
    request.post(post_options, function (err, response, body) {
        if (err) {
            cb(err.message, null);
        } else {
            cb(null, body);
        }
    });
}


function deleteService(req, cb) {
    var headers = {
      'Content-Type': 'application/json'
    }
    var originalUrl = req.originalUrl;
    var delete_options = {
      url: urlBackend + originalUrl,
      method: 'DELETE'
    }
    if(JSON.stringify(req.body) != '{}'){
      delete_options .json= req.body;
    }
    console.log(JSON.stringify(delete_options ));
    request.delete(delete_options , function (err, response, body) {
      if (err) {
        cb(err.message, null);
      } else {
        cb(null, body);
      }
    });
}


module.exports = {
    getService,
    postService,
    deleteService
}
