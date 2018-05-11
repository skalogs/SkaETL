<template>
  <v-container fluid grid-list-md >
   <v-layout row wrap>
        <v-flex xs12 sm12 md12>
         <v-btn color="primary" v-on:click.native="generateLogstash">Generate Configuration Logstash</v-btn>

        </v-flex>
   </v-layout>
  <v-tabs v-model="active" color="primary" slider-color="yellow">
          <v-tab :key="1" ripple>
            Configuration
          </v-tab>
          <v-tab :key="2" ripple>
            Command
          </v-tab>
          <v-tab-item :key="1">
              <v-flex xs12 sm12 md12>
               <v-text-field name="logstashConf" label="Configuration" textarea v-model="confLogstash"></v-text-field>
              </v-flex>
          </v-tab-item>
          <v-tab-item :key="2" >
              <v-flex xs12 sm12 md12>
               <v-text-field name="logstashCmd" label="Command" textarea v-model="commandLogstash"></v-text-field>
              </v-flex>
          </v-tab-item>
  </v-tabs>
  <v-layout row wrap>
      <v-flex xs12 sm12 md12 >
        <v-alert v-model="viewError" xs12 sm12 md12  color="error" icon="warning" value="true" dismissible>
             {{ msgError }}
        </v-alert>
       </v-flex>
  </v-layout row wrap>
  </v-container>
</template>


<script>
  export default{
   data () {
         return {
           idConfiguration : '',
           commandLogstash: '',
           confLogstash: '',
           msgError: '',
           viewError: false,
           fluxLogstash: '',
           tab: []
         }
   },
   mounted() {
            this.idConfiguration = this.$route.query.idConfiguration;
   },
   methods: {
        generateLogstash(){
          this.$http.get('/configuration/generate', {params: {idConfiguration: this.idConfiguration}}).then(response => {
              this.commandLogstash=response.data.commandLogstash;
              this.confLogstash=response.data.confLogstash;
           }, response => {
               this.viewError=true;
             this.msgError = "Error during call service";
           });

      }
    }
  }
</script>
