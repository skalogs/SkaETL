<template>
  <v-container fluid grid-list-md text-xs-center>

  <v-layout row wrap>
    <v-flex d-flex xs12>

      <v-tooltip right>
        <span slot="activator">
          <v-card flat hover>
            <v-card-text class="metric-title" @click="dialogConfiguration=true">Configuration</v-card-text>
            <v-card-text class="metric-value" @click="dialogConfiguration=true">8</v-card-text>
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
            <v-card-text class="metric-value" @click="dialogProcess=true">12</v-card-text>
          </v-card>
        </span>
        <span>
          Active: {{home.numberProcessActive}}<br>
          Inactive: {{home.numberProcessDeActive}}<br>
          Error: {{home.numberProcessError }}<br>
          Init: {{home.numberProcessInit }}
        </span>
      </v-tooltip>

      <v-tooltip right>
        <span slot="activator">
          <v-card flat hover>
            <v-card-text class="metric-title" @click="dialogMetric=true">Metric</v-card-text>
            <v-card-text class="metric-value" @click="dialogMetric=true">28</v-card-text>
          </v-card>
        </span>
        <span>
          Active: {{home.numberMetricActive}}<br>
          Inactive: {{home.numberMetricDeActive}}<br>
          Error: {{home.numberMetricError }}<br>
          Init: {{home.numberMetricInit }}
        </span>
      </v-tooltip>

      <v-tooltip right>
        <span slot="activator">
          <v-card flat hover>
            <v-card-text class="metric-title" @click="dialogReferential=true">Referential</v-card-text>
            <v-card-text class="metric-value" @click="dialogReferential=true">6</v-card-text>
          </v-card>
        </span>
        <span>
          Active: {{home.numberReferentialActive}}<br>
          Inactive: {{home.numberReferentialDeActive}}<br>
          Error: {{home.numberReferentialError }}<br>
          Init: {{home.numberReferentialInit }}
        </span>
      </v-tooltip>

      <v-tooltip right>
        <span slot="activator">
          <v-card flat hover>
            <v-card-text class="metric-title" @click="dialogWorker=true">Worker</v-card-text>
            <v-card-text class="metric-value" @click="dialogWorker=true">3</v-card-text>
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
            <v-card-text class="metric-value" @click="dialogClient=true">0</v-card-text>
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

  <v-layout row>
    <v-card>
      <v-card-media>
        <!-- #todo Remove hard-coded width and URL -->
        <iframe src="http://grafana-admin.skalogs-demo.skalogs.com/d-solo/FCY8Arimz/kafka?orgId=1&panelId=4&theme=light" width="1350" height="380" frameborder="0"></iframe>
      </v-card-media>
      <v-card-actions>
        <v-btn color="primary" flat v-on:click.native="openGrafana">I want to see more...</v-btn>
      </v-card-actions>
    </v-card>
  </v-layout row>

    <v-dialog v-model="dialogMetric" fullscreen transition="dialog-bottom-transition" :overlay="false" >
       <v-card tile>
          <v-data-table v-bind:headers="headersMetric" :items="home.listStatMetric" hide-actions  >
             <template slot="items" slot-scope="props">
               <td>{{props.item.name}}</td>
               <td>{{props.item.status}}</td>
               <td>{{props.item.nbInput}}</td>
               <td>{{props.item.nbOutput}}</td>
             </template>
          </v-data-table>
         <v-card-actions>
             <v-btn color="primary" flat @click.stop="dialogMetric=false">Close</v-btn>
         </v-card-actions>
       </v-card>
     </v-dialog>
     <v-dialog v-model="dialogReferential" fullscreen transition="dialog-bottom-transition" :overlay="false" >
       <v-card tile>
          <v-data-table v-bind:headers="headersReferential" :items="home.listStatReferential" hide-actions  >
             <template slot="items" slot-scope="props">
               <td>{{props.item.name}}</td>
               <td>{{props.item.status}}</td>
               <td>{{props.item.nbInput}}</td>
               <td>{{props.item.nbOutput}}</td>
             </template>
          </v-data-table>
         <v-card-actions>
             <v-btn color="primary" flat @click.stop="dialogReferential=false">Close</v-btn>
         </v-card-actions>
       </v-card>
     </v-dialog>
     <v-dialog v-model="dialogConfiguration"  fullscreen transition="dialog-bottom-transition" :overlay="false" >
      <v-card tile>
           <v-data-table v-bind:headers="headersConfiguration" :items="home.listStatConfiguration" hide-actions  >
              <template slot="items" slot-scope="props">
                <td>{{props.item.name}}</td>
                <td>{{props.item.status}}</td>
              </template>
           </v-data-table>
         <v-card-actions>
             <v-btn color="primary" flat @click.stop="dialogConfiguration=false">Close</v-btn>
         </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="dialogWorker" fullscreen transition="dialog-bottom-transition" :overlay="false" >
      <v-card tile>
         <v-data-table v-bind:headers="headersWorker" :items="home.listStatWorker" hide-actions  >
            <template slot="items" slot-scope="props">
              <td>{{props.item.name}}</td>
              <td>{{props.item.ip}}</td>
              <td>{{props.item.nbProcess}}</td>
              <td>{{props.item.type}}</td>
            </template>
         </v-data-table>
         <v-card-actions>
             <v-btn color="primary" flat @click.stop="dialogWorker=false">Close</v-btn>
         </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="dialogProcess" fullscreen transition="dialog-bottom-transition" :overlay="false" >
      <v-card tile>
         <v-data-table v-bind:headers="headersProcess" :items="home.listStatProcess" hide-actions  >
            <template slot="items" slot-scope="props">
              <td>{{props.item.name}}</td>
              <td>{{props.item.status}}</td>
              <td>{{props.item.nbRead}}</td>
              <td>{{props.item.nbOutput}}</td>
            </template>
         </v-data-table>
         <v-card-actions>
             <v-btn color="primary" flat @click.stop="dialogProcess=false">Close</v-btn>
         </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="dialogClient" fullscreen transition="dialog-bottom-transition" :overlay="false" >
      <v-card tile>
         <v-data-table v-bind:headers="headersClient" :items="home.listStatClient" hide-actions  >
            <template slot="items" slot-scope="props">
              <td>{{props.item.hostname}}</td>
              <td>{{props.item.dateActivity}}</td>
              <td>{{props.item.env}}</td>
            </template>
         </v-data-table>
         <v-card-actions>
             <v-btn color="primary" flat @click.stop="dialogClient=false">Close</v-btn>
         </v-card-actions>
      </v-card>
    </v-dialog>

    <v-layout row wrap>
      <v-flex xs6>
        <v-card>
          <v-card-title>Consumers processes</v-card-title>

          <v-data-table v-bind:headers="headers" :items="listProcess" hide-actions hide-headers>
            <template slot="items" slot-scope="props">
              <td><v-icon color="orange">cached</v-icon></td>

                <td v-if="props.item.statusProcess == 'ERROR'" class="text-xs-left">
                  <v-badge color="red">
                    <v-icon slot="badge">error</v-icon>
                    <span>{{props.item.processDefinition.name}}</span>
                  </v-badge>
                </td>

                <td v-if="props.item.statusProcess == 'ENABLE'" class="text-xs-left">
                  <v-badge color="green">
                    <v-icon slot="badge">play_arrow</v-icon>
                    <span>{{props.item.processDefinition.name}}</span>
                  </v-badge>
                </td>

                <td v-if="props.item.statusProcess == 'INIT'" class="text-xs-left">
                  <v-badge color="blue lighten-2">
                    <v-icon slot="badge">power_settings_new</v-icon>
                    <span>{{props.item.processDefinition.name}}</span>
                  </v-badge>
                </td>

                <td v-if="props.item.statusProcess == 'DISABLE'" class="text-xs-left">
                  <v-badge color="warning">
                    <v-icon slot="badge">pause</v-icon>
                    <span>{{props.item.processDefinition.name}}</span>
                  </v-badge>
                </td>

                <td class="text-xs-center">
                    <v-flex xs12>
                       {{props.item.processDefinition.processInput.host}} {{props.item.processDefinition.processInput.port}} {{props.item.processDefinition.processInput.topic}}
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
          <v-card-title>Metric processes</v-card-title>
            <v-data-table v-bind:headers="metricHeaders" :items="listMetricProcess" hide-actions hide-headers>
              <template slot="items" slot-scope="props">
                <td><v-icon color="indigo darken-2">widgets</v-icon></td>

                <td v-if="props.item.statusProcess == 'ERROR'" class="text-xs-left">
                  <v-badge color="red darken-1">
                    <v-icon slot="badge" dark>report_problem</v-icon>
                    <span>{{props.item.processDefinition.name}}</span>
                  </v-badge>
                </td>

                <td v-if="props.item.statusProcess == 'ENABLE'" class="text-xs-left">
                  <v-badge color="green">
                    <v-icon slot="badge">play_arrow</v-icon>
                    <span>{{props.item.processDefinition.name}}</span>
                  </v-badge>
                </td>

                <td v-if="props.item.statusProcess == 'INIT'" class="text-xs-left">
                  <v-badge color="blue lighten-2">
                    <v-icon slot="badge">power_settings_new</v-icon>
                    <span>{{props.item.processDefinition.name}}</span>
                  </v-badge>
                </td>

                <td v-if="props.item.statusProcess == 'DISABLE'" class="text-xs-left">
                  <v-badge color="warning">
                    <v-icon slot="badge">pause</v-icon>
                    <span>{{props.item.processDefinition.name}}</span>
                  </v-badge>
                </td>

                <td class="text-xs-center">{{props.item.processDefinition.aggFunction}}</td>
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
</style>

<script>
  export default{
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
              { text : 'Hostname',align : 'center',value : 'hostname'},
              { text : 'Date Activity',align : 'center',value : 'dateActivity'},
              { text : 'Environment',align : 'center',value : 'env'}
           ],
           headersProcess : [
             { text : 'Name',align : 'center',value : 'name'},
             { text : 'Status',align : 'center',value : 'status'},
             { text : 'Nb Read',align : 'center',value : 'nbRead'},
             { text : 'Nb Treat',align : 'center',value : 'nbOutput'}
           ],
           headersWorker : [
             { text : 'Name',align : 'center',value : 'name'},
             { text : 'Ip',align : 'center',value : 'ip'},
             { text : 'Nb Process',align : 'center',value : 'nbProcess'},
             { text : 'Type',align : 'center',value : 'type'}
           ],
           headersMetric : [
             { text : 'Name',align : 'center',value : 'name'},
             { text : 'Status',align : 'center',value : 'status'},
             { text : 'Nb Input',align : 'center',value : 'nbInput'},
             { text : 'Nb Output',align : 'center',value : 'nbOutput'}
           ],
           headersReferential : [
             { text : 'Name',align : 'center',value : 'name'},
             { text : 'Status',align : 'center',value : 'status'},
             { text : 'Nb Process',align : 'center',value : 'nbProcess'}
           ],
           headersConfiguration : [
             { text : 'Name',align : 'center',value : 'name'},
             { text : 'Status',align : 'center',value : 'status'}
           ],
           optionsGlobal: {responsive: true,maintainAspectRatio: false,
                                   legend: {
                                       position: 'bottom',
                                       labels: {fontColor: "white",
                                                fontSize: 12
                                               }
                                   },
                                   hover: {
                                       mode: 'label'
                                   },
                                   scales: {
                                       xAxes: [{
                                               display: true,
                                               type: 'linear',
                                               scaleLabel: {
                                                   display: true,
                                                   fontStyle: 'bold'
                                               },ticks: {fontColor: "#CCC"}
                                           }],
                                       yAxes: [{
                                               display: true,
                                               ticks: {
                                                   beginAtZero: true,
                                                   steps: 10,
                                                   stepValue: 5,
                                                   fontColor: "#CCC"
                                               }
                                           }]
                                   }
                                },
           dataCharts: {"dataProcess": '',"dataMetric": '',"dataWorker": '',"dataConfiguration" :''},
           listProcess: [],
           headers: [
             { text: 'Icon',align: 'center',value: '',width: '2%'},
             { text: 'Name',align: 'left',value: 'processDefinition.name',width: '10%'},
             { text: 'Input', align: 'center',value: 'processDefinition.input',width: '8%' },
             { text: 'Output', align: 'center',value: 'processDefinition.output',width: '8%' }
           ],
           listMetricProcess: [],
           metricHeaders: [
             {text: 'Icon', align: 'center', sortable: 0, value: '', width: '4%'},
             {text: 'Name', align: 'center', value: 'processDefinition.name', width: '8%'},
             {text: 'Function', align: 'center', value: 'processDefinition.aggFunction', width: '16%'},
             {text: 'Output', align: 'center', value: 'processDefinition.processOutputs',width: '8%' }
           ]
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
         // #todo Remove the hard-coded URL
         window.open('http://grafana-admin.skalogs-demo.skalogs.com','_blank');
      },
      loadConsumerProcess() {
        this.$http.get('/process/findAll').then(response => {
            this.listProcess=response.data;
         }, response => {
           this.viewError=true;
           this.msgError = "Error during call service";
         });
      },
      loadMetricProcess() {
        this.$http.get('/metric/listProcess').then(response => {
          this.listMetricProcess = response.data;
          console.log(this.listProcess);
        }, response => {
          this.viewError = true;
          this.msgError = "Error during call service";
        });
      }
    }
  }
</script>
