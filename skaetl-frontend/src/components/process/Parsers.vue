<template>
  <v-card class="mb-5">
    <v-card-title>
      <div>
        <div class="headline">Select your parsers</div>
        <div><span class="grey--text"><small>Optional</small></span></div>
      </div>
    </v-card-title>
    <v-card-text>
      <v-flex xs8 sm8 md8>
        <v-layout row>
          <v-select label="Choose Parser" v-model="currentParser.typeParser" v-bind:items="typeParser"
                    v-on:change="actionGrokView"/>
        </v-layout>
      </v-flex>
      <v-flex>
        <v-flex xs12 sm6 md6>
          <v-text-field label="Grok Pattern" v-show="viewGrok" v-model="currentParser.grokPattern"></v-text-field>
          <v-checkbox label="Active Fail Parser Forward " v-model="currentParser.activeFailForward"></v-checkbox>
          <v-text-field v-if="currentParser.activeFailForward" label="Topic Fail Parser" v-model="currentParser.failForwardTopic" required></v-text-field>
          <v-text-field label="CSV (separated by ;)" v-show="viewCSV" v-model="currentParser.schemaCSV"></v-text-field>
        </v-flex>
      </v-flex>
      <v-layout row wrap>
        <v-flex xs12 sm12 md12>
          <v-flex v-for="(parserItem, index) in processParsers">
            <v-chip color="blue-grey lighten-3" small close v-on:input="removeParser(index)">{{index+1}} - {{parserItem.typeParser}}</v-chip>
          </v-flex>
        </v-flex>

      </v-layout>
    </v-card-text>
    <v-card-actions>
      <v-btn color="primary" style="width: 120px" @click.native="$emit('previousStep')">
        <v-icon>navigate_before</v-icon>
        Previous
      </v-btn>
      <v-btn color="success" v-on:click.native="addParser">add parser
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
      processParsers: {
        type: Array,
        required: true
      }
    },
    data: function () {
      return {
        typeParser: ["CEF", "NITRO", "GROK", "CSV"],
        viewGrok: false,
        viewCSV: false,
        viewMessageClient: false,
        messageClientCreated: '',
        currentParser: {}
      }
    },
    methods: {
      actionGrokView(value) {
        if (value == "GROK") {
          this.viewGrok = true;
          this.viewCSV = false;
        } else if (value == "CSV") {
          this.viewCSV = true;
          this.viewGrok = false;
        } else {
          this.viewGrok = false;
          this.viewCSV = false;
        }
      },
      addParser() {
        this.processParsers.push(_.cloneDeep(this.currentParser));
      },
      removeParser(index) {
        console.log(this.processParsers);
        this.processParsers.splice(index,1);
      }
    }
  }
</script>
