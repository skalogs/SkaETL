<template>
  <v-container fluid grid-list-md>
    <v-card>
      <v-card-title primary-title><h3 class="headline mb-0">Logstash configuration</h3></v-card-title>
      <v-card-text>
        <v-text-field rows=2 box textarea append-icon="file_copy" id="command" :append-icon-cb="copyClipboardCommand" name="logstashCmd" label="Command" v-model="commandLogstash"></v-text-field>
        <v-text-field rows=10 box textarea append-icon="file_copy" id="conf" :append-icon-cb="copyClipboardConf" name="logstashConf" label="Configuration" v-model="confLogstash"></v-text-field>
      </v-card-text>
      <v-card-actions>
        <v-btn color="primary" href="/configuration/list">Return to configuration list<v-icon right>list</v-icon></v-btn>
      </v-card-actions>
    </v-card>

    <v-snackbar :timeout=2000 color="success" :vertical="false" v-model="snackbar" top :multi-line="false">
      The text has been copied to the clipboard
    </v-snackbar>

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
           tab: [],
           snackbar: false
         }
   },
   mounted() {
            this.idConfiguration = this.$route.query.idConfiguration;
            this.generateLogstash();
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
        },
        copyClipboardCommand() {
          var field = document.getElementById('command');
          field.focus();
          field.select();
          document.execCommand('copy');
          this.snackbar=true;
        },
        copyClipboardConf() {
          var field = document.getElementById('conf');
          field.focus();
          field.select();
          document.execCommand('copy');
          this.snackbar=true;
        }
    }
  }
</script>
