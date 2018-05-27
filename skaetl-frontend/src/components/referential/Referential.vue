<template>
  <v-container fluid grid-list-md >
    <v-stepper v-model="referentialWizardStep">
          <v-stepper-header v-if="!editMode">
                <v-stepper-step step="1" v-bind:complete="referentialWizardStep > 1" editable>Process Name</v-stepper-step>
                <v-divider></v-divider>
                <v-stepper-step step="2" v-bind:complete="referentialWizardStep > 2" :editable="referentialWizardStep > 1">Referential Key Name</v-stepper-step>
                <v-divider></v-divider>
                <v-stepper-step step="3" v-bind:complete="referentialWizardStep > 3" :editable="referentialWizardStep > 2">Select Consumer Process</v-stepper-step>
                <v-divider></v-divider>
                <v-stepper-step step="4" v-bind:complete="referentialWizardStep > 4" :editable="referentialWizardStep > 3">Add Entry</v-stepper-step>
                <v-divider></v-divider>
                <v-stepper-step step="5" v-bind:complete="referentialWizardStep > 5" :editable="referentialWizardStep > 4">Extract Meta-data</v-stepper-step>
          </v-stepper-header>

          <v-stepper-header v-if="editMode">
                <v-stepper-step step="1" editable>Process Name</v-stepper-step>
                <v-divider></v-divider>
                <v-stepper-step step="2" editable>Referential Key Name</v-stepper-step>
                <v-divider></v-divider>
                <v-stepper-step step="3" editable>Select Consumer Process</v-stepper-step>
                <v-divider></v-divider>
                <v-stepper-step step="4" editable>Add Entry</v-stepper-step>
                <v-divider></v-divider>
                <v-stepper-step step="5" editable>Extract Meta-data</v-stepper-step>
          </v-stepper-header>

          <v-stepper-content step="1">
            <v-card class="mb-5" >
              <v-card-title><div class="headline">Choose a referential process name</div></v-card-title>
              <v-card-text>
                <v-text-field label="Name of your process" v-model="itemToEdit.name" required></v-text-field>
              </v-card-text>
              <v-card-actions>
                <v-btn disabled color="primary" style="width: 120px" @click.native="previousStep()"><v-icon>navigate_before</v-icon>Previous</v-btn>
                <v-btn color="primary" style="width: 120px" @click.native="nextStep()" :disabled="!itemToEdit.name">Next<v-icon>navigate_next</v-icon></v-btn>
              </v-card-actions>
            </v-card>
          </v-stepper-content>

          <v-stepper-content step="2">
            <v-card class="mb-5">
              <v-card-title><div class="headline">Choose a key name</div></v-card-title>
              <v-card-text>
                <v-text-field label="Name of your key" v-model="itemToEdit.referentialKey" required></v-text-field>
              </v-card-text>
              <v-card-actions>
                <v-btn color="primary" style="width: 120px" @click.native="previousStep()"><v-icon>navigate_before</v-icon>Previous</v-btn>
                <v-btn color="primary" style="width: 120px" @click.native="nextStep()" :disabled="!itemToEdit.referentialKey">Next<v-icon>navigate_next</v-icon></v-btn>
              </v-card-actions>
            </v-card>
          </v-stepper-content>

          <v-stepper-content step="3">
            <v-card class="mb-5">
              <v-card-title><div class="headline">Select the consumer processes sources</div></v-card-title>
              <v-card-text>
                <v-select v-model="listSelected" label="Select your processes" item-value="id" item-text="processDefinition.name" return-object deletable-chips chips tags :items="listProcess" required></v-select>
              </v-card-text>
              <v-card-actions>
                <v-btn color="primary" style="width: 120px" @click.native="previousStep()"><v-icon>navigate_before</v-icon>Previous</v-btn>
                <v-btn color="primary" style="width: 120px" @click.native="nextStep()" :disabled="listSelected.length==0">Next<v-icon>navigate_next</v-icon></v-btn>
              </v-card-actions>
            </v-card>
          </v-stepper-content>

          <v-stepper-content step="4">
            <v-card class="mb-5">
              <v-card-title><div class="headline">Select the entries</div></v-card-title>
              <v-card-text>
              <v-layout row>
                 <v-flex xs4 sm4 md4>
                    <v-text-field label="Name of your entry" v-model="newEntry" required></v-text-field>
                  </v-flex>
                    <v-btn color="primary" v-on:click.native="addKeys()" :disabled="!newEntry">Add<v-icon>add</v-icon></v-btn>
              </v-layout>
              <v-layout row>
                   <v-flex xs4 sm4 md4>
                      <v-flex v-for="item in itemToEdit.listAssociatedKeys">
                         <v-chip color="orange" text-color="white" close @input="deleteItem(item)">{{item}}</v-chip>
                      </v-flex>
                   </v-flex>
                   <v-flex xs1 sm1 md1>
                         <v-icon large color="blue" v-show="itemToEdit.name!=''">arrow_forward</v-icon>
                   </v-flex>
                   <v-flex xs8 sm8 md8>
                         <v-btn flat color="blue">{{itemToEdit.referentialKey}}</v-btn>
                   </v-flex>
              </v-layout>
              </v-card-text>
              <v-card-actions>
                <v-btn color="primary" style="width: 120px" @click.native="previousStep()"><v-icon>navigate_before</v-icon>Previous</v-btn>
                <v-btn color="primary" style="width: 120px" @click.native="nextStep()" :disabled="itemToEdit.listAssociatedKeys.length==0">Next<v-icon>navigate_next</v-icon></v-btn>
              </v-card-actions>
            </v-card>
          </v-stepper-content>

          <v-stepper-content step="5">
            <v-card class="mb-5">
              <v-card-title><div class="headline">Select the meta-data to extract</div></v-card-title>
              <v-card-text>
                <v-layout row>
                  <v-flex xs4 sm4 md4>
                    <v-text-field label="Name of your Metadata" v-model="newMetadata"></v-text-field>
                  </v-flex>
                  <v-btn color="primary" v-on:click.native="addMetadata()" >Add<v-icon>add</v-icon></v-btn>
                </v-layout>
                <v-layout row>
                   <v-flex xs2 sm2 md2>
                     <v-layout row>
                        <v-flex v-for="item in itemToEdit.listMetadata">
                           <v-chip color="orange" text-color="white" close @input="deleteMetadata(item)">{{item}}</v-chip>
                        </v-flex>
                     </v-layout>
                   </v-flex>
                </v-layout>
                <v-layout row>
                  <v-flex xs6 sm6 md6>
                    <v-layout row>
                      <v-checkbox label="Validity event" v-model="itemToEdit.isValidationTimeAllField"></v-checkbox>
                      <v-text-field v-show="itemToEdit.isValidationTimeAllField" label="time (sec)" v-model="itemToEdit.timeValidationInSec"></v-text-field>
                    </v-layout>
                  </v-flex>
                </v-layout>
                <v-layout row>
                  <v-flex xs6 sm6 md6>
                    <v-layout row>
                      <v-checkbox label="Validity field" v-model="itemToEdit.isValidationTimeField"></v-checkbox>
                      <v-text-field v-show="itemToEdit.isValidationTimeField" label="time (sec)" v-model="itemToEdit.timeValidationInSec"></v-text-field>
                      <v-text-field v-show="itemToEdit.isValidationTimeField" label="field" v-model="itemToEdit.fieldChangeValidation"></v-text-field>
                    </v-layout>
                  </v-flex>
                </v-layout>
                <v-layout row>
                  <v-flex xs6 sm6 md6>
                    <v-layout row>
                      <v-checkbox label="Notification on change event" v-model="itemToEdit.isNotificationChange"></v-checkbox>
                      <v-text-field v-show="itemToEdit.isNotificationChange" label="field" v-model="itemToEdit.fieldChangeNotification"></v-text-field>
                    </v-layout>
                  </v-flex>
                </v-layout>
              </v-card-text>
              <v-card-actions>
                <v-btn color="primary" style="width: 120px" @click.native="previousStep()"><v-icon>navigate_before</v-icon>Previous</v-btn>
                <v-btn color="success" style="width: 120px" @click.native="updateReferential()">Save&nbsp;<v-icon>save</v-icon></v-btn>
              </v-card-actions>
            </v-card>
          </v-stepper-content>

    </v-stepper>
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
        referentialWizardStep: '',
        idReferential: '',
        newMetadata: '',
        newEntry: '',
        itemToEdit: {"idReferential":"",
                     "listAssociatedKeys":[],
                     "name":"",
                     "referentialKey":"",
                     "listIdProcessConsumer":[],
                     "listMetadata":[],
                     "isNotificationChange": false,
                     "fieldChangeNotification":"",
                     "timeValidationInSec": 0,
                     "isValidationTimeAllField": false,
                     "isValidationTimeField": false,
                     "fieldChangeValidation": ""
                    },
        viewError: false,
        msgError: '',
        listProcess: [],
        listSelected: [],
        headers: [
                   { text: 'Action', align: 'center',value: '',width: '10%' },
                   { text: 'Name', align: 'center',value: 'name',width: '10%' },
                   { text: 'Data Referential', align: 'center',value: 'valueKey',width: '10%' },
                   { text: 'Keys',align: 'center',value: 'listKeys', width: '80%'}
                 ],
        editMode: false
      }
   },
   mounted() {
      this.$http.get('/process/findAll').then(response => {
         this.listProcess=response.data;
         console.log(this.listProcess.length + " process(es) has been found");
         this.refreshSelected();
      }, response => {
         this.viewError=true;
         this.msgError = "Error during call service";
      });
      this.idReferential = this.$route.query.idReferential;
      if(this.idReferential){
          this.editReferential(this.idReferential);
          this.editMode = true;
      } else {
        this.$http.get('/referential/init').then(response => {
          console.log(response.data);
          this.itemToEdit=response.data;
        }, response => {
          this.viewError=true;
          this.msgError = "Error during call service";
        });
      }
   },
   methods: {
      nextStep() {
        this.referentialWizardStep++;
      },
      previousStep() {
        this.referentialWizardStep--;
      },
      editReferential(id){
        this.$http.get('/referential/find', {params: {idReferential: id}}).then(response => {
           this.itemToEdit = response.data;
           this.listSelected = [];
           this.refreshSelected();
        }, response => {
           this.viewError=true;
           this.msgError = "Error during call service";
        });
      },
      refreshSelected(){
        for (var i= 0; i < this.listProcess.length; i++) {
          var processConsumer = this.listProcess[i];
          for (var j= 0; j < this.itemToEdit.listIdProcessConsumer.length; j++) {
            if(this.itemToEdit.listIdProcessConsumer[j] === processConsumer.processDefinition.idProcess){
              this.listSelected.push(processConsumer);
            }
          }
        }
      },
      deleteReferential(id){
        this.$http.get('/referential/delete', {params: {idReferential: id}}).then(response => {
           this.back();
        }, response => {
           this.viewError=true;
           this.msgError = "Error during call service";
        });
      },
      updateReferential(){
       this.itemToEdit.listIdProcessConsumer= [];
        for (var i= 0; i < this.listSelected.length; i++) {
          this.itemToEdit.listIdProcessConsumer.push(this.listSelected[i].processDefinition.idProcess);
        }
        this.$http.post('/referential/update',this.itemToEdit ).then(response => {
           this.back();
        }, response => {
           this.viewError=true;
           this.msgError = "Error during call service";
        });
      },
      deleteItem(item){
         this.itemToEdit.listAssociatedKeys=this.itemToEdit.listAssociatedKeys.filter(e => e !== item);
      },
      deleteMetadata(item){
         this.itemToEdit.listMetadata=this.itemToEdit.listMetadata.filter(e => e !== item);
      },
      addKeys(){
         this.itemToEdit.listAssociatedKeys.push(this.newEntry);
      },
      addMetadata(){
         this.itemToEdit.listMetadata.push(this.newMetadata);
      },
      back(){
        this.$router.push('/referential/list');
      }
   }
  }
</script>
