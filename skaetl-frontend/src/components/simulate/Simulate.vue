<template>
  <v-container fluid grid-list-md >
      <v-layout row  >
         <v-flex xs6 sm6 md6 >
           <v-select label="Type" v-model="choiceSimulation" v-bind:items="typeSimulation" v-on:change="actionView"/>
         </v-flex>
      </v-layout>
      <v-layout row >
            <v-text-field v-show="viewText" name="Raw Data" label="Raw Data" textarea v-model="textRawData"></v-text-field>
      </v-layout>
      <v-layout row>
          <v-flex xs12 sm12 md12 v-show="viewKafka">
              <v-text-field label="Host" v-model="hostInput" required></v-text-field>
              <v-text-field label="Port" v-model="portInput" required></v-text-field>
              <v-text-field label="Topic" v-model="topicInput" required></v-text-field>
          </v-flex>
          <v-flex xs12 sm12 md12 v-show="viewKafka">
              <v-text-field label="Polling Time (ms)" v-model="pollingTimeInput" required type="number"></v-text-field>
              <v-text-field label="Max records" v-model="maxRecordsInput" required type="number"></v-text-field>
          </v-flex>
      </v-layout>
      <v-layout row>
          <v-chip color="blue-grey lighten-3" small>{{messageActive}}</v-chip>
          <v-btn color="primary" v-on:click.native="captureData()">Launch Simulation</v-btn>
      </v-layout>
      <v-flex v-for="itemCapture in listCapture" >
               <v-layout row class="pb-3 mb-3">
                   <v-flex xs1 sm1 md1>
                       <v-subheader>Status Treatment</v-subheader>
                       <v-subheader>Raw Data</v-subheader>
                       <v-subheader>Transformed</v-subheader>
                   </v-flex>
                   <v-flex xs11 sm11 md11>
                       <v-subheader>{{itemCapture.message}}</v-subheader>
                       <v-subheader>{{itemCapture.value}}</v-subheader>
                       <v-subheader>{{itemCapture.jsonValue}}</v-subheader>
                   </v-flex>
               </v-layout>
      </v-flex>
      <v-layout v-show="viewText" row class="pb-3 mb-3 borderbox">
           <v-flex xs1 sm1 md1>
               <v-subheader>Status Treatment</v-subheader>
               <v-subheader>Raw Data</v-subheader>
               <v-subheader>Transformed</v-subheader>
           </v-flex>
           <v-flex xs11 sm11 md11>
               <v-subheader>{{itemText.message}}</v-subheader>
               <v-subheader>{{itemText.value}}</v-subheader>
               <v-subheader>{{itemText.jsonValue}}</v-subheader>
           </v-flex>
       </v-layout>

    <v-layout row wrap>
      <v-flex xs12 sm12 md12 >
        <v-alert v-model="viewError" xs12 sm12 md12  color="error" icon="warning" value="true" dismissible>
             {{ msgError }}
        </v-alert>
        <v-alert v-model="viewMessageCapture" xs12 sm12 md12  color="info" icon="info" value="true" dismissible>
            {{ messageCapture }}
        </v-alert>
      </v-flex>

    </v-layout>
  </v-container>

</template>

<style scoped>
.borderbox {
    border-style: solid;
    border-width: 1px;
}
</style>

<script>
  export default{
   data () {
         return {
           hostInput: '',
           portInput: '',
           topicInput: '',
           pollingTimeInput: '2000',
           maxRecordsInput: '3',
           idProcess: '',
           process: '',
           msgError: '',
           viewMessageCapture: false,
           messageCapture: '',
           viewError: false,
           listCapture: [],
           messageActive: 'Simulation not active',
           simulationActive: false,
           choiceSimulation: '',
           typeSimulation: ["Kafka","Text"],
           viewKafka: false,
           textRawData : '',
           viewText: false,
           itemText: ''
         }
   },
   mounted() {
      this.idProcess = this.$route.query.idProcess;
      this.$http.get('/process/findProcess', {params: {idProcess: this.idProcess}}).then(response => {
         this.process=response.data;
         this.hostInput= this.process.processInput.host;
         this.portInput= this.process.processInput.port;
         this.topicInput= this.process.processInput.topicInput;
      }, response => {
         this.viewError=true;
         this.msgError = "Error during call service";
      });
   },
   methods: {
        actionView(target){
            if(target === "Kafka"){
               this.viewKafka=true;
               this.viewText=false;
            }else if(target === "Text"){
               this.viewKafka=false;
               this.viewText=true;
            }else if(target === "File"){
               this.viewKafka=false;
               this.viewText=false;
            }else{
               this.viewKafka=false;
               this.viewText=false;
            }
        },
        launchSimulate(){
             this.$http.post('/simulate/launchSimulate', this.process).then(response => {
                this.simulationActive=true
                this.messageActive= 'Simulation Running';
                this.launchCaptureData();
             }, response => {
                this.viewError=true;
                this.msgError = "Error during call service";
             });
        },
        captureData(){
             if(!this.simulationActive && this.viewKafka){
                this.launchSimulate();
             }else{
                this.launchCaptureData();
             }
        },
        launchCaptureData(){
            if(this.viewKafka){
              this.launchCaptureKafka();
            }else if(this.viewText){
              this.launchCaptureText();
            }else {
              console.log('no call');
            }
        },
        launchCaptureKafka(){
             var payload = {"bootStrapServers" : this.hostInput+':'+this.portInput, "maxPollRecords" : this.maxRecordsInput, "pollingTime" : this.pollingTimeInput};
             this.$http.post('/simulate/capture', payload).then(response => {
                this.listCapture = response.data;
                if(response.data.length === 0){
                   this.viewMessageCapture= true;
                   this.messageCapture= 'No result';
                }
             }, response => {
                this.viewError=true;
                this.msgError = "Error during call service";
             });
        },
        launchCaptureText(){
             var payload = {"textSubmit" : this.textRawData, "processConsumer": this.process};
             this.$http.post('/simulate/captureFromText', payload).then(response => {
                this.itemText = response.data;
                if(!response.data){
                   this.viewMessageCapture= true;
                   this.messageCapture= 'No result';
                }
             }, response => {
                this.viewError=true;
                this.msgError = "Error during call service";
             });
        }
    }
  }
</script>
