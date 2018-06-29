<template>
 <v-container fluid grid-list-md>
    <v-layout row wrap>
      <v-card xs12 sm12 md12>
        <v-card-title>
          <v-btn color="primary" v-on:click.native="newConfig">Create Config</v-btn>
          <v-spacer></v-spacer>
          <v-text-field
            v-model="search"
            append-icon="search"
            label="Search"
            single-line
            hide-details
          ></v-text-field>
        </v-card-title>

        <v-data-table v-bind:headers="headers" :items="listConfig" item-key="name" :search="search" :hide-actions="listConfig.length > 5 ? false : true">
          <template slot="items" slot-scope="props">
              <td>
                   <v-menu bottom left>
                      <v-btn slot="activator" icon>
                         <v-icon v-if="props.item.statusProcess == 'DEGRADED'" color="red">warning</v-icon>
                         <v-icon v-if="props.item.statusProcess == 'ERROR'" color="red">error_outline</v-icon>
                         <v-icon v-if="props.item.statusProcess == 'DISABLE' || props.item.statusProcess == 'INIT'" color="orange">pause_circle_filled</v-icon>
                         <v-icon v-if="props.item.statusProcess == 'ENABLE'" color="green">play_circle_filled</v-icon>
                      </v-btn>
                      <v-list>
                        <v-list-tile>
                          <v-list-tile-title style="height: 40px" >
                                <v-btn v-if="props.item.statusProcess == 'DEGRADED'" color="purple">{{props.item.statusConfig}}</v-btn>
                                <v-btn v-if="props.item.statusProcess == 'ERROR'" color="red">{{props.item.statusConfig}}</v-btn>
                                <v-btn v-if="props.item.statusProcess == 'DISABLE' || props.item.statusConfig == 'INIT'" color="orange">{{props.item.statusConfig}}</v-btn>
                                <v-btn v-if="props.item.statusProcess == 'ENABLE'" color="green">{{props.item.statusConfig}}</v-btn>
                          </v-list-tile-title>
                        </v-list-tile>
                        <v-list-tile v-on:click.native="editConfig(props.item.idConfiguration)">
                          <v-list-tile-title class="justify-center layout px-0">Edit</v-list-tile-title>
                        </v-list-tile>
                        <v-list-tile v-on:click.native="generate(props.item.idConfiguration)">
                          <v-list-tile-title class="justify-center layout px-0">Edit</v-list-tile-title>
                        </v-list-tile>
                        <v-list-tile :disabled="props.item.statusProcess == 'ENABLE' || props.item.statusProcess == 'ERROR' || props.item.statusProcess == 'DEGRADED'" v-on:click.native="deactive(props.item.idConfiguration)">
                          <v-list-tile-title class="justify-center layout px-0">Deactivate</v-list-tile-title>
                        </v-list-tile>
                        <v-list-tile  :disabled="props.item.statusProcess == 'DISABLE' || props.item.statusProcess == 'INIT'" v-on:click.native="active(props.item.idConfiguration)">
                          <v-list-tile-title class="justify-center layout px-0">Activate</v-list-tile-title>
                        </v-list-tile>
                        <v-list-tile v-on:click.native="deleteConfig(props.item.idConfiguration)">
                          <v-list-tile-title class="justify-center layout px-0">Delete</v-list-tile-title>
                        </v-list-tile>
                      </v-list>
                   </v-menu>
              </td>
              <td class="text-xs-center">{{props.item.name}}</td>
              <td class="text-xs-center">{{props.item.confData.env}}</td>
              <td class="text-xs-center">{{props.item.confData.category}}</td>
              <td class="text-xs-center">{{props.item.confData.apiKey}}</td>
              <td class="text-xs-center"><v-checkbox :disabled="true" v-model="props.item.statusCustomConfiguration"></v-checkbox></td>
              <td class="text-xs-center">
                <v-layout row>
                    <v-flex class="pa-0 ma-0" xs12 sm12 md12 v-for="inputitem in props.item.input">
                       <v-chip color="purple lighten-2" small>{{inputitem.typeInput}}</v-chip>
                    </v-flex>
                </v-layout>
              </td>
              <td class="text-xs-center">
                <v-layout row>
                    <v-flex class="pa-0 ma-0" xs12 sm12 md12 v-for="outputitem in props.item.output">
                       <v-chip color="orange lighten-2" small>{{outputitem.topic}}</v-chip>
                    </v-flex>
                </v-layout>
              </td>
          </template>
        </v-data-table>
      </v-card>
    </v-layout>
    <v-layout row wrap>
      <v-flex xs12 sm12 md12 >
        <v-alert v-model="viewError" xs12 sm12 md12  color="error" icon="warning" value="true" dismissible>
             {{ msgError }}
        </v-alert>
       </v-flex>
     </v-layout>
 </v-container>
</template>


<script>
  export default{
    data () {
         return {
           search: '',
           listConfig: [],
           input: {},
           uiCreate: '',
           msgError: '',
           viewError: false,
           selectedToCheckBox : false,
           headers: [
             { text: 'Action',align: 'center', sortable: 0, value: '', width: '8%'},
             { text: 'Name',align: 'center',value: 'name', width: '8%'},
             { text: 'Env',align: 'center',value: 'confData.env', width: '1%'},
             { text: 'Category',align: 'center',value: 'confData.category', width: '8%'},
             { text: 'ApiKey',align: 'center',value: 'confData.apiKey', width: '25%'},
             { text: 'Custom',align: 'center',value: 'statusCustomConfiguration', width: '2%'},
             { text: 'Input', align: 'center', sortable: 0, value: 'input' },
             { text: 'Output', align: 'center', sortable: 0, value: 'output' }
           ]
         }
    },
    mounted() {
       this.$http.get('/configuration/findAll').then(response => {
            this.listConfig=response.data;
         }, response => {
           this.viewError=true;
           this.msgError = "Error during call service";
         });
    },
    methods: {
        active(idConfigSelect){
          this.$http.get('/configuration/activeConfiguration', {params: {idConfiguration:idConfigSelect}}).then(response => {
              this.listConfig=response.data;
           }, response => {
             this.viewError=true;
             this.msgError = "Error during call service";
           });
        },
        deactive(idConfigSelect){
          this.$http.get('/configuration/deactiveConfiguration', {params: {idConfiguration:idConfigSelect}}).then(response => {
              this.listConfig=response.data;
           }, response => {
             this.viewError=true;
             this.msgError = "Error during call service";
           });
        },
        deleteConfig(idConfigSelect){
           this.$http.get('/configuration/deleteConfiguration', {params: {idConfiguration:idConfigSelect}}).then(response => {
              this.listConfig=response.data;
           }, response => {
             this.viewError=true;
             this.msgError = "Error during call service";
           });
        },
        generate(idConfigSelect){
          this.$router.push('/generate/logstash?idConfiguration='+idConfigSelect);
        },
        removeConfig(idConfigSelect){
          console.log('removeConfig', idConfigSelect);
        },
        editConfig(idConfigSelect){
          this.$router.push('/configuration/add?idConfiguration='+idConfigSelect);
        },
        newConfig(){
          this.$router.push('/configuration/add');
        }
    }
  }
</script>
