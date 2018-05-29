<template>
  <v-container fluid grid-list-md>
    <v-stepper v-model="wizardStep">
      <v-stepper-header v-if="!editMode">
        <v-stepper-step step="1" v-bind:complete="wizardStep > 1" editable>Process Name</v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step step="2" v-bind:complete="wizardStep > 2" :editable="wizardStep > 1">Input</v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step step="3" v-bind:complete="wizardStep > 3" :editable="wizardStep > 2">
          Parsers
          <small>Optional</small>
        </v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step step="4" v-bind:complete="wizardStep > 4" :editable="wizardStep > 2">
          Transformations
          <small>Optional</small>
        </v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step step="5" v-bind:complete="wizardStep > 5" :editable="wizardStep > 2">
          Validations
          <small>Optional</small>
        </v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step step="6" v-bind:complete="wizardStep > 6" :editable="wizardStep > 2">
          Filters
          <small>Optional</small>
        </v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step step="7" v-bind:complete="wizardStep > 7" :editable="wizardStep > 2">Outputs / Notifications</v-stepper-step>
      </v-stepper-header>

      <v-stepper-header v-if="editMode">
              <v-stepper-step step="1" editable>Process Name</v-stepper-step>
              <v-divider></v-divider>
              <v-stepper-step step="2" editable>Input</v-stepper-step>
              <v-divider></v-divider>
              <v-stepper-step step="3" editable>
                Parsers
                <small>Optional</small>
              </v-stepper-step>
              <v-divider></v-divider>
              <v-stepper-step step="4" editable>
                Transformations
                <small>Optional</small>
              </v-stepper-step>
              <v-divider></v-divider>
              <v-stepper-step step="5" editable>
                Validations
                <small>Optional</small>
              </v-stepper-step>
              <v-divider></v-divider>
              <v-stepper-step step="6" editable>
                Filters
                <small>Optional</small>
              </v-stepper-step>
              <v-divider></v-divider>
              <v-stepper-step step="7" editable>Output</v-stepper-step>
            </v-stepper-header>

      <v-stepper-content step="1">
        <v-card class="mb-5">
          <v-card-title>
            <div class="headline">Choose a process name</div>
          </v-card-title>
          <v-card-text>
            <v-text-field label="Name of your process" v-model="process.name" required
                          :rules="[() => !!process.name || 'This field is required']"></v-text-field>
          </v-card-text>
          <v-card-actions>
            <v-btn disabled color="primary" style="width: 120px" @click.native="previousStep()">
              <v-icon>navigate_before</v-icon>
              Previous
            </v-btn>
            <v-btn color="primary" style="width: 120px" @click.native="nextStep()" :disabled="!process.name">Next
              <v-icon>navigate_next</v-icon>
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-stepper-content>

      <v-stepper-content step="2">
        <Input :process-input="process.processInput" v-on:previousStep="previousStep" v-on:nextStep="nextStep" />
      </v-stepper-content>

      <v-stepper-content step="3">
        <Parsers :process-parsers="process.processParser" v-on:previousStep="previousStep" v-on:nextStep="nextStep" />
      </v-stepper-content>

      <v-stepper-content step="4">
        <Transformations :process-transformations="process.processTransformation" v-on:previousStep="previousStep" v-on:nextStep="nextStep" />
      </v-stepper-content>

      <v-stepper-content step="5">
        <Validations :process-validations="process.processValidation" v-on:previousStep="previousStep" v-on:nextStep="nextStep" />
      </v-stepper-content>

      <v-stepper-content step="6">
        <Filters :process-filters="process.processFilter" v-on:previousStep="previousStep" v-on:nextStep="nextStep" />
      </v-stepper-content>

      <v-stepper-content step="7">
        <Output :process-output="process.processOutput" v-on:previousStep="previousStep" v-on:saveProcess="saveProcess" />
      </v-stepper-content>
    </v-stepper>
  </v-container>
</template>

<script>
  import Input from "./Input";
  import Parsers from "./Parsers";
  import Transformations from "./Transformations";
  import Validations from "./Validations";
  import Filters from "./Filters";
  import Output from "./Output";

  export default {
    components: {Filters, Validations, Transformations, Parsers, Input, Output},
    data() {
      return {
        wizardStep: 1,
        message: "",
        process: {
          idProcess: "",
          processInput: {
            host: "kafka.kafka",
            port:"9092",
            topicInput:"processtopic"
          },
          processParser: [],
          processTransformation: [],
          processValidation: [],
          processFilter: [],
          processOutput : []
        },
        editMode: false
      }
    },
    mounted() {
      this.process.idProcess = this.$route.query.idProcess;

      if (this.process.idProcess) {
        this.editMode = true;
        this.$http.get('/process/findProcess', {params: {idProcess: this.process.idProcess}}).then(response => {
          this.process = response.data;
        }, response => {
          this.viewError = true;
          this.msgError = "Error during call service";
        });
      } else {
        this.$http.get('/process/init').then(response => {
          this.process = response.data;
        }, response => {
          this.viewError = true;
          this.msgError = "Error during call service";
        });
      }
    },
    methods: {
      nextStep() {
        this.wizardStep++;
      },
      previousStep() {
        this.wizardStep--;
      },
      saveProcess() {
        this.$http.post('/process/save',  this.process).then(response => {
          this.$router.push('/process/list');
        }, response => {
          this.viewError = true;
          this.msgError = "Error during call service";
        });
        console.log(this.process);
      }
    }
  }
</script>
