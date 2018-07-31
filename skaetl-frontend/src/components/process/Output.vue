<template>
  <v-card class="mb-5">
    <v-card-title>
      <div>
        <div class="headline">Select destination</div>
        <div><span class="grey--text"><small>Optional</small></span></div>
      </div>
    </v-card-title>
    <v-card-text>
      <v-dialog v-model="dialog" max-width="500px">
        <v-btn color="success" slot="activator">add output
          <v-icon>add</v-icon>
        </v-btn>

        <v-card>
          <v-card-title>
            <span class="headline">{{ formTitle }}</span>
          </v-card-title>
          <v-card-text>
            <v-select label="Output type" v-model="editedItem.typeOutput" v-bind:items="typeOut" item-text="name" item-value="name">
              <template slot="item" slot-scope="typeOut">
                <v-list-tile-content>
                  <v-list-tile-title v-html="typeOut.item.name"></v-list-tile-title>
                  <v-list-tile-sub-title v-html="typeOut.item.type"></v-list-tile-sub-title>
                </v-list-tile-content>
              </template>
            </v-select>
              <v-layout row v-show="isKafkaTopic()">
                <v-text-field label="Topic Out" v-model="editedItem.parameterOutput.topicOut"></v-text-field>
              </v-layout>
              <v-layout row v-show="isElasticsearch()">
                <v-select label="Retention" v-model="editedItem.parameterOutput.elasticsearchRetentionLevel"
                          v-bind:items="typeRetention"/>
              </v-layout>
              <v-layout row v-show="isEmail()">
                <v-text-field label="Destination Email" v-model="editedItem.parameterOutput.email" required/>
              </v-layout>

              <v-text-field v-show="isSlack()" label="WebHook URL"
                            v-model="editedItem.parameterOutput.webHookURL"></v-text-field>
              <v-text-field v-show="isSlack() || isEmail()" label="Template"
                            v-model="editedItem.parameterOutput.template" textarea>Template
              </v-text-field>
              <v-expansion-panel v-show="isSlack() || isEmail()">
                <v-expansion-panel-content>
                  <div slot="header">Template language short description</div>
                  <v-card color="grey lighten-3">
                    <v-card-text>
                      If you want to insert a variable, you have to respect the following syntax: [[${variable_name}]]
                      <br>For instance: "The customer IP is [[${client_ip}]] for ..."
                      <br><br>
                      On <b>Metric</b> template, the specific variables are:<br>
                      <li/>[[${<b>result</b>}]] : The value of the Metric result
                      <li/>[[${<b>rule_dsl</b>}]] : The request
                      <li/>[[${<b>rule_name</b>}]] : The rule name
                      <li/>[[${<b>project</b>}]] : The project name
                      <br><br>
                      On <b>Consumer</b> template, all the variables, present in the record, are authorised.
                    </v-card-text>
                  </v-card></v-expansion-panel-content>
              </v-expansion-panel>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" flat @click.native="close">Cancel</v-btn>
            <v-btn color="blue darken-1" flat @click.native="save">Save</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-card-text>
    <v-data-table :headers="headers" :items="processOutput" hide-actions>
      <template slot="items" slot-scope="props">
        <td>{{props.item.typeOutput}}</td>
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
      <v-btn color="primary" style="width: 120px" @click.native="$emit('nextStep')" v-show="canUseNext">
        Next
        <v-icon>navigate_next</v-icon>
      </v-btn>
      <v-btn color="primary" style="width: 120px" :disabled="!processOutput.length>0" @click.native="$emit('saveProcess')">Save<v-icon>create</v-icon></v-btn>
    </v-card-actions>
  </v-card>
</template>


<script>
  export default {
    props: {
      processOutput: {
        type: Array,
        required: true
      },
      canUseNext: {
        type: Boolean,
        required: false
      },
    },
    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'New Item' : 'Edit Item';
      }
    },
    data: function () {
      return {
        typeOut: [{ header: 'Outputs' },
                  {name: 'KAFKA', type: 'processing dedicated'},
                  {name: 'ELASTICSEARCH', type: 'long term storage dedicated'},
                  { divider: true },
                  { header: 'Notifications' },
                  {name: 'EMAIL', type: 'notification dedicated'},
                  {name: 'SLACK', type: 'notification dedicated'},
                  {name: 'SNMP', type: 'notification dedicated'},
                  {name: 'SYSTEM_OUT', type: 'testing dedicated'}],
        typeRetention: ["week","month","quarter","year"],
        dialog: false,
        editedItem: {
          "typeOutput": "ELASTICSEARCH",
          "parameterOutput": {
            "topicOut": "output-topic",
            "elasticsearchRetentionLevel": "week",
            "webHookURL": "",
            "template": "",
          }
        },
        defaultItem: {
          "typeOutput": "ELASTICSEARCH",
          "parameterOutput": {
            "topicOut": "output-topic",
            "elasticsearchRetentionLevel": "week",
            "webHookURL": "",
            "template": "",
          }
        },
        editedIndex: -1,
        headers: [
          { text: 'Type', value: 'typeOutput'},
          { text: 'Actions', value: 'typeParser', sortable: false }
        ],

      }
    },
    methods: {
      isKafkaTopic() {
        return this.editedItem.typeOutput == "KAFKA";
      },
      isElasticsearch() {
        return this.editedItem.typeOutput == "ELASTICSEARCH";
      },
      isSlack() {
        return this.editedItem.typeOutput == "SLACK";
      },
      isEmail() {
        return this.editedItem.typeOutput == "EMAIL";
      },
      isSnmp() {
        return this.editedItem.typeOutput == "SNMP";
      },
      close () {
        this.dialog = false;
        this.editedItem = _.cloneDeep(this.defaultItem);
        this.editedIndex = -1;
      },
      editItem (item) {
        this.editedIndex = this.processOutput.indexOf(item);
        this.editedItem = _.cloneDeep(item);
        this.dialog = true;
      },
      deleteItem (item) {
        var index = this.processOutput.indexOf(item);
        confirm('Are you sure you want to delete this item?') && this.processOutput.splice(index, 1);
      },

      save() {
        if (this.editedIndex > -1) {
          Object.assign(this.processOutput[this.editedIndex], this.editedItem);
        } else {
          this.processOutput.push(this.editedItem);
        }
        this.close();
      }
    }
  }
</script>
