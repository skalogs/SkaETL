<template>
  <v-container fluid grid-list-md >
    <v-card>
      <v-card-title class="card-title">Map of consumer processes</v-card-title>

      <d3-network :net-nodes="nodes" :net-links="links" :options="options" :link-cb="lcb"/>

      <v-layout row >
        <v-flex xs12 sm12 md12 >
          <v-alert v-model="viewError" xs12 sm12 md12  color="error" icon="warning" value="true" dismissible>
            {{ msgError }}
          </v-alert>
        </v-flex>
      </v-layout>
      <v-card-actions>
        <v-btn color="primary" flat href="/process/list">Return to consumer process list</v-btn>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<style src="vue-d3-network/dist/vue-d3-network.css"></style>
<style>
  .card-title {
    color: #757575;
    font-size: 22px;
    font-weight: bold;
  }
  #m-end path, #m-start{
    fill: rgba(18, 120, 98, 0.8);
  }
  .node-label{
    font-size: 12;
  }
  .link-label{
    fill: black;
    transform: translate(0,4);
    font-size: 11;
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
            force: 10000,
            nodeSize: 20,
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
             console.log(this.links);
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
