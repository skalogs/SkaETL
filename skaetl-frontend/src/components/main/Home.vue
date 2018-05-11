<template>
  <v-container fluid grid-list-md>
     <v-layout row style="cardSkalogs">
              <v-flex xs2 sm2 md2 class="pa-3">
                <v-card color="teal darken-1" class="white--text">
                  <v-container fluid grid-list-lg>
                     <v-layout row>
                     <v-icon light>settings</v-icon><div class="subheading">&nbsp;Configuration</div>
                     </v-layout>
                     <v-layout row>
                         <v-flex xs6 sm6 md6 class="text-md-right caption">
                              Active  </p>
                              Inactive </p>
                              Error </p>
                              Init
                         </v-flex>
                         <v-flex xs1 sm1 md1  class="text-md-right caption ">
                              {{home.numberConfigurationActive}} </p>
                              {{home.numberConfigurationDeActive}} </p>
                              {{home.numberConfigurationError }} </p>
                              {{home.numberConfigurationInit }}
                         </v-flex>
                     </v-layout>
                  </v-container>
                  <v-card-actions>
                      <v-btn flat color="orange" v-on:click.native="dialogConfiguration=true">View</v-btn>
                  </v-card-actions>
                </v-card>
              </v-flex>
         <v-flex xs2 sm2 md2 class="pa-3">
            <v-card color="light-blue darken-3" class="white--text">
              <v-container fluid grid-list-lg>
                 <v-layout row>
                  <v-icon light>cached</v-icon><div class="subheading">&nbsp;Consumer</div>
                 </v-layout>
                 <v-layout row>
                    <v-flex xs6 sm6 md6 class="text-md-right caption">
                         Active  </p>
                         Inactive </p>
                         Error  </p>
                         Init
                    </v-flex>
                    <v-flex xs1 sm1 md1  class="text-md-right caption ">
                         {{home.numberProcessActive}} </p>
                         {{home.numberProcessDeActive}} </p>
                         {{home.numberProcessError }}</p>
                         {{home.numberProcessInit }}
                    </v-flex>
                 </v-layout>
              </v-container>
              <v-card-actions>
                  <v-btn flat color="orange" v-on:click.native="dialogProcess=true">View</v-btn>
              </v-card-actions>
            </v-card>
         </v-flex>
         <v-flex xs2 sm2 md2 class="pa-3">
            <v-card color="blue-grey darken-1" class="white--text">
              <v-container fluid grid-list-lg>
                 <v-layout row>
                  <v-icon light>widgets</v-icon><div class="subheading">&nbsp;Metric</div>
                 </v-layout>
                 <v-layout row>
                    <v-flex xs6 sm6 md6 class="text-md-right caption">
                         Active  </p>
                         Inactive </p>
                         Error  </p>
                         Init
                    </v-flex>
                    <v-flex xs1 sm1 md1  class="text-md-right caption ">
                         {{home.numberMetricActive}} </p>
                         {{home.numberMetricDeActive}} </p>
                         {{home.numberMetricError }}</p>
                         {{home.numberMetricInit }}
                    </v-flex>
                 </v-layout>
              </v-container>
              <v-card-actions>
                  <v-btn flat color="orange" v-on:click.native="dialogMetric=true">View</v-btn>
              </v-card-actions>
            </v-card>
         </v-flex>
         <v-flex xs2 sm2 md2 class="pa-3">
            <v-card color="red darken-4" class="white--text">
              <v-container fluid grid-list-lg>
                 <v-layout row>
                  <v-icon light>near_me</v-icon><div class="subheading">&nbsp;Referential</div>
                 </v-layout>
                 <v-layout row>
                    <v-flex xs6 sm6 md6 class="text-md-right caption">
                         Active  </p>
                         Inactive </p>
                         Error  </p>
                         Init
                    </v-flex>
                    <v-flex xs1 sm1 md1  class="text-md-right caption ">
                         {{home.numberReferentialActive}} </p>
                         {{home.numberReferentialDeActive}} </p>
                         {{home.numberReferentialError }}</p>
                         {{home.numberReferentialInit }}
                    </v-flex>
                 </v-layout>
              </v-container>
              <v-card-actions>
                  <v-btn flat color="orange" v-on:click.native="dialogReferential=true">View</v-btn>
              </v-card-actions>
            </v-card>
         </v-flex>
         <v-flex xs2 sm2 md2 class="pa-3">
            <v-card color="deep-purple lighten-0" class="white--text">
              <v-container fluid grid-list-lg>
                 <v-layout row>
                  <div class="subheading">Worker</div>
                 </v-layout>
                 <v-layout row>
                     <v-flex xs6 sm6 md6 class="text-md-right caption">
                          <br>
                          Process</p>
                          Metric</p>
                          Ref.</p>
                     </v-flex>
                     <v-flex xs1 sm1 md1  class="text-md-right caption ">
                          <br>
                          {{home.numberWorkerProcess}} </p>
                          {{home.numberWorkerMetric}} </p>
                          {{home.numberWorkerReferential }}
                     </v-flex>
                 </v-layout>
              </v-container>
              <v-card-actions>
                  <v-btn flat color="orange" v-on:click.native="dialogWorker=true">View</v-btn>
              </v-card-actions>
            </v-card>
         </v-flex>

         <v-flex xs2 sm2 md2 class="pa-3">
           <v-card color="cyan darken-3" class="white--text">
             <v-container fluid grid-list-lg>
                <v-layout row>
                 <div class="subheading">Client Logstash</div>
                </v-layout>
                <v-layout row>
                    <v-flex xs6 sm6 md6 class="text-md-right caption ">
                         <br>
                         All </p>
                         Prod </p>
                         Error </p>
                    </v-flex>
                    <v-flex xs1 sm1 md1  class="text-md-right caption ">
                         <br>
                         {{dataCharts.numberAllClientConfiguration}} </p>
                         {{dataCharts.numberProdClientConfiguration}} </p>
                         {{dataCharts.numberErrorClientConfiguration}} </p>
                         </p>
                    </v-flex>
                </v-layout>
             </v-container>
             <v-card-actions>
                 <v-btn flat color="orange" v-on:click.native="dialogClient=true">View</v-btn>
             </v-card-actions>
           </v-card>
         </v-flex>
     </v-layout>
     <v-layout wrap row style="cardSkalogs">
         <v-flex class="pa-3">
           <v-card color="light-blue darken-3" class="white--text" style="height : 350px; width: 550px" >
             <v-container fluid grid-list-lg>
                <v-layout row>
                 <v-icon light>cached</v-icon><div class="subheading">&nbsp;Consumer</div>
                </v-layout>
                <v-layout row>
                      <div style="height: 100px">
                          <line-chart :chart-data="dataCharts.dataProcess" :options="optionsGlobal" :width="500" :height="300"></line-chart>
                      </div>
                </v-layout>
             </v-container>
           </v-card>
         </v-flex>
         <v-flex class="pa-3">
           <v-card color="blue-grey darken-1" class="white--text" style="height : 350px; width: 550px">
             <v-container fluid grid-list-lg>
                <v-layout row>
                 <v-icon light>widgets</v-icon><div class="subheading">&nbsp;Metric</div>
                </v-layout>
                <v-layout row>
                      <div>
                          <line-chart :chart-data="dataCharts.dataMetric" :options="optionsGlobal" :width="500" :height="300" ></line-chart>
                      </div>
                </v-layout>
             </v-container>
           </v-card>
         </v-flex>
         <v-flex  class="pa-3">
           <v-card color="cyan darken-3" class="white--text" style="height : 350px; width: 550px">
             <v-container fluid grid-list-lg>
                <v-layout row>
                 <div class="subheading">Client</div>
                </v-layout>
                <v-layout row>
                      <div>
                          <line-chart :chart-data="dataCharts.dataClient" :options="optionsGlobal" :width="500" :height="300" ></line-chart>
                      </div>
                </v-layout>
             </v-container>
           </v-card>
         </v-flex>
     </v-layout>

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
  .cardSkalogs {
    height : 450px;
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
           dataCharts: {"dataProcess": '',"dataMetric": '',"dataWorker": '',"dataConfiguration" :''}
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
    },
    methods : {

    }
  }
</script>
