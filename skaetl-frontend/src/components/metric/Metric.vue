<template>
  <v-container fluid grid-list-md>
    <v-stepper v-model="metricWizardStep">
      <v-stepper-header v-if="!editMode">
        <v-stepper-step step="1" v-bind:complete="metricWizardStep > 1" editable>Process Name</v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step step="2" v-bind:complete="metricWizardStep > 2" :editable="metricWizardStep > 1">Source
        </v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step step="3" v-bind:complete="metricWizardStep > 3" :editable="metricWizardStep > 2">Window
        </v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step step="4" v-bind:complete="metricWizardStep > 4" :editable="metricWizardStep > 3">Function
        </v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step step="5" v-bind:complete="metricWizardStep > 5" :editable="metricWizardStep > 4">
          Where
          <small>Optional</small>
        </v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step step="6" v-bind:complete="metricWizardStep > 6" :editable="metricWizardStep > 4">
          Group By
          <small>Optional</small>
        </v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step step="7" v-bind:complete="metricWizardStep > 7" :editable="metricWizardStep > 4">
          Having
          <small>Optional</small>
        </v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step step="8" v-bind:complete="metricWizardStep > 7" :editable="metricWizardStep > 4">
          Join
          <small>Optional</small>
        </v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step step="9" v-bind:complete="metricWizardStep > 7" :editable="metricWizardStep > 4">Outputs / Notifications
        </v-stepper-step>
      </v-stepper-header>

      <v-stepper-header v-if="editMode">
        <v-stepper-step step="1" editable>Process Name</v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step step="2" editable>Source
        </v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step step="3" editable>Window
        </v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step step="4" editable>Function
        </v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step step="5" editable>
          Where
          <small>Optional</small>
        </v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step step="6" editable>
          Group By
          <small>Optional</small>
        </v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step step="7" editable>
          Having
          <small>Optional</small>
        </v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step step="8" editable>
          Join
          <small>Optional</small>
        </v-stepper-step>
        <v-divider></v-divider>
        <v-stepper-step step="9" editable>Destination
        </v-stepper-step>
      </v-stepper-header>

      <v-stepper-content step="1">
        <v-card class="mb-5">
          <v-card-title>
            <div class="headline">Choose a metric name</div>
          </v-card-title>
          <v-card-text>
            <v-text-field label="Name of your process" v-model="metricProcess.name" required
                          :rules="[() => !!metricProcess.name || 'This field is required']"></v-text-field>
          </v-card-text>
          <v-card-actions>
            <v-btn disabled color="primary" style="width: 120px" @click.native="previousStep()">
              <v-icon>navigate_before</v-icon>
              Previous
            </v-btn>
            <v-btn color="primary" style="width: 120px" @click.native="nextStep()" :disabled="!metricProcess.name">Next
              <v-icon>navigate_next</v-icon>
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-stepper-content>

      <v-stepper-content step="2">
        <v-card class="mb-5">
          <v-card-title>
            <div class="headline">Select process consumers sources</div>
          </v-card-title>
          <v-card-text>
            <v-select v-model="metricProcess.selectedProcess" label="Select processes" item-value="id"
                      item-text="processDefinition.name" return-object deletable-chips chips tags
                      :items="listProcess"></v-select>
          </v-card-text>
          <v-card-actions>
            <v-btn color="primary" style="width: 120px" @click.native="previousStep()">
              <v-icon>navigate_before</v-icon>
              Previous
            </v-btn>
            <v-btn color="primary" style="width: 120px" @click.native="nextStep()"
                   :disabled="metricProcess.selectedProcess.length===0">Next
              <v-icon>navigate_next</v-icon>
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-stepper-content>

      <v-stepper-content step="3">
        <v-card class="mb-5">
          <v-card-title>
            <div class="headline">Choose the window for the computation</div>
          </v-card-title>
          <v-card-text>
            <v-select v-bind:items="windowTypes" v-model="metricProcess.windowType" label="Select Window Type"
                      item-value="text" required
                      :rules="[() => !!metricProcess.windowType || 'This field is required']"></v-select>
            <v-layout row>
              <v-text-field label="Size" v-model="metricProcess.size" required
                            :rules="[() => !!metricProcess.size || 'This field is required']"></v-text-field>
              <v-select v-bind:items="timeunits" v-model="metricProcess.sizeUnit" label="Select Window TimeUnit"
                        item-value="text" required
                        :rules="[() => !!metricProcess.sizeUnit || 'This field is required']"></v-select>
            </v-layout>
            <v-layout row v-if="metricProcess.windowType == 'HOPPING'">
              <v-text-field label="Advance by Size" v-model="metricProcess.advanceBy" required
                            :rules="[() => metricProcess.windowType == 'HOPPING' && !!metricProcess.advanceBy || 'This field is required']"></v-text-field>
              <v-select v-bind:items="timeunits" v-model="metricProcess.advanceByUnit"
                        label="Select advance by timeUnit" item-value="text" required
                        :rules="[() => metricProcess.windowType == 'HOPPING' && !!metricProcess.advanceByUnit || 'This field is required']"></v-select>
            </v-layout>
          </v-card-text>
          <v-card-actions>
            <v-btn color="primary" style="width: 120px" @click.native="previousStep()">
              <v-icon>navigate_before</v-icon>
              Previous
            </v-btn>
            <v-btn color="primary" style="width: 120px" @click.native="nextStep()" :disabled="!metricProcess.size">Next
              <v-icon>navigate_next</v-icon>
            </v-btn>
          </v-card-actions>
        </v-card>

      </v-stepper-content>

      <v-stepper-content step="4">
        <v-card class="mb-5">
          <v-card-title>
            <div class="headline">Choose a function to apply</div>
          </v-card-title>
          <v-card-text>
            <v-layout row>
              <v-select v-bind:items="functions" v-model="metricProcess.functionName" label="Select function"
                        item-value="text" required
                        :rules="[() => !!metricProcess.functionName || 'This field is required']"></v-select>
              <v-text-field label="Field" v-model="metricProcess.functionField" required
                            :rules="[() => !!metricProcess.functionField || 'This field is required']"></v-text-field>
            </v-layout>
          </v-card-text>
          <v-card-actions>
            <v-btn color="primary" style="width: 120px" @click.native="previousStep()">
              <v-icon>navigate_before</v-icon>
              Previous
            </v-btn>
            <v-btn color="primary" style="width: 120px" @click.native="nextStep()"
                   :disabled="!metricProcess.functionName || !metricProcess.functionField">Next
              <v-icon>navigate_next</v-icon>
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-stepper-content>

      <v-stepper-content step="5">
        <v-card class="mb-5">
          <v-card-title>
            <div>
              <div class="headline">Define a filter condition</div>
              <div><span class="grey--text"><small>Optional</small></span></div>
            </div>
          </v-card-title>
          <v-card-text>
            <v-text-field label="Where condition" v-model="metricProcess.where"
                          hint="A condition such as myfield = 'something'" persistent-hint></v-text-field>
            <FiltersHelp></FiltersHelp>
          </v-card-text>
          <v-card-actions>
            <v-btn color="primary" style="width: 120px" @click.native="previousStep()">
              <v-icon>navigate_before</v-icon>
              Previous
            </v-btn>
            <v-btn color="primary" style="width: 120px" @click.native="nextStep()">Next
              <v-icon>navigate_next</v-icon>
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-stepper-content>

      <v-stepper-content step="6">
        <v-card class="mb-5">
          <v-card-title>
            <div>
              <div class="headline">Select group by fields</div>
              <div><span class="grey--text"><small>Optional</small></span></div>
            </div>
          </v-card-title>
          <v-card-text>
            <v-text-field label="Group by Field" v-model="metricProcess.groupBy" hint="Comma separated"
                          persistent-hint></v-text-field>
          </v-card-text>
          <v-card-actions>
            <v-btn color="primary" style="width: 120px" @click.native="previousStep()">
              <v-icon>navigate_before</v-icon>
              Previous
            </v-btn>
            <v-btn color="primary" style="width: 120px" @click.native="nextStep()">Next
              <v-icon>navigate_next</v-icon>
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-stepper-content>

      <v-stepper-content step="7">
        <v-card class="mb-5">
          <v-card-title>
            <div>
              <div class="headline">Define an output condition</div>
              <div><span class="grey--text"><small>Optional</small></span></div>
            </div>
          </v-card-title>
          <v-card-text>
            <v-text-field label="Having result" v-model="metricProcess.having" hint="A condition such as >= 42"
                          persistent-hint></v-text-field>
          </v-card-text>
          <v-card-actions>
            <v-btn color="primary" style="width: 120px" @click.native="previousStep()">
              <v-icon>navigate_before</v-icon>
              Previous
            </v-btn>
            <v-btn color="primary" style="width: 120px" @click.native="nextStep()">Next
              <v-icon>navigate_next</v-icon>
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-stepper-content>

      <v-stepper-content step="8">
        <v-card class="mb-5">
          <v-card-title>
            <div>
              <div class="headline">Correlate with another dataset</div>
              <div><span class="grey--text"><small>Optional</small></span></div>
            </div>
          </v-card-title>
          <v-card-text>
            <v-select v-bind:items="joinTypes" v-model="metricProcess.joinType"
                      label="Select Join Type" item-value="text" required
                      :rules="[() => !!metricProcess.joinType || 'This field is required']"></v-select>
            <div v-if="isJoinEnabled()">
              <v-card class="mb-3">
                <v-card-title>
                  <div class="title">Join on Process Consumers</div>
                </v-card-title>
                <v-card-text>
                  <v-select v-model="metricProcess.selectedProcessB" label="Select joined processes" item-value="id"
                            item-text="processDefinition.name" return-object deletable-chips chips tags
                            :items="listProcess"></v-select>
                </v-card-text>
              </v-card>
              <v-card class="mb-3">
                <v-card-title>
                  <div class="title">Join window</div>
                </v-card-title>
                <v-card-text>
                  <v-layout row>
                    <v-text-field label="Window Size" v-model="metricProcess.joinWindowSize" required
                                  :rules="[() => !!metricProcess.joinWindowSize || 'This field is required']"></v-text-field>
                    <v-select v-bind:items="timeunits" v-model="metricProcess.joinWindowUnit"
                              label="Select Window Size TimeUnit" item-value="text" required
                              :rules="[() => !!metricProcess.joinWindowUnit || 'This field is required']"></v-select>
                  </v-layout>
                </v-card-text>
              </v-card>
              <v-card class="mb-3">
                <v-card-title>
                  <div class="title">Join rule</div>
                </v-card-title>
                <v-card-text>
                  <v-layout row>
                    <v-text-field label="Field from A" v-model="metricProcess.joinKeyFromA"
                                  persistent-hint></v-text-field>
                    <v-text-field label="Field from B" v-model="metricProcess.joinKeyFromB"
                                  persistent-hint></v-text-field>
                  </v-layout>
                </v-card-text>
              </v-card>
              <v-card class="mb-3">
                <v-card-title>
                  <div class="title">Join where condition</div>
                </v-card-title>
                <v-card-text>
                  <v-text-field label="Where condition" v-model="metricProcess.joinWhere"
                                hint="A condition such as myfield = 'something'" persistent-hint></v-text-field>
                </v-card-text>
              </v-card>
            </div>
          </v-card-text>
          <v-card-actions>
            <v-btn color="primary" style="width: 120px" @click.native="previousStep()">
              <v-icon>navigate_before</v-icon>
              Previous
            </v-btn>
            <v-btn color="primary" style="width: 120px" @click.native="nextStep()">Next
              <v-icon>navigate_next</v-icon>
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-stepper-content>

      <v-stepper-content step="9">
        <Output :process-output="metricProcess.processOutputs" v-on:previousStep="previousStep" v-on:saveProcess="save" can-save="true"/>
      </v-stepper-content>
    </v-stepper>
  </v-container>
</template>

<script>
  import Output from "../process/Output";
  import FiltersHelp from "../process/FiltersHelp";

  export default {
    components: {FiltersHelp, Output},
    data() {
      return {
        metricProcess: {
          idProcess: "",
          processName: "",
          selectedProcess: [],
          sourceProcessConsumers: [],
          selectedProcessB: [],
          sourceProcessConsumersB: [],
          joinType: "NONE",
          joinKeyFromA: "",
          joinKeyFromB: "",
          joinWhere: "",
          joinWindowSize: 5,
          joinWindowUnit: "MINUTES",
          functionName: "COUNT",
          functionField: "*",
          windowType: "TUMBLING",
          size: 5,
          sizeUnit: "MINUTES",
          advanceBy: 1,
          advanceByUnit: "MINUTES",
          where: "",
          groupBy: "",
          having: "",
          processOutputs: []
        },
        functions: ["COUNT", "COUNT-DISTINCT", "SUM", "AVG", "MIN", "MAX", "STDDEV", "MEAN"],
        windowTypes: ["TUMBLING", "HOPPING", "SESSION"],
        timeunits: ["SECONDS", "MINUTES", "HOURS", "DAYS"],
        joinTypes: ["NONE", "INNER", "OUTER", "LEFT" ],
        metricWizardStep: 1,
        message: "",
        listProcess: [],
        editMode: false
      }
    },
    mounted() {
      this.metricProcess.idProcess = this.$route.query.idProcess;

      if (this.metricProcess.idProcess) {
        this.editMode = true;
        this.$http.get('/metric/findById', {params: {idProcess: this.metricProcess.idProcess}}).then(response => {
          this.metricProcess = response.data;
          this.metricProcess.selectedProcess = [];
          this.metricProcess.selectedProcessB = [];
          var result = this.metricProcess.aggFunction.match(/(.*)\((.*)\)/);
          this.metricProcess.functionName = result[1];
          this.metricProcess.functionField = result[2];
          this.refreshSelected();
        }, response => {
          this.viewError = true;
          this.msgError = "Error during call service";
        });
      } else {
        this.$http.get('/metric/init').then(response => {
          //FIXME clearly initialize should set the entire object and provided most of the default value
          this.metricProcess.idProcess = response.data.idProcess;
          this.metricProcess['@class'] = response.data['@class'];
        }, response => {
          this.viewError = true;
          this.msgError = "Error during call service";
        });
      }

      this.$http.get('/process/findAll').then(response => {
        this.listProcess = response.data;
        this.refreshSelected();
      }, response => {
        this.viewError = true;
        this.msgError = "Error during call service";
      });
    },
    methods: {
      nextStep() {
        this.metricWizardStep++;
      },
      previousStep() {
        this.metricWizardStep--;
      },
      refreshSelected() {

        for (var i = 0; i < this.listProcess.length; i++) {
          var processConsumer = this.listProcess[i];
          if (this.metricProcess.sourceProcessConsumers.indexOf(processConsumer.processDefinition.idProcess) >= 0) {
            this.metricProcess.selectedProcess.push(processConsumer);
          }

          if (this.metricProcess.sourceProcessConsumersB.indexOf(processConsumer.processDefinition.idProcess) >= 0) {
            this.metricProcess.selectedProcessB.push(processConsumer);
          }
        }
      },
      save() {
        this.metricProcess.aggFunction = this.metricProcess.functionName + '(' + this.metricProcess.functionField + ')';
        this.metricProcess.sourceProcessConsumers = this.metricProcess.selectedProcess.map(process => process.id);
        this.metricProcess.sourceProcessConsumersB = this.metricProcess.selectedProcessB.map(process => process.id);
        this.$http.post('/metric/update', this.metricProcess).then(response => {
          this.$router.push('/metric/list');
        }, response => {
          this.viewError = true;
          this.msgError = "Error during call service";
        });
      },
      windowFormat(processDefinition) {
        if (processDefinition.windowType == 'HOPPING') {
          return processDefinition.windowType + "(" + processDefinition.size + " " + processDefinition.sizeUnit + ")";
        } else {
          return processDefinition.windowType + "(" + processDefinition.size + " " + processDefinition.sizeUnit + ", " +
            processDefinition.advanceBy + " " + processDefinition.advanceByUnit + ")";
        }
      },
      isJoinEnabled() {
        return this.metricProcess.joinType != "NONE";
      }
    }
  }
</script>
