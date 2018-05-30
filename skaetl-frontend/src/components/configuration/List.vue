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

        <v-data-table v-bind:headers="headers" :items="listConfig" item-key="name" :search="search">
          <template slot="items" slot-scope="props">
              <td>
                    <v-btn color="warning" style="width: 120px" small v-on:click.native="editConfig(props.item.idConfiguration)">Edit<v-icon right>edit</v-icon></v-btn>
                    <v-btn color="success" style="width: 120px" small v-on:click.native="generate(props.item.idConfiguration)">Logstash<v-icon right>description</v-icon></v-btn>
                    <v-btn color="success" style="width: 120px" small v-if="props.item.statusConfig == 'INIT' || props.item.statusConfig == 'DISABLE'" v-on:click.native="active(props.item.idConfiguration)">Activate<v-icon right>touch_app</v-icon></v-btn>
                    <v-btn color="pink darken-2" style="width: 120px" small v-if="props.item.statusConfig == 'ACTIVE'" v-on:click.native="deactive(props.item.idConfiguration)">DeActivate<v-icon right>close</v-icon></v-btn>
                    <v-btn color="error" style="width: 120px" small v-if="props.item.statusConfig == 'ERROR'" v-on:click.native="active(props.item.idConfiguration)">ERROR<v-icon right>error_outline</v-icon></v-btn>
                    <v-btn color="red" style="width: 120px" small v-on:click.native="deleteConfig(props.item.idConfiguration)">delete<v-icon right>delete</v-icon></v-btn>
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
