<template>
  <v-app>
    <v-toolbar color="blue darken-3" app fixed :clipped-left="$vuetify.breakpoint.lgAndUp"
               v-on:click.native="deployMenu()">
      <v-toolbar-side-icon></v-toolbar-side-icon>
      <v-toolbar-title class="white--text">SkaETL</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn flat>{{userDefine}}</v-btn>
      <v-btn flat v-if="this.userDefine != null" v-on:click.native="logout">Logout</v-btn>
    </v-toolbar>
    <v-navigation-drawer app stateless permanent floating :mini-variant.sync="mini"
                         :clipped="$vuetify.breakpoint.lgAndUp">
      <v-list dense class="pt-0">
        <v-list-tile v-for="item in items" :key="item.title" @click="" :href="item.link">
          <v-list-tile-action>
            <v-tooltip right>
              <v-icon slot="activator" :color="item.color">{{ item.icon }}</v-icon>
              <span>{{ item.title }}</span>
            </v-tooltip>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>{{ item.title }}</v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
    </v-navigation-drawer>
    <v-content>
      <router-view></router-view>
    </v-content>
    <!-- <v-footer app></v-footer> -->

  </v-app>
</template>

<script>
  export default {
    data() {
      return {
        userDefine: '',
        mini: true,
        app: this,
        items: [
          {title: 'Home', icon: 'dashboard', color: 'blue darken-2', link: '/main/process'},
          {title: 'Logstash Configuration', icon: 'settings', color: 'green darken-2', link: '/configuration/list'},
          {title: 'Process Consumer', icon: 'cached', color: 'orange darken-2', link: '/process/list'},
          {title: 'Metric Consumer', icon: 'widgets', color: 'indigo darken-2', link: '/metric/list'},
          {title: 'Referential', icon: 'near_me', color: 'red darken-2', link: '/referential/list'},
          {title: 'Grok Pattern Simulation', icon: 'question_answer', color: 'purple darken-2', link: '/simulate/grok'},
          {title: 'Kafka Live', icon: 'call_split', color: 'blue-grey darken-2', link: '/process/live'}
        ]
      }
    },
    mounted() {
      this.userDefine = window.localStorage.getItem('userDefine');
    },
    methods: {
      deployMenu() {
        this.mini = !this.mini;
      },
      logout() {
        console.log('logout');
        var app = this;
        app.$store.state.isLoggedIn = false;
        window.localStorage.removeItem('userDefine');
        app.$router.push('/main/login');
      }
    }
  }
</script>
