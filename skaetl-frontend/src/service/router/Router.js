import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)


let router = new VueRouter({
    mode: 'history',
    routes: [
      {
        path: '/process/network',
        component: require('../../components/process/list/Network.vue'),
        name: 'network',
        meta: {
          requiresAuth: true
        }
      },
      {
        path: '/main/process',
        component: require('../../components/main/Home.vue'),
        name: 'main',
        meta: {
          requiresAuth: true
        }
      },
      {
        path: '/main/process/new',
        component: require('../../components/main/NewHome.vue'),
        name: 'main',
        meta: {
          requiresAuth: true
        }
      },
      {
        path: '/configuration/list',
        component: require('../../components/configuration/List.vue'),
        name: 'listConf',
        meta: {
          requiresAuth: true
        }
      },
      {
        path: '/configuration/add',
        component: require('../../components/configuration/Configuration.vue'),
        name: 'createConf',
        meta: {
          requiresAuth: true
        }
      },
      {
        path: '/generate/logstash',
        component: require('../../components/configuration/Logstash.vue'),
        name: 'generateLogstashConf',
        meta: {
          requiresAuth: true
        }
      },
      {
        path: '/grok/view',
        component: require('../../components/grok/View.vue'),
        name: 'grok',
        meta: {
          requiresAuth: true
        }
      },
      {
        path: '/grok/add',
        component: require('../../components/grok/Add.vue'),
        name: 'grokAdd',
        meta: {
          requiresAuth: true
        }
      },
      {
        path: '/process/list',
        component: require('../../components/process/list/List.vue'),
        name: 'processlist',
        meta: {
          requiresAuth: true
        }
      },
      {
        path: '/process/edit',
        component: require('../../components/process/Process.vue'),
        name: 'processedit',
        meta: {
          requiresAuth: true
        }
      },
      {
        path: '/process/action/view',
        component: require('../../components/process/action/Action.vue'),
        name: 'processactionview',
        meta: {
          requiresAuth: true
        }
      },
      {
        path: '/metric/list',
        component: require('../../components/metric/List.vue'),
        name: 'listMetric',
        meta: {
          requiresAuth: true
        }
      },
      {
        path: '/metric/add',
        component: require('../../components/metric/Metric.vue'),
        name: 'createMetric',
        meta: {
          requiresAuth: true
        }
      },
      {
        path: '/simulate/view',
        component: require('../../components/simulate/Simulate.vue'),
        name: 'simulateview',
        meta: {
          requiresAuth: true
        }
      },
      {
        path: '/simulate/grok',
        component: require('../../components/simulate/Grok.vue'),
        name: 'simulateviewgrok',
        meta: {
          requiresAuth: true
        }
      },
      {
        path: '/referential/list',
        component: require('../../components/referential/List.vue'),
        name: 'listReferential',
        meta: {
          requiresAuth: true
        }
      },
      {
        path: '/referential/add',
        component: require('../../components/referential/Referential.vue'),
        name: 'createReferential',
        meta: {
          requiresAuth: true
        }
      },
      {
        path: '/main/login',
        component: require('../../components/main/Login.vue'),
        name: 'login'
      },
      {
        path: '/main/oops',
        component: require('../../components/main/Oops.vue'),
        name: 'oops',
        meta: {
          requiresAuth: true
        }
      },
      {
        path: '/process/live',
        component: require('../../components/capture/View.vue'),
        name: 'live',
        meta: {
          requiresAuth: true
        }
      },
      {
        path: '*',
        redirect: '/main/process'
      }
    ]
  }
)

export default router
