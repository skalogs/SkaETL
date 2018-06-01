<template>
  <v-container fluid grid-list-md >
      <v-layout row>
          <v-flex xs12 sm12 md12>
              <v-text-field label="Host" v-model="hostInput" required></v-text-field>
              <v-text-field label="Windows Time (ms)" v-model="windowTimeInput" required type="number"></v-text-field>
              <v-text-field label="Deserializer value" v-model="deserializer" required></v-text-field>
          </v-flex>
          <v-flex xs12 sm12 md12>
              <v-text-field label="Port" v-model="portInput" required></v-text-field>
              <v-text-field label="Max records" v-model="maxRecordsInput" required type="number"></v-text-field>
          </v-flex>
          <v-flex xs12 sm12 md12>
              <v-text-field label="Topic" v-model="topicInput" required></v-text-field>
              <v-select label="Offset" v-model="offsetInput" v-bind:items="typeOffset" required/>
          </v-flex>
      </v-layout>
      <v-layout row>
          <v-btn color="primary" v-on:click.native="launchCaptureKafka()">Live Topic</v-btn>
          <v-btn color="red" v-on:click.native="reset()">Reset</v-btn>
      </v-layout>
      <v-layout row v-show="loadingValue">
              <v-progress-linear :indeterminate="true"></v-progress-linear>
      </v-layout>
      <v-layout row v-show="!loadingValue">
        <tree-view :data="listCapture" :options="{maxDepth: 1,rootObjectKey: 'data'}"></tree-view>
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
           viewMessageCapture: false,
           loadingValue: false,
           messageCapture: '',
           msgError: '',
           viewError: false,
           hostInput: '',
           portInput: '',
           topicInput: '',
           offsetInput: '',
           windowTimeInput: '2000',
           maxRecordsInput: '3',
           hostInput: '',
           portInput: '',
           topicInput: '',
           deserializer : 'org.apache.kafka.common.serialization.StringDeserializer',
           listCapture: [],
           typeOffset: ["latest", "earliest","last"]
         }
   },
   mounted() {
      this.listCapture= [];
      this.topicInput = this.$route.query.topic;
      this.hostInput = this.$route.query.hostInput;
      this.portInput = this.$route.query.portInput;
      this.offsetInput = this.$route.query.offsetInput;
   },
   methods: {
        reset(){
            this.listCapture= [];
        },
        launchCaptureKafka(){
             this.reset();
             this.loadingValue= true;
             var payload = {"topic" : this.topicInput, "bootStrapServers" : this.hostInput+':'+this.portInput, "maxRecords" : this.maxRecordsInput, "windowTime" : this.windowTimeInput, "offset" : this.offsetInput, "deserializer": this.deserializer};
             this.$http.post('/simulate/raw/captureRaw', payload).then(response => {
                this.listCapture= [];
                var res = response.data;
                for (var i=0;i<res.length;i++){
                      var itemString = res[i];
                      var itemJson = JSON.parse(itemString);
                      this.listCapture.push(itemJson);
                }
                if(response.data.length === 0){
                   this.viewMessageCapture= true;
                   this.messageCapture= 'No result';
                }
                this.loadingValue= false;
             }, response => {
                this.viewError=true;
                this.msgError = "Error during call service";
             });
        }
    }
  }
</script>
