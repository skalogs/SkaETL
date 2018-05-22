var merge = require('webpack-merge')
var prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  GF_ROOT_URL: '"http://grafana-admin.skalogs-demo.skalogs.com/"'
})
