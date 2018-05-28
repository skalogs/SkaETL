<template>
  <v-container fluid grid-list-xs text-xs-center pa-0>
  <v-layout row wrap>
    <v-flex d-flex xs12>
      <v-tooltip right>
        <span slot="activator">
          <v-card flat hover>
            <v-card-text class="metric-title" @click="dialogConfiguration=true">Configuration</v-card-text>
            <v-card-text class="metric-value" @click="dialogConfiguration=true">{{home.numberConfigurationTotal}}</v-card-text>
          </v-card>
        </span>
        <span>
          Active: {{home.numberConfigurationActive}}<br>
          Inactive: {{home.numberConfigurationDeActive}}<br>
          Error: {{home.numberConfigurationError }}<br>
          Init: {{home.numberConfigurationInit }}
        </span>
      </v-tooltip>

      <v-tooltip right>
        <span slot="activator">
          <v-card flat hover>
            <v-card-text class="metric-title" @click="dialogProcess=true">Consumer</v-card-text>
            <v-card-text class="metric-value" @click="dialogProcess=true">{{home.numberProcessTotal}}</v-card-text>
          </v-card>
        </span>
        <span>
          Active: {{home.numberProcessActive}}<br>
          Inactive: {{home.numberProcessDeActive}}<br>
          Error: {{home.numberProcessError }}<br>
          Degraded: {{home.numberProcessDegraded }}<br>
          Init: {{home.numberProcessInit }}<br>
          Creation: {{home.numberProcessCreation }}
        </span>
      </v-tooltip>

      <v-tooltip right>
        <span slot="activator">
          <v-card flat hover>
            <v-card-text class="metric-title" @click="dialogMetric=true">Metric</v-card-text>
            <v-card-text class="metric-value" @click="dialogMetric=true">{{home.numberMetricTotal}}</v-card-text>
          </v-card>
        </span>
        <span>
          Active: {{home.numberMetricActive}}<br>
          Inactive: {{home.numberMetricDeActive}}<br>
          Error: {{home.numberMetricError }}<br>
          Degraded: {{home.numberMetricDegraded }}<br>
          Init: {{home.numberMetricInit }}<br>
          Creation: {{home.numberMetricCreation }}
        </span>
      </v-tooltip>

      <v-tooltip right>
        <span slot="activator">
          <v-card flat hover>
            <v-card-text class="metric-title" @click="dialogReferential=true">Referential</v-card-text>
            <v-card-text class="metric-value" @click="dialogReferential=true">{{home.numberReferentialTotal}}</v-card-text>
          </v-card>
        </span>
        <span>
          Active: {{home.numberReferentialActive}}<br>
          Inactive: {{home.numberReferentialDeActive}}<br>
          Error: {{home.numberReferentialError }}<br>
          Degraded: {{home.numberReferentialDegraded }}<br>
          Init: {{home.numberReferentialInit }}<br>
          Creation: {{home.numberReferentialCreation }}
        </span>
      </v-tooltip>

      <v-tooltip right>
        <span slot="activator">
          <v-card flat hover>
            <v-card-text class="metric-title" @click="dialogWorker=true">Worker</v-card-text>
            <v-card-text class="metric-value" @click="dialogWorker=true">{{home.numberWorkerTotal}}</v-card-text>
          </v-card>
        </span>
        <span>
          Process: {{home.numberWorkerProcess}}<br>
          Metric: {{home.numberWorkerMetric}}<br>
          Referential: {{home.numberWorkerReferential }}<br>
        </span>
      </v-tooltip>

      <v-tooltip right>
        <span slot="activator">
          <v-card flat hover>
            <v-card-text class="metric-title" @click="dialogClient=true">Client Logstash</v-card-text>
            <v-card-text class="metric-value" @click="dialogClient=true">{{dataCharts.numberAllClientConfiguration}}</v-card-text>
          </v-card>
        </span>
        <span>
          All: {{dataCharts.numberAllClientConfiguration}}<br>
          Production: {{dataCharts.numberProdClientConfiguration}}<br>
          Error: {{dataCharts.numberErrorClientConfiguration}}<br>
        </span>
      </v-tooltip>

    </v-flex>
  </v-layout row>

  <v-layout wrap row>
    <v-card width="100%">
      <v-card-title class="table-title">Consumer processes traffic</v-card-title>
      <v-card-media>
        <div class="chart-container">
          <line-chart :chart-data="dataCharts.dataProcess" :options="optionsGlobal"/>
        </div>
      </v-card-media>
      <v-card-actions>
        <v-btn color="primary" flat v-on:click.native="openGrafana">I want to see more...</v-btn>
      </v-card-actions>
    </v-card>
  </v-layout>

    <v-dialog v-model="dialogMetric" fullscreen transition="dialog-bottom-transition" :overlay="false" @keydown.esc="dialogMetric=false" @keydown.enter="dialogMetric=false">
       <v-card tile>
          <v-data-table v-bind:headers="headersMetric" :items="home.listStatMetric" hide-actions  >
             <template slot="items" slot-scope="props">
               <td><b>{{props.item.name}}</b></td>
               <td class="text-xs-center">{{props.item.status}}</td>
               <td class="text-xs-center">{{props.item.nbInput}}</td>
               <td class="text-xs-center">{{props.item.nbOutput}}</td>
             </template>
          </v-data-table>
         <v-card-actions>
             <v-btn color="primary" flat @click.stop="dialogMetric=false">Close this window</v-btn>
         </v-card-actions>
       </v-card>
     </v-dialog>
     <v-dialog v-model="dialogReferential" fullscreen transition="dialog-bottom-transition" :overlay="false" @keydown.esc="dialogReferential=false" @keydown.enter="dialogReferential=false">
       <v-card tile>
          <v-data-table v-bind:headers="headersReferential" :items="home.listStatReferential" hide-actions  >
             <template slot="items" slot-scope="props">
               <td><b>{{props.item.name}}</b></td>
               <td class="text-xs-center">{{props.item.status}}</td>
               <td class="text-xs-center">{{props.item.nbInput}}</td>
               <td class="text-xs-center">{{props.item.nbOutput}}</td>
             </template>
          </v-data-table>
         <v-card-actions>
             <v-btn color="primary" flat @click.stop="dialogReferential=false">Close this windows</v-btn>
         </v-card-actions>
       </v-card>
     </v-dialog>
     <v-dialog v-model="dialogConfiguration"  fullscreen transition="dialog-bottom-transition" :overlay="false" @keydown.esc="dialogConfiguration=false" @keydown.enter="dialogConfiguration=false">
      <v-card tile>
           <v-data-table v-bind:headers="headersConfiguration" :items="home.listStatConfiguration" hide-actions  >
              <template slot="items" slot-scope="props">
                <td><b>{{props.item.name}}</b></td>
                <td class="text-xs-center">{{props.item.status}}</td>
              </template>
           </v-data-table>
         <v-card-actions>
             <v-btn color="primary" flat @click.stop="dialogConfiguration=false">Close this window</v-btn>
         </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="dialogWorker" fullscreen transition="dialog-bottom-transition" :overlay="false" @keydown.esc="dialogWorker=false" @keydown.enter="dialogWorker=false">
      <v-card tile>
         <v-data-table v-bind:headers="headersWorker" :items="home.listStatWorker" hide-actions  >
            <template slot="items" slot-scope="props">
              <td><b>{{props.item.name}}</b></td>
              <td class="text-xs-center">{{props.item.ip}}</td>
              <td class="text-xs-center">{{props.item.nbProcess}}</td>
              <td class="text-xs-center">{{props.item.type}}</td>
            </template>
         </v-data-table>
         <v-card-actions>
             <v-btn color="primary" flat @click.stop="dialogWorker=false">Close this window</v-btn>
         </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="dialogProcess" fullscreen transition="dialog-bottom-transition" :overlay="false" @keydown.esc="dialogProcess=false" @keydown.enter="dialogProcess=false">
      <v-card tile>
         <v-data-table v-bind:headers="headersProcess" :items="home.listStatProcess" hide-actions>
            <template slot="items" slot-scope="props">
              <td><b>{{props.item.name}}</b></td>
              <td class="text-xs-center">{{props.item.status}}</td>
              <td class="text-xs-center">{{props.item.nbRead}}</td>
              <td class="text-xs-center">{{props.item.nbOutput}}</td>
            </template>
         </v-data-table>
         <v-card-actions>
             <v-btn color="primary" flat @click.stop="dialogProcess=false">Close this window</v-btn>
         </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="dialogClient" fullscreen transition="dialog-bottom-transition" :overlay="false" @keydown.esc="dialogClient=false" @keydown.enter="dialogClient=false">
      <v-card tile>
         <v-data-table v-bind:headers="headersClient" :items="home.listStatClient" hide-actions  >
            <template slot="items" slot-scope="props">
              <td><b>{{props.item.hostname}}</b></td>
              <td class="text-xs-center">{{props.item.dateActivity}}</td>
              <td class="text-xs-center">{{props.item.env}}</td>
            </template>
         </v-data-table>
         <v-card-actions>
             <v-btn color="primary" flat @click.stop="dialogClient=false">Close this window</v-btn>
         </v-card-actions>
      </v-card>
    </v-dialog>

    <v-layout row wrap>
      <v-flex xs6>
        <v-card>
          <v-card-title class="table-title">Consumer processes</v-card-title>

          <v-data-table :items="listProcess" hide-actions hide-headers>
            <template slot="items" slot-scope="props">
              <td width="1%"><v-icon>cached</v-icon></td>

                <td v-if="props.item.statusProcess == 'ERROR' || props.item.statusProcess == 'DEGRADED'" class="text-xs-left">
                  <v-badge color="red">
                    <v-icon slot="badge">error</v-icon>
                    <span class="process-name">{{props.item.processDefinition.name}}</span>
                  </v-badge>
                </td>

                <td v-if="props.item.statusProcess == 'ENABLE'" class="text-xs-left">
                  <v-badge color="green">
                    <v-icon slot="badge">play_arrow</v-icon>
                    <span class="process-name">{{props.item.processDefinition.name}}</span>
                  </v-badge>
                </td>

                <td v-if="props.item.statusProcess == 'INIT' || props.item.statusProcess == 'CREATION'" class="text-xs-left">
                  <v-badge color="blue lighten-2">
                    <v-icon slot="badge">power_settings_new</v-icon>
                    <span class="process-name">{{props.item.processDefinition.name}}</span>
                  </v-badge>
                </td>

                <td v-if="props.item.statusProcess == 'DISABLE'" class="text-xs-left">
                  <v-badge color="warning">
                    <v-icon slot="badge">pause</v-icon>
                    <span class="process-name">{{props.item.processDefinition.name}}</span>
                  </v-badge>
                </td>

                <td class="text-xs-center">
                    <v-flex xs12>
                       {{props.item.processDefinition.processInput.host}}:{{props.item.processDefinition.processInput.port}}({{props.item.processDefinition.processInput.topicInput}})
                    </v-flex>
                </td>
                <td class="text-xs-center">
                  <v-flex  class="pa-0 ma-0" xs12 sm12 md12 v-for="outputitem in props.item.processDefinition.processOutput">
                    <v-flex class="pa-0 ma-0">
                       {{outputitem.typeOutput}}
                    </v-flex>
                  </v-flex>
                </td>
            </template>
          </v-data-table>
          <v-card-actions>
            <v-btn color="primary" flat href="/process/list">See all consumer processes</v-btn>
          </v-card-actions>
        </v-card>
      </v-flex>

      <v-flex xs6>
        <v-card>
          <v-card-title class="table-title">Metric processes</v-card-title>
            <v-data-table :items="listMetricProcess" hide-actions hide-headers>
              <template slot="items" slot-scope="props">
                <td width="1%"><v-icon>widgets</v-icon></td>

                <td v-if="props.item.statusProcess == 'ERROR' || props.item.statusProcess == 'DEGRADED'" class="text-xs-left">
                  <v-badge color="red darken-1">
                    <v-icon slot="badge" dark>report_problem</v-icon>
                    <span class="process-name">{{props.item.processDefinition.name}}</span>
                  </v-badge>
                </td>

                <td v-if="props.item.statusProcess == 'ENABLE'" class="text-xs-left">
                  <v-badge color="green">
                    <v-icon slot="badge">play_arrow</v-icon>
                    <span class="process-name">{{props.item.processDefinition.name}}</span>
                  </v-badge>
                </td>

                <td v-if="props.item.statusProcess == 'INIT' || props.item.statusProcess == 'CREATION'" class="text-xs-left">
                  <v-badge color="blue lighten-2">
                    <v-icon slot="badge">power_settings_new</v-icon>
                    <span class="process-name">{{props.item.processDefinition.name}}</span>
                  </v-badge>
                </td>

                <td v-if="props.item.statusProcess == 'DISABLE'" class="text-xs-left">
                  <v-badge color="warning">
                    <v-icon slot="badge">pause</v-icon>
                    <span class="process-name">{{props.item.processDefinition.name}}</span>
                  </v-badge>
                </td>

                <td class="text-xs-center">{{props.item.processDefinition.aggFunction}}</td>

                <td>
                  <v-flex  class="pa-0 ma-0" xs12 sm12 md12 v-for="source in props.item.processDefinition.sourceProcessConsumers">
                    <v-flex class="pa-0 ma-0">
                      {{ getProcessName(source) }}
                    </v-flex>
                  </v-flex>
                </td>

                <td class="text-xs-center">
                  <v-flex  class="pa-0 ma-0" xs12 sm12 md12 v-for="outputitem in props.item.processDefinition.processOutputs">
                    <v-flex class="pa-0 ma-0">
                      {{outputitem.typeOutput}}
                    </v-flex>
                  </v-flex>
                </td>
              </template>
            </v-data-table>
          <v-card-actions>
            <v-btn color="primary" flat href="/metric/list">See all metric processes</v-btn>
          </v-card-actions>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<style>
  .metric-title {
    color: #757575;
    text-align: center;
    padding: 0;
    font-size: 14px;
    font-weight: bold;
  }
  .metric-value {
    color: #1E88E5;
    text-align: center;
    padding: 0;
    font-size: 50px;
    font-weight: bold;
  }
  .table-title {
    color: #757575;
    font-size: 22px;
    font-weight: bold;
  }
  .process-name {
    white-space: nowrap;
    font-weight: bold;
  }
  .chart-container {
    position: relative;
    margin: auto;
    width: 90vw;
  }
</style>

<script>
  import LineChart from './LineChart.js'
    export default{
      components: {
        LineChart
      },
    data () {
         return {
           dialogClient : false,
           dialogProcess : false,
           dialogWorker : false,
           dialogConfiguration : false,
           dialogMetric : false,
           dialogReferential : false,
           viewError : false,
           msgError : '',
           home : '',
           headersClient : [
              { text : 'Client hostname',align : 'center',value : 'hostname'},
              { text : 'Activity date',align : 'center',value : 'dateActivity'},
              { text : 'Client environment',align : 'center',value : 'env'}
           ],
           headersProcess : [
             { text : 'Process name',align : 'center',value : 'name'},
             { text : 'Process status',align : 'center',value : 'status'},
             { text : 'Number of read',align : 'center',value : 'nbRead'},
             { text : 'Number of processing', align : 'center',value : 'nbOutput'}
           ],
           headersWorker : [
             { text : 'Worker name',align : 'center',value : 'name'},
             { text : 'Worker IP address',align : 'center',value : 'ip'},
             { text : 'Number of processing',align : 'center',value : 'nbProcess'},
             { text : 'Worker type',align : 'center',value : 'type'}
           ],
           headersMetric : [
             { text : 'Process name',align : 'center',value : 'name'},
             { text : 'Process status',align : 'center',value : 'status'},
             { text : 'Nb Input todo',align : 'center',value : 'nbInput'},
             { text : 'Number of processing',align : 'center',value : 'todo'}
           ],
           headersReferential : [
             { text : 'Referential name',align : 'center',value : 'name'},
             { text : 'Referential status',align : 'center',value : 'status'},
             { text : 'Number of processing',align : 'center',value : 'nbProcess'}
           ],
           headersConfiguration : [
             { text : 'Configuration name',align : 'center',value : 'name'},
             { text : 'Configuration status',align : 'center',value : 'status'}
           ],
           optionsGlobal: {maintainAspectRatio: false,
                                   legend: {
                                       position: 'bottom',
                                       labels: {fontColor: "black",
                                                fontSize: 11,
                                                boxWidth: 15}
                                   },
                                   hover: {mode: 'label'},
                                   scales: {
                                       xAxes: [{
                                               display: true,
                                               type: 'linear',
                                               scaleLabel: {
                                                   display: true,
                                                   fontStyle: 'bold'
                                               },
                                               ticks: {fontColor: "black"},
                                               gridLines: {display: true}
                                           }],
                                       yAxes: [{
                                               display: true,
                                               ticks: {
                                                   beginAtZero: true,
                                                   steps: 10,
                                                   stepValue: 5,
                                                   fontColor: "black"
                                               },
                                               scaleLabel: {
                                                 display: true,
                                                 labelString: "Message processed",
                                                 fontColor: "black"
                                               },
                                               gridLines: {display: true}
                                           }]
                                   }
                                },
           dataCharts: {"dataProcess": ''},
           listProcess: [],
           listMetricProcess: [],
           metricProcess: new Map()
         }
    },
    mounted() {
         this.$http.get('/home/fetch').then(response => {
           this.home=response.data;
         }, response => {
           this.viewError=true;
           this.msgError = "Error during call service";
         });
         this.$http.get('/home/dataCapture').then(response => {
           this.dataCharts=response.data;
         }, response => {
           this.viewError=true;
           this.msgError = "Error during call service";
         });
         this.loadConsumerProcess();
         this.loadMetricProcess();
    },

    methods : {
      openGrafana(){
         window.open(process.env.GF_ROOT_URL,'_blank');
      },
      loadConsumerProcess() {
        this.$http.get('/process/findAll').then(response => {
            this.listProcess=response.data;
            this.listProcess = this.listProcess.slice(0,3);
         }, response => {
           this.viewError=true;
           this.msgError = "Error during call service";
         });
      },
      loadMetricProcess() {
        this.$http.get('/metric/listProcess').then(response => {
          this.listMetricProcess = response.data;
          this.listMetricProcess = this.listMetricProcess.slice(0,3);
        }, response => {
          this.viewError = true;
          this.msgError = "Error during call service";
        });
      },
      getProcessName(id){
        if (this.metricProcess != undefined && this.metricProcess.get(id) != undefined) {
            return this.metricProcess.get(id);
        } else {
          this.$http.get('/referential/find', {params: {idReferential: id}}).then(response => {
            this.process = response.data;
            name = this.process.name;
            this.metricProcess.set(id, name);
            return name;
          }, response => {
            this.viewError=true;
            this.msgError = "Error during call service";
        });
        }
      }
    }
  }
</script>
