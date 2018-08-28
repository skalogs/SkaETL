<template>
  <v-card class="mb-5">
    <v-card-title>
      <div>
        <div class="headline">Select your transformations</div>
        <div><span class="grey--text"><small>Optional</small></span></div>
      </div>
    </v-card-title>
    <v-card-text>
      <v-dialog v-model="dialog" max-width="500px">
        <v-btn color="success" slot="activator">add Transformation
          <v-icon>add</v-icon>
        </v-btn>
        <v-card>
          <v-card-title>
            <span class="headline">{{ formTitle }}</span>
          </v-card-title>
          <v-card-text>
            <v-layout row wrap>
              <v-select label="Type Transformation" v-model="editedItem.typeTransformation" :items="type"
                        max-height="600"
                        item-value="name">
                <template slot="selection" slot-scope="data">
                  {{data.item.name}}
                </template>
                <template slot="item" slot-scope="data">
                  <v-list-tile-content>
                    <v-list-tile-title v-html="data.item.name"></v-list-tile-title>
                    <v-list-tile-sub-title v-html="data.item.description"></v-list-tile-sub-title>
                  </v-list-tile-content>
                </template>
              </v-select>
            </v-layout>

            <v-layout row wrap v-show="isComposeField()">
              <v-text-field label="Key Field"
                            v-model="editedItem.parameterTransformation.composeField.key" required></v-text-field>
              <v-text-field label="Value Field"
                            v-model="editedItem.parameterTransformation.composeField.value" required></v-text-field>
            </v-layout>

            <v-layout row wrap v-show="isCsvLookup()">
              <v-text-field label="Key Field"
                            v-model="editedItem.parameterTransformation.csvLookupData.field" required></v-text-field>
            </v-layout>
            <v-layout row wrap v-show="isCsvLookup()">
              <v-textarea label="Value Field" outline
                            v-model="editedItem.parameterTransformation.csvLookupData.data" required></v-textarea>
            </v-layout>


            <v-layout row wrap v-show="isDateField()">
              <v-text-field label="Key Field"
                            v-model="editedItem.parameterTransformation.formatDateValue.keyField"
                            required></v-text-field>
              <v-text-field label="Source Format"
                            v-model="editedItem.parameterTransformation.formatDateValue.srcFormat"
                            required></v-text-field>
              <v-text-field label="Target Format"
                            v-model="editedItem.parameterTransformation.formatDateValue.targetFormat"
                            required></v-text-field>
            </v-layout>

            <v-layout row wrap v-show="isDateExtractor()">
              <v-text-field label="Key Field"
                            v-model="editedItem.parameterTransformation.formatDateValue.keyField"
                            required></v-text-field>
              <v-text-field label="Source Format"
                            v-model="editedItem.parameterTransformation.formatDateValue.srcFormat"
                            required></v-text-field>
            </v-layout>

            <v-layout row wrap v-show="isDateExtractor()">
              <v-text-field label="Target Field"
                            v-model="editedItem.parameterTransformation.formatDateValue.targetField"
                            required></v-text-field>

              <v-text-field label="Target Format"
                            v-model="editedItem.parameterTransformation.formatDateValue.targetFormat"
                            required></v-text-field>
            </v-layout>
            <v-alert :value="isDateExtractor()" color="info" icon="info" outline>
              Date format should follow <a href="https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#patterns" target="_blank">Java Date Pattern Syntax</a>.
            </v-alert>

            <v-layout row wrap v-show="isLookupList()">
              <v-text-field label="Limit on field (Optional) "
                            v-model="editedItem.parameterTransformation.keyField"></v-text-field>
            </v-layout>
            <v-layout row wrap v-show="isLookupList()">
              <v-text-field label="Replace value" v-model="replaceValue" required></v-text-field>
              <v-icon slot="divider" color="blue">forward</v-icon>
              <v-text-field label="new value" v-model="replaceNewValue" required></v-text-field>
              <v-btn color="primary" v-on:click.native="addItemToLookupList">Add</v-btn>
            </v-layout>
            <v-layout row wrap v-show="isLookupList()">
              <v-flex v-for="item in listLookup">
                <v-chip color="orange" text-color="white" close @input="deleteItemToLookupList(item)">
                  {{item.oldValue}}-{{item.newValue}}
                </v-chip>
              </v-flex>
            </v-layout>

            <v-layout row wrap v-show="isHash()">
              <v-text-field label="Field"
                            v-model="editedItem.parameterTransformation.processHashData.field" required></v-text-field>
              <v-select label="Type Hash" v-model="editedItem.parameterTransformation.processHashData.typeHash"
                        v-bind:items="typeHash" max-height="600" required/>
            </v-layout>

            <v-layout row wrap v-show="isKeyField()">
              <v-text-field label="Field "
                            v-model="editedItem.parameterTransformation.keyField" required></v-text-field>
            </v-layout>

            <v-layout row wrap v-show="isLookupExternal()">
              <v-text-field label="Url"
                            v-model="editedItem.parameterTransformation.externalHTTPData.url"></v-text-field>
            </v-layout>
            <v-layout row wrap v-show="isLookupExternal()">
              <v-select label="METHOD" v-model="editedItem.parameterTransformation.externalHTTPData.httpMethod"
                        v-bind:items="methodCall"/>
              <v-text-field type="number" label="Refresh in seconds"
                            v-model="editedItem.parameterTransformation.externalHTTPData.refresh"></v-text-field>
              <v-text-field label="Limit on field (Optional) "
                            v-model="editedItem.parameterTransformation.keyField"></v-text-field>
            </v-layout>
            <v-layout row wrap v-show="isLookupExternal()">
              <v-text-field label="body with POST"
                            v-model="editedItem.parameterTransformation.externalHTTPData.body"></v-text-field>
            </v-layout>
            <v-layout row wrap v-show="isFormatGeoPoint()">
              <v-checkbox label="Convert in GeoJSON format" hint="Not needed if your sending as text, tick this if your sending geopoint as an array with [latitude, longitude]."
                          persistent-hint
                          v-model="editedItem.parameterTransformation.formatGeoJson"></v-checkbox>
            </v-layout>


          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" flat @click.native="close">Cancel</v-btn>
            <v-btn color="blue darken-1" flat @click.native="save">Save</v-btn>
          </v-card-actions>

        </v-card>

      </v-dialog>
      <v-data-table :headers="headers" :items="processTransformations" hide-actions>
        <template slot="items" slot-scope="props">
          <td>{{props.item.typeTransformation}}</td>
          <td>{{formatField(props.item)}}</td>
          <td class="justify-center layout px-0">
            <v-btn icon class="mx-0" @click="editItem(props.item)">
              <v-icon color="teal">edit</v-icon>
            </v-btn>
            <v-btn icon class="mx-0" @click="deleteItem(props.item)">
              <v-icon color="pink">delete</v-icon>
            </v-btn>
          </td>
        </template>
      </v-data-table>
    </v-card-text>
    <v-card-actions>
      <v-btn color="primary" style="width: 120px" @click.native="$emit('previousStep')">
        <v-icon>navigate_before</v-icon>
        Previous
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
        dialog: false,
        editedItem: {
          "parameterTransformation": {
            "composeField": {"key": "", "value": ""},
            "formatDateValue": {"keyField": "", "srcFormat": "", "targetFormat": "", "targetField": ""},
            "keyField": "",
            "mapLookup": {},
            "externalHTTPData": {"url": "http://url:port", "refresh": "10", "httpMethod": "GET", "body": ""},
            "processHashData": {"field": "", "typeHash": "SHA256"},
            "csvLookupData": {"field": "", "data": ""}
          }
        },
        editedIndex: -1,
        headers: [
          {text: 'Type', value: 'typeTransformation', sortable: false},
          {text: 'Field', value: 'parameterTransformation.keyField' , sortable: false},
          {text: 'Actions', value: 'typeParser', sortable: false}
        ],
        defaultItem: {
          "parameterTransformation": {
            "composeField": {"key": "", "value": ""},
            "formatDateValue": {"keyField": "", "srcFormat": "", "targetFormat": "", "targetField": ""},
            "keyField": "",
            "mapLookup": {},
            "externalHTTPData": {"url": "http://url:port", "refresh": "10", "httpMethod": "GET", "body": ""},
            "processHashData": {"field": "", "typeHash": "SHA256"},
            "csvLookupData": {"field": "", "data": "value;keyToAdd1;valueToAdd1;keyToAdd2;valueToAdd2;....."}
          }
        },
        methodCall: ["GET", "POST"],
        typeHash: ["MURMUR3", "SHA256"],
        type: [],
        replaceValue: '',
        replaceNewValue: '',
        listLookup: []
      }
    },
    mounted() {
      this.$http.get('/process/transformators', {}).then(response => {
        this.type = response.data.sort();
      }, response => {
        this.viewError = true;
        this.msgError = "Error during call service";
      });
    },
    computed: {
      formTitle() {
        return this.editedIndex === -1 ? 'New Item' : 'Edit Item';
      }
    },
    methods: {
      isCsvLookup(){
        return this.editedItem.typeTransformation == "ADD_CSV_LOOKUP";
      },
      isComposeField() {
        return this.editedItem.typeTransformation == "ADD_FIELD" || this.editedItem.typeTransformation == "RENAME_FIELD";
      },
      isDateField() {
        return this.editedItem.typeTransformation == "FORMAT_DATE";
      },
      isDateExtractor() {
        return this.editedItem.typeTransformation == "DATE_EXTRACTOR";
      },
      isLookupList() {
        return this.editedItem.typeTransformation == "LOOKUP_LIST";
      },
      isLookupExternal() {
        return this.editedItem.typeTransformation == "LOOKUP_EXTERNAL";
      },
      isHash() {
        return this.editedItem.typeTransformation == "HASH";
      },
      isKeyField() {
        var value = this.editedItem.typeTransformation;
        return value == "DELETE_FIELD" || value == "FORMAT_BOOLEAN" ||
          value == "FORMAT_GEOPOINT" || value == "FORMAT_DOUBLE" ||
          value == "FORMAT_LONG" || value == "FORMAT_IP" ||
          value == "FORMAT_KEYWORD" || value == "FORMAT_TEXT" || 
          value == "ADD_GEO_LOCALISATION" || value == "CAPITALIZE" ||
          value == "UNCAPITALIZE" || value == "LOWER_CASE" ||
          value == "UPPER_CASE" || value == "SWAP_CASE" ||
          value == "TRIM" || value == "FORMAT_EMAIL" ||
          value == "TRANSLATE_ARRAY";
      },
      isFormatGeoPoint(){
        return this.editedItem.typeTransformation == "FORMAT_GEOPOINT";
      },
      formatField(item) {
        if (item.typeTransformation == "ADD_FIELD" || item.typeTransformation == "RENAME_FIELD") {
          return item.parameterTransformation.composeField.key;
        } else if (item.typeTransformation == "HASH") {
          return item.parameterTransformation.processHashData.field;
        } else if (item.typeTransformation == "ADD_CSV_LOOKUP") {
          return item.parameterTransformation.csvLookupData.field;
        } else if (item.typeTransformation == "DATE_EXTRACTOR") {
          return item.parameterTransformation.formatDateValue.targetField;
        } else {
          return item.parameterTransformation.keyField;
        }
      },
      close() {
        this.dialog = false;
        this.editedItem = _.cloneDeep(this.defaultItem);
        this.editedIndex = -1;
        this.lookupList = [];
        this.replaceValue = '';
        this.replaceNewValue = '';
      },
      editItem(item) {
        this.editedIndex = this.processTransformations.indexOf(item);
        this.editedItem = _.cloneDeep(item);
        this.dialog = true;
        if (this.editedItem.typeTransformation == "LOOKUP_LIST") {
          Object.entries(this.editedItem.parameterTransformation.mapLookup).forEach(
            ([key, value]) => this.listLookup.push({oldValue: key, newValue: value})
          );
        }
      },
      deleteItem(item) {
        var index = this.processTransformations.indexOf(item);
        confirm('Are you sure you want to delete this item?') && this.processTransformations.splice(index, 1);
      },

      save() {
        if (this.editedItem.typeTransformation == "LOOKUP_LIST") {
          var result = {};
          for (var i = 0; i < this.listLookup.length; i++) {
            var itemLookup = this.listLookup[i];
            result[itemLookup.oldValue] = itemLookup.newValue;
          }
          this.editedItem.parameterTransformation.mapLookup = result;
        }
        if (this.editedIndex > -1) {
          Object.assign(this.processTransformations[this.editedIndex], this.editedItem);
        } else {
          this.processTransformations.push(this.editedItem);
        }
        this.close();
      },
      deleteItemToLookupList(item){
        this.listLookup=this.listLookup.filter(e => e !== item);
      },
      addItemToLookupList(){
        this.listLookup.push({oldValue: this.replaceValue, newValue: this.replaceNewValue});
      }
    }
  }
</script>
