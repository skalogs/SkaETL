<template>
  <v-container fluid grid-list-md >
     <d3-network :net-nodes="nodes" :net-links="links" :options="options" :link-cb="lcb"/>
     <v-layout row >
        <v-flex xs12 sm12 md12 >
          <v-alert v-model="viewError" xs12 sm12 md12  color="error" icon="warning" value="true" dismissible>
               {{ msgError }}
          </v-alert>
        </v-flex>
     </v-layout>
  </v-container>

</template>
<style>
@import url('https://fonts.googleapis.com/css?family=PT+Sans');
body{
  font-family: 'PT Sans', sans-serif;
  background-color: #eee;
}
.title{
  position:absolute;
  text-align: center;
  left: 2em;
}
h1,a{
  color: #1aad8d;
  text-decoration: none;
}

ul.menu {
  list-style: none;
  position: absolute;
  z-index: 100;
  min-width: 20em;
  text-align: left;
}
ul.menu li{
  margin-top: 1em;
  position: relative;
}

#m-end path, #m-start{
  fill: rgba(18, 120, 98, 0.8);
}
</style>

<script>
  import D3Network from 'vue-d3-network';

  export default{
    components: {
      D3Network
    },
    data () {
         return {
           viewError : false,
           msgError : '',
           nodes: [],
           links: [],
           options:
           {
            force: 3000,
            nodeSize: 15,
            nodeLabels: true,
            linkLabels: true,
            linkWidth: 2,
           }
      }
    },
    mounted() {
         this.$http.get('/process/network').then(response => {
             var network = response.data;
             this.nodes=network.nodeList;
             this.links=network.linksList;
         }, response => {
           this.viewError=true;
           this.msgError = "Error during call service";
         });
    },
    methods: {
      lcb (link) {
        link._svgAttrs = { 'marker-end': 'url(#m-end)',
                         'marker-start': 'url(#m-start)'}
        return link
      }
    }
  }
</script>
