<template>
  <v-card class="mb-5">
    <v-card-title>
      <div>
        <div class="headline">Select your validations</div>
        <div><span class="grey--text"><small>Optional</small></span></div>
      </div>
    </v-card-title>
    <v-card-text>
      <v-flex>
        <v-flex xs12 sm6 md6>
          <v-layout row wrap>
            <v-select label="Type Validation" v-model="currentValidation.typeValidation" v-bind:items="type"
                      v-on:change="actionView"/>
          </v-layout>
        </v-flex>
      </v-flex>
      <v-flex>
        <v-flex xs12 sm6 md6>
          <v-layout row wrap v-show="viewMandatory">
            <v-text-field label="Mandatory (separeted by ;)"
                          v-model="currentValidation.parameterValidation.mandatory" required></v-text-field>
          </v-layout>
        </v-flex>
      </v-flex>
      <v-flex>
        <v-flex xs12 sm6 md6 v-show="viewBlackList">
          <v-layout row wrap>
            <v-text-field label="Key" v-model="keyBlackList" required></v-text-field>
            <v-text-field label="Value" v-model="valueBlackList" required></v-text-field>
            <v-btn color="primary" v-on:click.native="addItemBlackList">add BlackList Item</v-btn>
          </v-layout>
          <v-layout>
            <v-btn color="primary" v-on:click.native="removeBlackList">Remove</v-btn>
            <v-flex xs2 sm2 md2 v-for="itemBlack in currentValidation.parameterValidation.blackList">
              <v-btn color="purple lighten-2" small>{{itemBlack.key}}-{{itemBlack.value}}</v-btn>
            </v-flex>
          </v-layout>
        </v-flex>
      </v-flex>
      <v-flex>
        <v-flex xs12 sm6 md6>
          <v-layout row wrap v-show="viewGrok">
            <v-text-field label="Grok field " v-model="currentValidation.parameterValidation.grok.key"></v-text-field>
            <v-text-field label="Grok Pattern"
                          v-model="currentValidation.parameterValidation.grok.value"></v-text-field>
          </v-layout>
        </v-flex>
      </v-flex>
      <v-flex>
        <v-flex xs12 sm6 md6>
          <v-layout row wrap v-show="viewMaxFields">
            <v-text-field label="Maximum field "
                          v-model="currentValidation.parameterValidation.maxFields" required></v-text-field>
          </v-layout>
        </v-flex>
      </v-flex>
      <v-flex>
        <v-flex xs12 sm6 md6>
          <v-layout row wrap v-show="vieMaxMessageSize">
            <v-text-field label="Maximum Size message"
                          v-model="currentValidation.parameterValidation.maxMessageSize" required></v-text-field>
          </v-layout>
        </v-flex>
      </v-flex>
      <v-flex>
        <v-flex xs12 sm6 md6>
          <v-layout row wrap v-show="viewFieldExist">
            <v-text-field label="Field exist" v-model="currentValidation.parameterValidation.fieldExist" required></v-text-field>
          </v-layout>
        </v-flex>
      </v-flex>

      <v-layout row wrap>
        <v-flex xs12 sm12 md12>
          <v-flex v-for="(validItem,index) in processValidations">
            <v-chip color="blue-grey lighten-3" small close v-on:input="removeValidation(index)">{{index+1}} - {{validItem.typeValidation}}</v-chip>
          </v-flex>
        </v-flex>
      </v-layout>
    </v-card-text>
    <v-card-actions>
      <v-btn color="primary" style="width: 120px" @click.native="$emit('previousStep')">
        <v-icon>navigate_before</v-icon>
        Previous
      </v-btn>
      <v-btn color="success" v-on:click.native="addValidation">add Validation
        <v-icon>add</v-icon>
      </v-btn>
      <v-btn color="primary" style="width: 120px" @click.native="$emit('nextStep')">Next
        <v-icon>navigate_next</v-icon>
      </v-btn>
    </v-card-actions>
  </v-card>
</template>


<script>
  export default {
    props: {
      processValidations: {
        type: Array,
        required: true
      }
    },
    data: function () {
      return {
        currentValidation:  {
          "parameterValidation":{
            "mandatory":'',
            "blackList":[],
            "addField": {"key":"","value":""},
            "renameField": {"key":"","value":""},
            "deleteField": {"key":"","value":""},
            "formatDate": {"key":"","value":""},
            "grok": {"key":"","value":""},
            "maxFields":"",
            "maxMessageSize":"",
            "fieldExist":""
          }
        },
        keyBlackList: '',
        valueBlackList: '',
        type : ["MANDATORY_FIELD","BLACK_LIST_FIELD","MAX_FIELD","MAX_MESSAGE_SIZE","FIELD_EXIST"],
        vieMaxMessageSize: false,
        viewMaxFields: false,
        viewGrok: false,
        viewBlackList: false,
        viewMandatory: false,
        viewFieldExist: false,

      }
    },
    methods: {
      actionView(value){
        //Miam miam miammmmmmm
        if(value== "FORMAT_JSON"){
          this.disable();
          this.currentValidation.parameterValidation.formatJson=true;
        }else if(value== "MANDATORY_FIELD"){
          this.disable();
          this.viewMandatory=true;
        }else if(value == "BLACK_LIST_FIELD"){
          this.disable();
          this.viewBlackList=true;
        }else if(value == "GROK"){
          this.disable();
          this.viewGrok=true;
        }else if(value == "MAX_FIELD"){
          this.disable();
          this.viewMaxFields=true;
        }else if(value == "MAX_MESSAGE_SIZE"){
          this.disable();
          this.vieMaxMessageSize=true;
        }else if(value == "FIELD_EXIST"){
          this.disable();
          this.viewFieldExist=true;
        }else{
          this.disable();
        }
      },
      disable(){
        this.vieMaxMessageSize=false;
        this.viewMaxFields=false;
        this.viewGrok=false;
        this.viewBlackList=false;
        this.viewMandatory=false;
        this.viewFieldExist=false;
        this.currentValidation.parameterValidation.formatJson=false;
      },
      addItemBlackList(){
        this.currentValidation.parameterValidation.blackList.push({key: this.keyBlackList ,value: this.valueBlackList});
        this.keyBlackList = '';
        this.valueBlackList = '';
      },
      removeBlackList(){
        this.currentValidation.parameterValidation.blackList=[];
        this.keyBlackList = '';
        this.valueBlackList = '';
      },
      addValidation(){
        this.processValidations.push(_.cloneDeep(this.currentValidation));
      },
      removeValidation(index) {
        this.processValidations.splice(index,1);
      }
    }
  }
</script>
