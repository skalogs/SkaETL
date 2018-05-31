<template>
  <v-card class="mb-5">
    <v-card-title>
      <div>
        <div class="headline">Select your validations</div>
        <div><span class="grey--text"><small>Optional</small></span></div>
      </div>
    </v-card-title>
    <v-card-text>
      <v-dialog v-model="dialog" max-width="500px">
        <v-btn color="success" slot="activator">add validation
          <v-icon>add</v-icon>
        </v-btn>

        <v-card>
          <v-card-title>
            <span class="headline">{{ formTitle }}</span>
          </v-card-title>
          <v-card-text>
            <v-select label="Type Validation" v-model="editedItem.typeValidation" v-bind:items="type"/>

            <v-layout row wrap v-show="isMandatory()">
              <v-text-field label="Mandatory (separated by ;)"
                            v-model="editedItem.parameterValidation.mandatory" required></v-text-field>
            </v-layout>

            <v-flex xs12 sm6 md6 v-show="isBlackList()">
              <v-layout row wrap>
                <v-text-field label="Key" v-model="keyBlackList" required></v-text-field>
                <v-text-field label="Value" v-model="valueBlackList" required></v-text-field>
                <v-btn color="primary" v-on:click.native="addItemBlackList">add BlackList Item</v-btn>
              </v-layout>
              <v-layout>
                <v-flex v-for="itemBlack in editedItem.parameterValidation.blackList">
                  <v-btn color="purple lighten-2" smallclose @input="removeBlackList(item)">{{itemBlack.key}}-{{itemBlack.value}}</v-btn>
                </v-flex>
              </v-layout>
            </v-flex>

            <v-layout row wrap v-show="isMaxField()">
              <v-text-field label="Maximum field "
                            v-model="editedItem.parameterValidation.maxFields" required></v-text-field>
            </v-layout>

            <v-layout row wrap v-show="isMaxMessageSize()">
              <v-text-field label="Maximum Size message"
                            v-model="editedItem.parameterValidation.maxMessageSize" required></v-text-field>
            </v-layout>

            <v-layout row wrap v-show="isFieldExist()">
              <v-text-field label="Field exist" v-model="editedItem.parameterValidation.fieldExist"
                            required></v-text-field>
            </v-layout>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" flat @click.native="close">Cancel</v-btn>
            <v-btn color="blue darken-1" flat @click.native="save">Save</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>


    </v-card-text>
    <v-data-table :headers="headers" :items="processValidations" hide-actions>
      <template slot="items" slot-scope="props">
        <td>{{props.item.typeValidation}}</td>
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
        dialog: false,

        editedItem: {
          "parameterValidation": {
            "mandatory": '',
            "blackList": [],
            "maxFields": "",
            "maxMessageSize": "",
            "fieldExist": ""
          }
        },
        defaultItem: {
          "parameterValidation": {
            "mandatory": '',
            "blackList": [],
            "maxFields": "",
            "maxMessageSize": "",
            "fieldExist": ""
          }
        },
        editedIndex: -1,
        headers: [
          {text: 'Type', value: 'typeParser'},
          {text: 'Actions', value: 'typeParser', sortable: false}
        ],
        keyBlackList: '',
        valueBlackList: '',
        type: ["MANDATORY_FIELD", "BLACK_LIST_FIELD", "MAX_FIELD", "MAX_MESSAGE_SIZE", "FIELD_EXIST"]
      }
    },
    computed: {
      formTitle() {
        return this.editedIndex === -1 ? 'New Item' : 'Edit Item';
      }
    },
    methods: {
      isMandatory() {
        return this.editedItem.typeValidation == "MANDATORY_FIELD";
      },
      isBlackList() {
        return this.editedItem.typeValidation == "BLACK_LIST_FIELD";
      },
      isMaxField() {
        return this.editedItem.typeValidation == "MAX_FIELD";
      },
      isMaxMessageSize() {
        return this.editedItem.typeValidation == "MAX_MESSAGE_SIZE";
      },
      isFieldExist() {
        return this.editedItem.typeValidation == "FIELD_EXIST";
      },
      close () {
        this.dialog = false;
        this.editedItem = _.cloneDeep(this.defaultItem);
        this.editedIndex = -1;
      },
      editItem (item) {
        this.editedIndex = this.processValidations.indexOf(item);
        this.editedItem = _.cloneDeep(item);
        this.dialog = true;
      },
      deleteItem (item) {
        var index = this.processValidations.indexOf(item);
        confirm('Are you sure you want to delete this item?') && this.processValidations.splice(index, 1);
      },

      save() {
        if (this.editedIndex > -1) {
          Object.assign(this.processValidations[this.editedIndex], this.editedItem);
        } else {
          this.processValidations.push(this.editedItem);
        }
        this.close();
      },
      addItemBlackList() {
        this.editedItem.parameterValidation.blackList.push({key: this.keyBlackList, value: this.valueBlackList});
        this.keyBlackList = '';
        this.valueBlackList = '';
      },
      removeBlackList(item) {
        this.editedItem.parameterValidation.blackList=this.editedItem.parameterValidation.blackList.filter(e => e !== item);
        this.keyBlackList = '';
        this.valueBlackList = '';
      }
    }
  }
</script>
