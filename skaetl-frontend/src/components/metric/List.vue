<template>
  <v-container fluid grid-list-md>
    <v-layout row wrap>
      <v-card xs12 sm12 md12>
        <v-card-title>
          <v-btn color="primary" v-on:click.native="newProcess">Create Metric Process</v-btn>
          <v-btn :disabled="listProcess.length > 0 ? false : true" color="orange" v-on:click.native="visualize">Visualise<v-icon right>wifi</v-icon></v-btn>
          <v-tooltip right>
            <v-btn slot="activator" flat v-on:click.native="refreshAction" icon color="blue lighten-2">
              <v-icon>refresh</v-icon>
            </v-btn>
            <span>Refresh the list</span>
          </v-tooltip>
          <v-spacer></v-spacer>
          <v-text-field
            v-model="search"
            append-icon="search"
            label="Search"
            single-line
            hide-details
          ></v-text-field>
        </v-card-title>
        <v-data-table v-bind:headers="headers" :items="listProcess" :search="search" :hide-actions="listProcess.length > 5 ? false : true">
          <template slot="items" slot-scope="props">
            <td>
              <v-btn color="warning" style="width: 120px" small v-on:click.native="editProcess(props.item.id)">Edit
                <v-icon right>edit</v-icon>
              </v-btn>
              <v-btn color="success" style="width: 120px" small
                     v-if="props.item.statusProcess == 'DISABLE' || props.item.statusProcess == 'INIT'"
                     v-on:click.native="activateProcess(props.item.id)">Activate
                <v-icon right>touch_app</v-icon>
              </v-btn>
              <v-btn color="pink darken-2" style="width: 120px" small v-if="props.item.statusProcess == 'ENABLE'"
                     v-on:click.native="deactivateProcess(props.item.id)">Deactivate
                <v-icon right>close</v-icon>
              </v-btn>
              <v-btn color="error" style="width: 120px" small v-if="props.item.statusProcess == 'ERROR'"
                     v-on:click.native="deactivateProcess(props.item.id)">ERROR
                <v-icon right>error_outline</v-icon>
              </v-btn>
              <v-btn color="error" style="width: 120px" small v-if="props.item.statusProcess == 'DEGRADED'"
                     v-on:click.native="deactivateProcess(props.item.id)">DEGRADED
                <v-icon right>error_outline</v-icon>
              </v-btn>
              <v-btn color="red" style="width: 120px" small v-on:click.native="deleteProcess(props.item.id)">delete
                <v-icon right>delete</v-icon>
              </v-btn>
            </td>
            <td class="text-xs-center">{{props.item.processDefinition.name}}</td>
            <td class="text-xs-center">
              <v-chip color="blue-grey lighten-3" small>{{props.item.processDefinition.aggFunction}}</v-chip>
            </td>
            <td class="text-xs-center">
              <v-chip color="purple lighten-2" small>{{windowFormat(props.item.processDefinition)}}</v-chip>
            </td>
            <td class="text-xs-center">
              <v-chip color="blue-grey lighten-3" small v-if="props.item.processDefinition.where">
                {{props.item.processDefinition.where}}
              </v-chip>
            </td>
            <td class="text-xs-center">
              <v-chip color="blue-grey lighten-3" small v-if="props.item.processDefinition.groupBy">
                {{props.item.processDefinition.groupBy}}
              </v-chip>
            </td>
            <td class="text-xs-center">
              <v-chip color="purple lighten-2" small v-if="props.item.processDefinition.having">
                {{props.item.processDefinition.having}}
              </v-chip>
            </td>
            <td class="text-xs-center">
              <v-flex  class="pa-0 ma-0" xs12 sm12 md12 v-for="outputitem in props.item.processDefinition.processOutputs">
                <v-flex class="pa-0 ma-0">
                  <v-chip color="blue-grey lighten-3" small>{{outputitem.typeOutput}}</v-chip>
                </v-flex>
              </v-flex>
            </td>
          </template>
        </v-data-table>
      </v-card>
    </v-layout>
    <v-layout row wrap>
      <v-flex xs12 sm12 md12>
        <v-alert v-model="viewError" xs12 sm12 md12 color="error" icon="warning" value="true" dismissible>
          {{ msgError }}
        </v-alert>
      </v-flex>
    </v-layout>
  </v-container>
</template>


<script>
  export default {
    data() {
      return {
        search: '',
        listProcess: [],
        input: {},
        uiCreate: '',
        msgError: '',
        viewError: false,
        selectedToCheckBox: false,
        headers: [
          {text: 'Action', align: 'center', sortable: 0, value: '', width: '4%'},
          {text: 'Name', align: 'center', value: 'processDefinition.name', width: '8%'},
          {text: 'Function', align: 'center', value: 'processDefinition.aggFunction', width: '16%'},
          {text: 'Window', align: 'center', sortable: 0, value: '', width: '8%'},
          {text: 'Where', align: 'center', value: 'processDefinition.where', width: '8%'},
          {text: 'Group By', align: 'center', value: 'processDefinition.groupBy', width: '16%'},
          {text: 'Having', align: 'center', value: 'processDefinition.having', width: '16%'},
          {text: 'Output', align: 'center', value: 'processDefinition.processOutputs',width: '8%' }
        ]
      }
    },
    mounted() {
      this.$http.get('/metric/listProcess').then(response => {
        this.listProcess = response.data;
        console.log(this.listProcess);
      }, response => {
        this.viewError = true;
        this.msgError = "Error during call service";
      });
    },
    methods: {
      visualize(){
        this.$router.push('/process/network?source=metric');
      },
      refreshAction() {
        this.$http.get('/metric/listProcess').then(response => {
          this.listProcess = response.data;
        }, response => {
          this.viewError = true;
          this.msgError = "Error during call service";
        });
      },
      newProcess() {
        this.$router.push('/metric/add');
      },
      editProcess(idProcess) {
        this.$router.push('/metric/add?idProcess=' + idProcess);
      },
      activateProcess(idProcess) {
        this.$http.get('/metric/activate', {params: {idProcess: idProcess}}).then(response => {
          this.refreshAction();
        }, response => {
          this.viewError = true;
          this.msgError = "Error during call service";
        });
      },
      deactivateProcess(idProcess) {
        this.$http.get('/metric/deactivate', {params: {idProcess: idProcess}}).then(response => {
          this.refreshAction();
        }, response => {
          this.viewError = true;
          this.msgError = "Error during call service";
        });

      },
      deleteProcess(idProcess) {
        this.$http.delete('/metric/delete', {params: {idProcess: idProcess}}).then(response => {
          this.refreshAction();
        }, response => {
          this.viewError = true;
          this.msgError = "Error during call service";
        });
      },
      windowFormat(processDefinition) {
        if (processDefinition.windowType != 'HOPPING') {
          return processDefinition.windowType + "(" + processDefinition.size + " " + processDefinition.sizeUnit + ")";
        } else {
          return processDefinition.windowType + "(" + processDefinition.size + " " + processDefinition.sizeUnit + ", " +
            processDefinition.advanceBy + " " + processDefinition.advanceByUnit + ")";
        }
      }

    }
  }
</script>
