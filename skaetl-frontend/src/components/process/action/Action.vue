<template>
  <v-container fluid grid-list-md >
   <v-layout row>
      <v-flex class="pa-3">
         <v-flex xs12 sm12 md12>
          <p>
            <h2> Your Process is  {{consumerState.processDefinition.name}} with status {{consumerState.statusProcess}} </h2>
          </p>
         <v-layout row>
             <v-subheader v-if="consumerState.registryWorkers.length == 0">No Worker Running</v-subheader>
             <v-subheader v-if="consumerState.registryWorkers.length > 0">Worker Running</v-subheader>
         </v-layout row>
         <v-flex v-for="itemConsumerState in consumerState.registryWorkers">
             <v-btn color="teal" small>{{itemConsumerState}}</v-btn>
         </v-flex>
         </p></p></p>
         <v-layout row class="pa-3">
              <v-btn color="primary" v-on:click.native="scaleUp()">Scale up</v-btn>
              <v-btn color="primary" v-on:click.native="scaleDown()">Scale down</v-btn>
              <v-btn color="warning" v-on:click.native="simulate()">Simulation</v-btn>
         </v-layout>
         <p/>
         </v-flex>
      </v-flex>
   </v-layout>
   <v-layout row>
      <v-card class="pa-3">
          <v-card-media> <img height="200px" width="400px" src="../../../assets/kibana.png" /></v-card-media>
          <v-card-title primary-title>
              <div>
                <div>Explore your data with Kibana</div>
              </div>
          </v-card-title>
          <v-card-actions>
              <v-btn flat color="orange">Explore</v-btn>
          </v-card-actions>
      </v-card>
      <v-card class="pa-3">
          <v-card-media> <img height="200px" width="400px" src="../../../assets/grafana.png" /></v-card-media>
          <v-card-title primary-title>
              <div>
                <div>Status Process with Grafana</div>
              </div>
          </v-card-title>
          <v-card-actions>
              <v-btn flat color="orange">Explore</v-btn>
          </v-card-actions>
      </v-card>
      <v-card class="pa-3">
          <v-card-media> <img height="200px" width="400px" src="../../../assets/referential.png" /></v-card-media>
          <v-card-title primary-title>
              <div>
                <div>Referential</div>
              </div>
          </v-card-title>
          <v-card-actions>
              <v-btn flat color="orange"  v-on:click.native="manageReferential()">Manage</v-btn>
              <v-btn flat color="orange" >Explore</v-btn>
          </v-card-actions>
      </v-card>
   </v-layout>
   <v-layout row >
      <v-flex xs12 sm12 md12 >
        <v-alert v-model="viewError" xs12 sm12 md12  color="error" icon="warning" value="true" dismissible>
             {{ msgError }}
        </v-alert>
        <v-alert v-model="viewStatus" xs12 sm12 md12  color="success" icon="success" value="true" dismissible>
             {{ msgStatus }}
        </v-alert>
       </v-flex>
     </v-layout row >
    </v-container>

</template>


<script>
  export default{
   data () {
         return {
           idProcess: '',
           consumerState: {"statusProcess":"",
                           "processDefinition":{"idProcess":"","name":"","timestamp":""},
                           "registryWorkers":[]
                          },
           msgStatus: '',
           viewStatus: false,
           msgError: '',
           viewError: false
         }
    },
    mounted() {
          this.idProcess = this.$route.query.idProcess;
          this.find();
    },
    methods: {
        manageReferential(){
            this.$router.push('/referential');
        },
        openES(){
            window.open('http://kibana.skalogs-demo.skalogs.com','_blank');
        },
        openGrafana(){
            window.open('http://grafana-admin.skalogs-demo.skalogs.com','_blank');
        },
        live(){
            this.$router.push('/process/live?idProcess='+this.idProcess);
        },
        simulate(){
            this.$router.push('/simulate/view?idProcess='+this.idProcess);
        },
        active(idProcess){
          this.$http.get('/process/active', {params: {idProcess: idProcess}}).then(response => {
             this.viewStatus = true;
             this.msgStatus = 'Activated !';
          }, response => {
             this.viewError=true;
             this.msgError = "Error during call service";
          });
        },
        deactive(idProcess){
          this.$http.get('/process/disable', {params: {idProcess: idProcess}}).then(response => {
             this.find();
          }, response => {
             this.viewError=true;
             this.msgError = "Error during call service";
          });
        },
        scaleUp(){
          this.$http.get('/process/scaleup', {params: {idProcess: this.idProcess}}).then(response => {
            this.viewStatus = true;
            this.msgStatus = 'Scaled up !';
          }, response => {
            this.viewError=true;
            this.msgError = "Error during call service";
          });
        },
        scaleDown(){
          this.$http.get('/process/scaledown', {params: {idProcess: this.idProcess}}).then(response => {
            this.viewStatus = true;
            this.msgStatus = 'Scaled down !';
          }, response => {
            this.viewError=true;
            this.msgError = "Error during call service";
          });
        },
        find(){
           this.$http.get('/process/findConsumerState', {params: {idProcess: this.idProcess}}).then(response => {
               this.consumerState=response.data;
               console.log(this.consumerState.processDefinition.name);
            }, response => {
               this.viewError=true;
               this.msgError = "Error during call service";
            });
        }
    }
  }
</script>
