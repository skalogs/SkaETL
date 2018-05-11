import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

Vue.filter('escape', function (value) {
  if(typeof value !== "undefined")
    return value.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
});
