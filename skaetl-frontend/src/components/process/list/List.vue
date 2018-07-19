<template>
 <v-container fluid grid-list-md>
    <v-layout row wrap>
      <v-card xs12 sm12 md12>
        <v-card-title>
          <v-btn color="primary" v-on:click.native="newConfig">Create Consumer</v-btn>
          <v-btn :disabled="listProcess.length > 0 ? false : true" color="orange" v-on:click.native="visualize">Visualise<v-icon right>wifi</v-icon></v-btn>
          <v-tooltip right>
            <v-btn :disabled="listProcess.length > 0 ? false : true" slot="activator" flat v-on:click.native="refreshAction" icon color="blue lighten-2">
              <v-icon>refresh</v-icon>
            </v-btn>
            <span>Refresh the list</span>
          </v-tooltip>
          <v-spacer></v-spacer>
          <v-text-field
            v-model="search"
            append-icon="search"
            label="Search"
            single-line
            hide-details
          ></v-text-field>
        </v-card-title>
        <v-data-table v-bind:headers="headers" :items="listProcess" :search="search" :hide-actions="listProcess.length > 5 ? false : true">
          <template slot="items" slot-scope="props">
              <td>
                 <v-menu bottom left>
                    <v-btn slot="activator" icon>
                       <v-icon v-if="props.item.statusProcess == 'DEGRADED'" color="red">warning</v-icon>
                       <v-icon v-if="props.item.statusProcess == 'ERROR'" color="red">error_outline</v-icon>
                       <v-icon v-if="props.item.statusProcess == 'DISABLE' || props.item.statusProcess == 'INIT'" color="orange">pause_circle_filled</v-icon>
                       <v-icon v-if="props.item.statusProcess == 'ENABLE'" color="green">play_circle_filled</v-icon>
                    </v-btn>
                    <v-list>
                      <v-list-tile>
                        <v-list-tile-title style="height: 40px" >
                              <v-btn v-if="props.item.statusProcess == 'DEGRADED'" color="purple">{{props.item.statusProcess}}</v-btn>
                              <v-btn v-if="props.item.statusProcess == 'ERROR'" color="red">{{props.item.statusProcess}}</v-btn>
                              <v-btn v-if="props.item.statusProcess == 'DISABLE' || props.item.statusProcess == 'INIT'" color="orange">{{props.item.statusProcess}}</v-btn>
                              <v-btn v-if="props.item.statusProcess == 'ENABLE'" color="green">{{props.item.statusProcess}}</v-btn>
                        </v-list-tile-title>
                      </v-list-tile>
                      <v-list-tile v-on:click.native="editProcess(props.item.id)">
                        <v-list-tile-title class="justify-center layout px-0">Edit</v-list-tile-title>
                      </v-list-tile>
                      <v-list-tile v-on:click.native="liveProcess(props.item.id)">
                        <v-list-tile-title class="justify-center layout px-0">Live</v-list-tile-title>
                      </v-list-tile>
                      <v-list-tile v-on:click.native="nextProcess(props.item.id)">
                        <v-list-tile-title class="justify-center layout px-0">Action</v-list-tile-title>
                      </v-list-tile>
                      <v-list-tile :disabled="props.item.statusProcess == 'DISABLE' || props.item.statusProcess == 'INIT'" v-on:click.native="deactivateProcess(props.item.id)">
                        <v-list-tile-title class="justify-center layout px-0">Deactivate</v-list-tile-title>
                      </v-list-tile>
                      <v-list-tile  :disabled="props.item.statusProcess == 'ENABLE' || props.item.statusProcess == 'ERROR' || props.item.statusProcess == 'DEGRADED'" v-on:click.native="activateProcess(props.item.id)">
                        <v-list-tile-title class="justify-center layout px-0">Activate</v-list-tile-title>
                      </v-list-tile>
                      <v-list-tile v-on:click.native="deleteProcess(props.item.id)">
                        <v-list-tile-title class="justify-center layout px-0">Delete</v-list-tile-title>
                      </v-list-tile>
                    </v-list>
                 </v-menu>
              </td>
              <td class="text-xs subheading">{{props.item.processDefinition.name}}</td>
              <td class="text-xs-center">
                  <v-flex xs12>
                     <v-chip color="purple lighten-2" small>{{props.item.processDefinition.processInput.host}}:{{props.item.processDefinition.processInput.port}}({{props.item.processDefinition.processInput.topicInput}})</v-chip>
                  </v-flex>
              </td>
              <td class="text-xs-center">
                <v-flex  class="pa-0 ma-0" xs12 sm12 md12 v-for="parseritem in props.item.processDefinition.processParser">
                  <v-flex class="pa-0 ma-0">
                     <v-chip color="blue-grey lighten-3" small>{{parseritem.typeParser}}</v-chip>
                  </v-flex>
                </v-flex>
              </td>
              <td class="text-xs-center">
                <v-flex  class="pa-0 ma-0" xs12 sm12 md12 v-for="transformationitem in props.item.processDefinition.processTransformation">
                  <v-flex class="pa-0 ma-0">
                     <v-chip color="blue-grey lighten-3" small>{{formatTransformation(transformationitem)}}</v-chip>
                  </v-flex>
                </v-flex>
              </td>
              <td class="text-xs-center">
                <v-flex  class="pa-0 ma-0" xs12 sm12 md12 v-for="validationitem in props.item.processDefinition.processValidation">
                  <v-flex class="pa-0 ma-0">
                     <v-chip color="blue-grey lighten-3" small>{{formatValidation(validationitem)}}</v-chip>
                  </v-flex>
                </v-flex>
              </td>

              <td class="text-xs-center">
                <v-flex xs12 sm12 md12 v-for="filteritem in props.item.processDefinition.processFilter">
                  <v-flex xs10>
                     <v-chip color="deep-orange lighten-3" small>{{filteritem.name}}</v-chip>
                  </v-flex>
                </v-flex>
              </td>
              <td class="text-xs-center">
                <v-flex  class="pa-0 ma-0" xs12 sm12 md12 v-for="outputitem in props.item.processDefinition.processOutput">
                  <v-flex class="pa-0 ma-0">
                     <v-chip color="blue-grey lighten-3" small>{{outputitem.typeOutput}}</v-chip>
                  </v-flex>
                </v-flex>
              </td>

          </template>
        </v-data-table>
      </v-card>
    </v-layout>
    <v-layout row wrap>
      <v-flex xs12 sm12 md12 >
        <v-alert v-model="viewError" xs12 sm12 md12  color="error" icon="warning" value="true" dismissible>
             {{ msgError }}
        </v-alert>
       </v-flex>
    </v-layout>

    <v-dialog v-model="dialogLive" max-width="560px">
      <v-card>
        <v-card-title primary-title><h3>Select your Kafka live</h3></v-card-title>
        <v-card-actions>
          <v-btn color="primary" v-on:click.native="launchCaptureKafka()">Live After Parsing<v-icon right>call_split</v-icon></v-btn>
          <v-btn color="primary" v-on:click.native="launchCaptureKafkaAfterTransformation()">Live After Process<v-icon right>call_split</v-icon></v-btn>
          <v-btn color="error" @click.stop="dialogLive=false">Close<v-icon right>close</v-icon></v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

 </v-container>
</template>


<script>
  export default{
    data () {
         return {
           search: '',
           listProcess: [],
           idProcess: '',
           input: {},
           uiCreate: '',
           msgError: '',
           dialogLive: false,
           viewErrorDialog: false,
           msgErrorDialog: '',
           viewMessageCaptureDialog: false,
           messageCaptureDialog: '',
           viewError: false,
           selectedToCheckBox : false,
           headers: [
             { text: 'Action',align: 'center',sortable: 0, width: '4%'},
             { text: 'Name',align: 'left',value: 'processDefinition.name',width: '10%'},
             { text: 'Input', align: 'center',value: 'processDefinition.input',width: '8%' },
             { text: 'Parser',align: 'center', value: 'processDefinition.processParser', width: '8%' },
             { text: 'Transformation',align: 'center', value: 'processDefinition.transformation', width: '15%' },
             { text: 'Validation',align: 'center', value: 'processDefinition.validation', width: '15%' },
             { text: 'Filter',align: 'center', value: 'processDefinition.filter', width: '15%' },
             { text: 'Output', align: 'center',value: 'processDefinition.output',width: '8%' }
           ],
           process : {"processInput" : {"host":"", "topicInput":"", "port":""}, "name":"", "idProcess": ""},
           listCapture: [],
           idProcessDialog: ''
         }
    },
    mounted() {
        this.$http.get('/process/findAll').then(response => {
            this.listProcess=response.data;
         }, response => {
           this.viewError=true;
           this.msgError = "Error during call service";
         });
    },
    methods: {
        visualize(){
          this.$router.push('/process/network?source=consumer');
        },
        launchCaptureKafka(){
             this.$router.push('/process/live?topic='+this.process.idProcess+'parsedprocess&hostInput='+this.process.processInput.host+'&portInput='+this.process.processInput.port+'&offsetInput=latest');
        },
        launchCaptureKafkaAfterTransformation(){
             this.$router.push('/process/live?topic='+this.process.idProcess+'treatprocess&hostInput='+this.process.processInput.host+'&portInput='+this.process.processInput.port+'&offsetInput=latest');
        },
        liveProcess(idProcess){
            this.$http.get('/process/findProcess', {params: {idProcess: idProcess}}).then(response => {
               this.process=response.data;
               this.idProcessDialog=idProcess;
               this.dialogLive= true;
            }, response => {
               this.viewError=true;
               this.msgError = "Error during call service";
            });
        },
        deactivateProcess(idProcess){
          this.$http.get('/process/deactivate', {params: {idProcess: idProcess}}).then(response => {
             this.refreshAction();
          }, response => {
             this.viewError=true;
             this.msgError = "Error during call service";
          });
        },
        activateProcess(idProcess){
          this.$http.get('/process/activate', {params: {idProcess: idProcess}}).then(response => {
             this.refreshAction();
          }, response => {
             this.viewError=true;
             this.msgError = "Error during call service";
          });
        },
        refreshAction(){
          this.$http.get('/process/findAll').then(response => {
              this.listProcess=response.data;
          }, response => {
             this.viewError=true;
             this.msgError = "Error during call service";
          });
        },
        nextProcess(idProcessSelect){
          this.$router.push('/process/action/view?idProcess='+idProcessSelect);
        },
        newConfig(){
           this.$router.push('/process/edit');
        },
        formatValidation(validationitem) {
          switch (validationitem.typeValidation) {
            case "MANDATORY_FIELD":
              return validationitem.typeValidation + " " + validationitem.parameterValidation.mandatory;
            case "BLACK_LIST_FIELD":
              return validationitem.typeValidation + " " + validationitem.parameterValidation.blackList.map(element => element.key).join(", ");
            case "MAX_FIELD":
              return validationitem.typeValidation + " " + validationitem.parameterValidation.maxFields;
            case "MAX_MESSAGE_SIZE":
              return validationitem.typeValidation + " " + validationitem.parameterValidation.maxMessageSize;
            case "FIELD_EXIST":
              return validationitem.typeValidation + " " + validationitem.parameterValidation.fieldExist;
            case "FORMAT_DATE":
              return validationitem.typeValidation;
          }
        },
        formatTransformation(transformationItem) {
            if(transformationItem.typeTransformation == "ADD_FIELD" || transformationItem.typeTransformation == "RENAME_FIELD" ){
              return transformationItem.typeTransformation + " on " + transformationItem.parameterTransformation.composeField.key;
            }else{
               return transformationItem.typeTransformation + " on " + transformationItem.parameterTransformation.keyField;
            }
        },
        deleteProcess(idProcess) {
          this.$http.delete('/process/deleteProcess', {params: {idProcess: idProcess}}).then(response => {
             this.refreshAction();
          }, response => {
             this.viewError=true;
             this.msgError = "Error during call service";
          });
        },
        editProcess(idProcess){
          this.$router.push('/process/edit?idProcess='+idProcess);
        }
    }
  }
</script>
