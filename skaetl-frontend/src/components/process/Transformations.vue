<template>
  <v-card class="mb-5">
    <v-card-title>
      <div>
        <div class="headline">Select your transformations</div>
        <div><span class="grey--text"><small>Optional</small></span></div>
      </div>
    </v-card-title>
    <v-card-text>
      <v-flex>
        <v-flex xs12 sm6 md6>
          <v-layout row wrap>
            <v-select label="Type Transformation" v-model="currentTransformation.typeTransformation" v-bind:items="type"
                      v-on:change="actionView" max-height="600"/>
          </v-layout>
        </v-flex>
      </v-flex>
      <v-flex>
        <v-flex xs12 sm6 md6>
          <v-layout row wrap v-show="viewComposeField">
            <v-text-field label="Key Field"
                          v-model="currentTransformation.parameterTransformation.composeField.key" required></v-text-field>
            <v-text-field label="Value Field"
                          v-model="currentTransformation.parameterTransformation.composeField.value" required></v-text-field>
          </v-layout>
        </v-flex>
      </v-flex>
      <v-flex>
        <v-flex xs12 sm6 md6>
          <v-layout row wrap v-show="viewDateField">
            <v-text-field label="Key Field"
                          v-model="currentTransformation.parameterTransformation.formatDateValue.keyField" required></v-text-field>
            <v-text-field label="Source Format"
                          v-model="currentTransformation.parameterTransformation.formatDateValue.srcFormat" required></v-text-field>
            <v-text-field label="Target Format"
                          v-model="currentTransformation.parameterTransformation.formatDateValue.targetFormat" required></v-text-field>
          </v-layout>
        </v-flex>
      </v-flex>
      <v-flex>
        <v-flex xs12 sm6 md6>
          <v-layout row wrap v-show="viewLookupList">
            <v-text-field label="Limit on field (Optional) "
                          v-model="currentTransformation.parameterTransformation.keyField"></v-text-field>
          </v-layout>
          <v-layout row wrap v-show="viewLookupList">
            <v-text-field label="Replace value" v-model="replaceValue" required></v-text-field>
            <v-icon slot="divider" color="blue">forward</v-icon>
            <v-text-field label="new value" v-model="replaceNewValue" required></v-text-field>
            <v-btn color="primary" v-on:click.native="addItemToLookupList">Add</v-btn>
          </v-layout>
          <v-layout row wrap v-show="viewLookupList">
            <v-flex v-for="item in listLookup">
              <v-chip color="orange" text-color="white" close @input="deleteItem(item)">
                {{item.oldValue}}-{{item.newValue}}
              </v-chip>
            </v-flex>
          </v-layout>
        </v-flex>
      </v-flex>
      <v-flex>
        <v-flex xs12 sm6 md6>
          <v-layout row wrap v-show="viewHash">
            <v-text-field label="Field"
                          v-model="currentTransformation.parameterTransformation.processHashData.field" required></v-text-field>
            <v-select label="Type Hash" v-model="currentTransformation.parameterTransformation.processHashData.typeHash"
                      v-bind:items="typeHash" max-height="600" required/>
          </v-layout>
          </p></p>
        </v-flex>
      </v-flex>
      <v-flex>
        <v-flex xs12 sm6 md6>
          <v-layout row wrap v-show="viewKeyField">
            <v-text-field label="Field "
                          v-model="currentTransformation.parameterTransformation.keyField" required></v-text-field>
          </v-layout>
        </v-flex>
      </v-flex>
      <v-flex>
        <v-flex xs12 sm6 md6>
          <v-layout row wrap v-show="viewLookupExternal">
            <v-text-field label="Url"
                          v-model="currentTransformation.parameterTransformation.externalHTTPData.url"></v-text-field>
          </v-layout>
          <v-layout row wrap v-show="viewLookupExternal">
            <v-select label="METHOD" v-model="currentTransformation.parameterTransformation.externalHTTPData.httpMethod"
                      v-bind:items="methodCall"/>
            <v-text-field type="number" label="Refresh in seconds"
                          v-model="currentTransformation.parameterTransformation.externalHTTPData.refresh"></v-text-field>
            <v-text-field label="Limit on field (Optional) "
                          v-model="currentTransformation.parameterTransformation.keyField"></v-text-field>
          </v-layout>
          <v-layout row wrap v-show="viewLookupExternal">
            <v-text-field label="body with POST"
                          v-model="currentTransformation.parameterTransformation.externalHTTPData.body"></v-text-field>
          </v-layout>
        </v-flex>
      </v-flex>
      <v-layout row wrap>
        <v-flex xs12 sm12 md12>
          <v-flex v-for="(transfoItem, index) in processTransformations">
            <v-chip color="blue-grey lighten-3" small close v-on:input="removeTransformation(index)">{{index+1}} - {{transfoItem.typeTransformation}}</v-chip>
          </v-flex>
        </v-flex>
      </v-layout>
    </v-card-text>
    <v-card-actions>
      <v-btn color="primary" style="width: 120px" @click.native="$emit('previousStep')">
        <v-icon>navigate_before</v-icon>
        Previous
      </v-btn>
      <v-btn color="success" v-on:click.native="addTransformation">add Transformation
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
      processTransformations: {
        type: Array,
        required: true
      }
    },
    data: function () {
      return {
        currentTransformation: {
          "parameterTransformation": {
            "composeField": {"key": "", "value": ""},
            "formatDateValue": {"keyField": "", "srcFormat": "", "targetFormat": ""},
            "keyField": "",
            "listLookup": [],
            "externalHTTPData": {"url": "http://url:port", "refresh": "10", "httpMethod": "GET", "body": ""},
            "processHashData": {"field": "", "typeHash": "SHA256"}
          }
        },
        methodCall: ["GET", "POST"],
        typeHash: ["MURMUR3", "SHA256"],
        type: ["ADD_FIELD", "DELETE_FIELD", "RENAME_FIELD", "FORMAT_DATE", "FORMAT_BOOLEAN", "FORMAT_GEOPOINT",
          "FORMAT_DOUBLE", "FORMAT_LONG", "FORMAT_IP", "LOOKUP_LIST", "LOOKUP_EXTERNAL", "HASH", "ADD_GEO_LOCALISATION",
          "CAPITALIZE", "UNCAPITALIZE", "UPPER_CASE", "LOWER_CASE", "SWAP_CASE", "TRIM"],
        viewMessageClient: false,
        viewKeyField: false,
        viewComposeField: false,
        viewDateField: false,
        viewLookupList: false,
        viewLookupExternal: false,
        viewHash: false,
        listLookup: [],
        replaceValue: '',
        replaceNewValue: '',

      }
    },
    methods: {
      actionView(value) {
        if (value == "FORMAT_DATE") {
          this.disable();
          this.viewDateField = true;
        } else if (value == "HASH") {
          this.disable();
          this.viewHash = true;
        } else if (value == "LOOKUP_EXTERNAL") {
          this.disable();
          this.viewLookupExternal = true;
        } else if (value == "LOOKUP_LIST") {
          this.disable();
          this.viewLookupList = true;
        } else if (value == "ADD_FIELD" || value == "RENAME_FIELD") {
          this.disable();
          this.viewComposeField = true;
        } else if (value == "DELETE_FIELD" || value == "FORMAT_BOOLEAN" || value == "FORMAT_GEOPOINT" || value == "FORMAT_DOUBLE" || value == "FORMAT_LONG" || value == "FORMAT_IP" || value == "ADD_GEO_LOCALISATION" || value == "CAPITALIZE" || value == "UNCAPITALIZE" || value == "LOWER_CASE" || value == "UPPER_CASE" || value == "SWAP_CASE" ||value == "TRIM") {
          this.disable();
          this.viewKeyField = true;
        } else {
          this.disable();
        }
      },
      addTransformation() {
        if (this.viewLookupList) {
          var result = {};
          for (var i = 0; i < this.listLookup.length; i++) {
            var itemLookup = this.listLookup[i];
            result[itemLookup.oldValue] = itemLookup.newValue;
          }
          this.currentTransformation.parameterTransformation.mapLookup = result;
        }
        this.processTransformations.push(_.cloneDeep(this.currentTransformation));
      },
      disable(){
        this.viewKeyField=false;
        this.viewComposeField=false;
        this.viewDateField=false;
        this.viewLookupList=false;
        this.viewLookupExternal=false;
        this.viewHash=false;
      },
      removeTransformation(index) {
        this.processTransformations.splice(index,1);
      }
    }
  }
</script>
