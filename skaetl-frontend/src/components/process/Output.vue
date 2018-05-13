<template>
  <v-card class="mb-5">
    <v-card-title>
      <div>
        <div class="headline">Select destination</div>
        <div><span class="grey--text"><small>Optional</small></span></div>
      </div>
    </v-card-title>
    <v-card-text>
      <v-flex xs6 sm6 md6>
        <v-flex xs8 sm8 md8>
          <v-layout row>
            <v-select label="Output type" v-model="currentProcessOutput.typeOutput" v-bind:items="typeOut"/>
          </v-layout>
        </v-flex>
        <v-flex xs8 sm8 md8>
          <v-layout row v-show="viewOut()">
            <v-text-field label="Topic Out" v-model="currentProcessOutput.parameterOutput.topicOut"></v-text-field>
          </v-layout>
        </v-flex>
        <v-flex xs8 sm8 md8>
          <v-layout row v-show="viewES()">
            <v-select label="Retention" v-model="currentProcessOutput.parameterOutput.elasticsearchRetentionLevel"
                      v-bind:items="typeRetention"/>
          </v-layout>
          <v-layout row v-show="viewEmail()">
            <v-text-field label="Destination Email" v-model="currentProcessOutput.parameterOutput.email" required/>
          </v-layout>
        </v-flex>
        <v-layout row>
          <v-flex xs12>
            <v-text-field v-show="viewSlack()" label="WebHook URL"
                          v-model="currentProcessOutput.parameterOutput.webHookURL"></v-text-field>
            <v-text-field v-show="viewSlack() || viewEmail()" label="Template"
                          v-model="currentProcessOutput.parameterOutput.template" textarea>Template
            </v-text-field>
            <v-expansion-panel v-show="viewSlack() || viewEmail()">
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
          </v-flex>
        </v-layout>
      </v-flex>
      <v-layout row wrap>
        <v-flex xs12 sm12 md12>
          <v-flex v-for="(processOutItem, index) in processOutput">
            <v-chip color="blue-grey lighten-3" small close v-on:input="removeOutput(index)">{{index+1}} - {{processOutItem.typeOutput}}</v-chip>
          </v-flex>
        </v-flex>
      </v-layout>
    </v-card-text>
    <v-card-actions>
      <v-btn color="primary" style="width: 120px" @click.native="$emit('previousStep')">
        <v-icon>navigate_before</v-icon>
        Previous
      </v-btn>
      <v-btn color="success" style="width: 120px" @click.native="create()">Add Output<v-icon>add</v-icon></v-btn>
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
      }
    },
    data: function () {
      return {
        typeOut: ["KAFKA","SYSTEM_OUT","ELASTICSEARCH","EMAIL","SLACK","SNMP"],
        typeRetention: ["week","month","quarter","year"],
        currentProcessOutput: {"typeOutput": "ELASTICSEARCH",
                               "parameterOutput": {"topicOut": "output-topic",
                                                   "elasticsearchRetentionLevel":"week",
                                                   "webHookURL":"",
                                                   "template":"",
                                                  }
                              }
      }
    },
    methods: {
      create(){
        this.processOutput.push(_.cloneDeep(this.currentProcessOutput));
      },
      removeOutput(index) {
        this.processOutput.splice(index,1);
      },
      viewOut() {
        return this.currentProcessOutput.typeOutput == "KAFKA";
      },
      viewES() {
        return this.currentProcessOutput.typeOutput == "ELASTICSEARCH";
      },
      viewSlack() {
        return this.currentProcessOutput.typeOutput == "SLACK";
      },
      viewEmail() {
        return this.currentProcessOutput.typeOutput == "EMAIL";
      },
      viewSnmp() {
        return this.currentProcessOutput.typeOutput == "SNMP";
      }
    }
  }
</script>
