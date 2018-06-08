<template>
  <v-container fluid grid-list-md >
    <v-stepper v-model="confWizardStep">
          <v-stepper-header v-if="!editMode">
                <v-stepper-step step="1" v-bind:complete="confWizardStep > 1" editable>Process Name</v-stepper-step>
                <v-divider></v-divider>
                <v-stepper-step step="2" v-bind:complete="confWizardStep > 2" :editable="confWizardStep > 1">Attribute</v-stepper-step>
                <v-divider></v-divider>
                <v-stepper-step step="3" v-bind:complete="confWizardStep > 3" :editable="confWizardStep > 2">Input</v-stepper-step>
                <v-divider></v-divider>
                <v-stepper-step step="4" v-bind:complete="confWizardStep > 4" :editable="confWizardStep > 3">Topic Output</v-stepper-step>
                <v-divider></v-divider>
          </v-stepper-header>

          <v-stepper-header v-if="editMode">
                <v-stepper-step step="1" editable>Process Name</v-stepper-step>
                <v-divider></v-divider>
                <v-stepper-step step="2" editable>Attribute</v-stepper-step>
                <v-divider></v-divider>
                <v-stepper-step step="3" editable>Input</v-stepper-step>
                <v-divider></v-divider>
                <v-stepper-step step="4" editable>Topic Output</v-stepper-step>
                <v-divider></v-divider>
          </v-stepper-header>

          <v-stepper-content step="1">
          <div v-on:keyup.13="nextStep()">
            <v-card class="mb-5">
              <v-card-title><div class="headline">Choose a Logstash configuration name</div></v-card-title>
              <v-card-text>
                <v-text-field label="Name of your configuration" v-model="configurationLogstash.name" required></v-text-field>
              </v-card-text>
              <v-card-actions>
                <v-btn disabled color="primary" style="width: 120px" @click.native="previousStep()"><v-icon>navigate_before</v-icon>Previous</v-btn>
                <v-btn color="primary" style="width: 120px" @click.native="nextStep()" :disabled="!configurationLogstash.name">Next<v-icon>navigate_next</v-icon></v-btn>
              </v-card-actions>
            </v-card>
            </div>
         </v-stepper-content>

          <v-stepper-content step="2">
            <v-card class="mb-5 pa-3" @keydown.enter="nextStep()">
              <v-card-title><div class="headline">Describe your attributes</div></v-card-title>
              <v-card-text>
               <v-flex xs12 sm6 md6>
                  <v-layout row wrap>
                     <v-select label="Name of your environment" v-model="configurationLogstash.confData.env" v-bind:items="typeEnv"/>
                  </v-layout>
                  <v-layout row wrap>
                     <v-text-field label="Category of your logs" v-model="configurationLogstash.confData.category"></v-text-field>
                  </v-layout >
                  <v-layout row wrap>
                     <v-text-field label="The unique Api Key" v-model="configurationLogstash.confData.apiKey"></v-text-field>
                     <v-btn color="primary" v-on:click.native="generateApiKeyBtn()">Generate an ApiKey</v-btn>
                  </v-layout>
                  <v-layout row wrap >
                      <v-checkbox label="Custom Configuration" v-model="configurationLogstash.statusCustomConfiguration"></v-checkbox>
                  </v-layout>
                  <v-layout row >
                      <v-text-field style="width: 600px; height: 600px" v-show="configurationLogstash.statusCustomConfiguration" label="Configuration" textarea v-model="configurationLogstash.customConfiguration"></v-text-field>
                  </v-layout>
               </v-flex>
               </v-card-text>
               <v-card-actions>
                 <v-btn color="primary" style="width: 120px" @click.native="previousStep()"><v-icon>navigate_before</v-icon>Previous</v-btn>
                 <v-btn color="primary" style="width: 120px" @click.native="nextStep()" :disabled="!configurationLogstash.confData.apiKey">Next<v-icon>navigate_next</v-icon></v-btn>
                 <v-btn color="success" style="width: 150px" @click.native="saveOutput()" v-show="configurationLogstash.statusCustomConfiguration">Save&nbsp;<v-icon>save</v-icon></v-btn>
               </v-card-actions>
            </v-card>
         </v-stepper-content>


          <v-stepper-content step="3">
            <v-card class="mb-5 pa-3" @keydown.enter="nextStep()">
              <v-card-title><div class="headline">Select your inputs</div></v-card-title>
              <v-card-text>
                 <v-flex>
                   <v-flex xs12 sm4 md4>
                      <v-layout row wrap >
                         <v-select label="Input type" v-model="typeInput" v-bind:items="typeDataIn" v-on:change="actionView" required/>
                         <v-text-field label="Tag label" v-model="tag" required ></v-text-field>
                         <v-btn color="primary" v-on:click.native="addConfig">Add<v-icon>add</v-icon></v-btn>
                      </v-layout>
                      </p></p>
                    </v-flex>
                 </v-flex>
                 <v-flex>
                   <v-flex xs12 sm4 md4>
                      <v-layout row wrap v-show="viewHost">
                         <v-text-field label="Host name" v-model="host"></v-text-field>
                         <v-text-field label="Host port" v-model="port"></v-text-field>
                      </v-layout>
                      </p></p>
                    </v-flex>
                 </v-flex>
                 <v-flex>
                   <v-flex xs12 sm4 md4>
                      <v-layout row wrap v-show="viewType">
                         <v-text-field label="Type" v-model="typeForced"></v-text-field>
                         <v-text-field label="Codec"v-model="codec"></v-text-field>
                      </v-layout>
                      </p></p>
                    </v-flex>
                 </v-flex>
                 <v-flex>
                   <v-flex xs12 sm4 md4>
                      <v-layout row wrap v-show="viewTopic" >
                         <v-text-field label="Topic name" v-model="topic"></v-text-field>
                      </v-layout>
                      </p></p>
                    </v-flex>
                 </v-flex>
                 <v-flex>
                   <v-flex xs12 sm4 md4>
                      <v-layout row wrap v-show="viewPath">
                         <v-text-field label="File path" v-model="path"></v-text-field>
                      </v-layout>
                      </p></p>
                    </v-flex>
                 </v-flex>
              <v-flex>
              <v-layout row>
                     <v-flex v-for="(item,index) in configurationLogstash.input">
                        <v-chip color="orange" text-color="white" close @input="deleteItem(index)">{{item.typeInput}}-{{item.tag}}</v-chip>
                     </v-flex>
              </v-layout>
              </v-flex>
              </v-card-text>
              <v-card-actions>
                <v-btn color="primary" style="width: 120px" @click.native="previousStep()"><v-icon>navigate_before</v-icon>Previous</v-btn>
                <v-btn color="primary" style="width: 120px" @click.native="nextStep()" :disabled="configurationLogstash.input.length===0">Next<v-icon>navigate_next</v-icon></v-btn>
              </v-card-actions>
            </v-card>
          </v-stepper-content>

           <v-stepper-content step="4">
              <v-card class="mb-5 pa-3">
                <v-card-title><div class="headline">Select your outputs</div></v-card-title>
                <v-card-text>
                   <v-flex>
                     <v-flex xs6 sm6 md6>
                        <v-layout row wrap>
                           <v-text-field label="Host name" v-model="hostOutput" required></v-text-field>
                           <v-text-field label="Host port" v-model="portOutput" required></v-text-field>
                        </v-layout>
                        </p></p>
                      </v-flex>
                   </v-flex>
                   <v-flex>
                     <v-flex xs6 sm6 md6>
                        <v-layout row wrap >
                           <v-text-field label="Topic name" v-model="topicOutput" required></v-text-field>
                           <v-text-field label="Codec" v-model="codecOutput"></v-text-field>
                        </v-layout>
                        </p></p>
                      </v-flex>
                   </v-flex>
                   <v-flex>
                     <v-flex xs6 sm6 md6>
                        <v-layout row wrap >
                           <v-select v-model="listTag" label="Select a tag to apply" item-value="idCli" item-text="tag" return-object deletable-chips chips tags :items="configurationLogstash.input" required></v-select>
                        </v-layout>
                        </p></p>
                      </v-flex>
                   </v-flex>
                <v-layout row>
                       <v-flex v-for="(item,index) in configurationLogstash.output">
                          <v-chip color="orange" text-color="white" close @input="deleteOutputItem(index)">{{item.topic}}</v-chip>
                       </v-flex>
                </v-layout>
                </v-card-text>
                <v-card-actions>
                  <v-btn color="primary" style="width: 120px" @click.native="previousStep()"><v-icon>navigate_before</v-icon>Previous</v-btn>
                  <v-btn color="primary" style="width: 150px" @click.native="createOutput()" :disabled="!topicOutput">Create Output&nbsp;<v-icon>create_new_folder</v-icon></v-btn>
                  <v-btn color="success" style="width: 150px" @click.native="saveOutput()" :disabled="configurationLogstash.output.length===0">Save&nbsp;<v-icon>save</v-icon></v-btn>
                </v-card-actions>
              </v-card>
           </v-stepper-content>



    </v-stepper>
    <v-layout row wrap>
      <v-flex xs12 sm12 md12 >
        <v-alert v-model="viewError" xs12 sm12 md12  color="error" icon="warning" value="true" dismissible>
             {{ msgError }}
        </v-alert>
       </v-flex>
     </v-layout row wrap>
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
        confWizardStep: '',
        typeInput: '',
        host: '',
        port: '',
        typeForced: '',
        codec: '',
        topic: '',
        path: '',
        tag: '',
        listTag: [],
        hostOutput: 'kafka.kafka',
        portOutput: '9092',
        topicOutput: 'processtopic',
        codecOutput: '',
        configurationLogstash: {"idConfiguration":"","name":"","input":[],"output": [], "confData": {"env":"","apiKey":"","category":""}, "statusCustomConfiguration" : false, "customConfiguration": ""},
        viewError: false,
        msgError: '',
        typeDataIn : ["KAFKA","UDP","TCP","FILE","BEATS","User Advanced"],
        typeEnv: ["dev","uat","int","prod"],
        viewTopic: false,
        viewPath: false,
        viewType: false,
        viewHost: false,
        editMode: false
      }
   },
   mounted() {
      this.configurationLogstash.confData.apiKey = this.generateApiKey();
      this.idConfiguration = this.$route.query.idConfiguration;
      if(this.idConfiguration && this.idConfiguration !='undefined' ){
           this.editMode = true;
           this.$http.get('/configuration/getConfiguration', {params: {idConfiguration:this.idConfiguration}}).then(response => {
              this.configurationLogstash=response.data;
           }, response => {
             this.viewError=true;
             this.msgError = "Error during call service";
           });
      }
   },
   methods: {
      generateApiKeyBtn(){
        this.configurationLogstash.confData.apiKey = this.generateApiKey();
      },
      generateApiKey(){
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
      },
      nextStep() {
        this.confWizardStep++;
      },
      previousStep() {
        this.confWizardStep--;
      },
      deleteItem(index){
         if (index > -1) {
              this.configurationLogstash.input.splice(index, 1);
         }
      },
      deleteOutputItem(index){
         if (index > -1) {
              this.configurationLogstash.output.splice(index, 1);
         }
      },
      createOutput(){
         //filter
         var listTagResult = this.listTag.map(x => x.tag);
         this.configurationLogstash.output.push({"host":this.hostOutput,"port":this.portOutput,"topic":this.topicOutput,"codec":this.codecOutput,"listTag": listTagResult});
         this.hostOutput='kafka.kafka';
         this.portOutput='9092';
         this.topicOutput='processtopic';
         this.codecOutput='';
         this.listTag= [];
      },
      addConfig(){
         if (this.tag && this.tag != ''){
           this.configurationLogstash.input.push({"idCli": this.generateApiKey(), "host":this.host,"port":this.port,"topic":this.topic,"codec":this.codec,"typeForced":this.typeForced,"path":this.path,"typeInput": this.typeInput, "tag": this.tag});
           this.typeInput='';
           this.host='';
           this.port='';
           this.typeForced='';
           this.codec='';
           this.topic='';
           this.path='';
           this.tag='';
         }else{
            this.viewError=true;
            this.msgError = "You must Tag your input configuration.";
         }
      },
      saveOutput(){
          if(this.idConfiguration){
            this.$http.post('/configuration/editConfiguration', this.configurationLogstash).then(response => {
               this.$router.push('/configuration/list');
            }, response => {
               this.viewError=true;
               this.msgError = "Error during call service";
            });
          }else{
            this.$http.post('/configuration/createConfiguration', this.configurationLogstash).then(response => {
               this.$router.push('/configuration/list');
            }, response => {
               this.viewError=true;
               this.msgError = "Error during call service";
            });
          }
      },
      actionView(value){
          if(value== "User Advanced"){
            this.viewHost=false;
            this.viewTopic=false;
            this.viewPath=false;
            this.viewType=false;
          }else if(value== "KAFKA"){
            this.viewTopic=true;
            this.viewPath=false;
            this.viewType=false;
            this.viewHost=true;
          }else if(value == "FILE"){
            this.viewTopic=false;
            this.viewPath=true;
            this.viewHost=false;
            this.viewType=true;
          }else{
            this.viewTopic=false;
            this.viewPath=false;
            this.viewType=true;
            this.viewHost=true;
          }
      }
   }
  }
</script>
