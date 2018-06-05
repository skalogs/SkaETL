<template>
  <v-container fluid grid-list-md>
    <v-card>
      <v-card-title class="card-title">Map of consumer processes</v-card-title>
        <v-layout row>
          <v-switch v-model="visibles" label="consumer" color="success" value="consumer" hide-details></v-switch>
          <v-switch v-model="visibles" label="metric" color="red" value="metric" hide-details></v-switch>
          <v-spacer></v-spacer><v-spacer></v-spacer><v-spacer></v-spacer>
          <v-slider v-model="force" min=0 max=20000 thumb-label label="force" step="1000" ticks></v-slider>
        </v-layout>
      <d3-network :net-nodes="nodes" :net-links="links" :options="options" :link-cb="lcb"/>

      <v-layout row >
        <v-flex xs12 sm12 md12 >
          <v-alert v-model="viewError" xs12 sm12 md12  color="error" icon="warning" value="true" dismissible>
            {{ msgError }}
          </v-alert>
        </v-flex>
      </v-layout>
      <v-card-actions>
        <v-btn v-show="this.source =='consumer'" color="primary" flat href="/process/list">Return to consumer process list</v-btn>
        <v-btn v-show="this.source =='metric'" color="primary" flat href="/metric/list">Return to metric process list</v-btn>
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
    fill: #757575;
    font-size: .9em;
  }
  .node{
    stroke-width: 1;
    stroke: rgba(0,0,0,.2);
  }
  .link-label{
    fill: #757575;
    transform: translate(0,.9em);
    font-size: .9em;
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
           consumerNodes: [],
           consumerLinks: [],
           consumerNodes: [],
           consumerLinks: [],
           visibles: ['consumer','metric'],
           source: '',
           force: 10000
      }
    },
    mounted() {
         this.$http.get('/process/network').then(response => {
             var network = response.data;
             this.consumerNodes=network.consumerNodeList;
             this.consumerLinks=network.consumerLinksList;
             this.metricNodes=network.metricNodeList;
             this.metricLinks=network.metricLinksList;

             this.source = this.$route.query.source;
             if (this.source == "metric")
               this.visibles = ['metric'];
             else
               this.visibles = ['consumer'];

         }, response => {
           this.viewError=true;
           this.msgError = "Error during call service";
         });
    },
  	computed:{
      options(){
    	  return {
      	  force: this.force,
      	  forces:{
            X:0.2,
            Y:0.5
          },
          nodeSize: 20,
          nodeLabels: true,
          linkLabels: true,
           linkWidth: 2,
           size:{h:700}
        }
      }
    },
    watch: {
      visibles: function () {
        this.nodes=[];
        this.links=[];
        if (this.visibles.includes("metric")) {
          this.nodes = this.nodes.concat(this.metricNodes);
          this.links = this.links.concat(this.metricLinks);
          if (!this.visibles.includes("consumer")) {
            this.links = this.links.filter(link => link.name != 'stream');
          }
        }
        if (this.visibles.includes("consumer")) {
          this.nodes = this.nodes.concat(this.consumerNodes);
          this.links = this.links.concat(this.consumerLinks);
        }
        this.nodes = this.arrayUnique(this.nodes);
      }
    },
    methods: {
      lcb (link) {
        link._svgAttrs = { 'marker-end': 'url(#m-end)'}
        return link
      },
      arrayUnique(array) {
        var a = array.concat();
          for(var i=0; i<a.length; ++i) {
            for(var j=i+1; j<a.length; ++j) {
              if(a[i].name === a[j].name)
                a.splice(j--, 1);
            }
          }
        return a;
      }
    }
  }
</script>
