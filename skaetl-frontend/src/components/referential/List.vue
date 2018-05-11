<template>
  <v-container fluid grid-list-md >
     <v-layout row wrap>
         <v-flex xs6 sm6 md6>
            <v-btn color="primary" v-on:click.native="addView">Create Referential</v-btn>
            <v-btn color="orange" v-on:click.native="kibana">Visualise Referential</v-btn>
            <v-btn flat  v-on:click.native="load" icon color="blue lighten-2">
                  <v-icon>refresh</v-icon>
            </v-btn>
         </v-flex>
     </v-layout>
     <v-data-table v-bind:headers="headers" :items="listReferential" hide-actions >
         <template slot="items" slot-scope="props">
           <td class="text-md-center">
                  <v-btn color="warning" style="width: 120px" small v-on:click.native="editReferential(props.item.id)">Edit<v-icon right>edit</v-icon></v-btn>
                  <v-btn color="success" style="width: 120px" small text-color="white" v-if="props.item.statusProcess == 'INIT' || props.item.statusProcess == 'DISABLE'" v-on:click.native="activateReferential(props.item.id)">Activate<v-icon right>touch_app</v-icon></v-btn>
                  <v-btn color="error" style="width: 120px" small text-color="white" v-if="props.item.statusProcess == 'ACTIVE'" v-on:click.native="deactivateReferential(props.item.id)">DeActivate<v-icon right>close</v-icon></v-btn>
                  <v-btn color="error" style="width: 120px" small v-if="props.item.statusProcess == 'ERROR'" v-on:click.native="activateReferential(props.item.idConfiguration)">ERROR<v-icon right>error_outline</v-icon></v-btn>
                  <v-btn color="red" style="width: 120px" small v-on:click.native="deleteReferential(props.item.id)">delete<v-icon right>delete</v-icon></v-btn>
           </td>
           <td class="text-xs-center">{{props.item.processDefinition.name}}</td>
           <td class="text-xs-center">{{props.item.processDefinition.referentialKey}}</td>
           <td class="text-xs-center">
             <v-layout row>
                <v-flex v-for="item in props.item.processDefinition.listAssociatedKeys">
                   <v-chip color="blue-grey lighten-3">{{item}}</v-chip>
                </v-flex>
             </v-layout>
           </td>
           <td class="text-md-center">
             <v-layout row>
                <v-flex v-for="item in props.item.processDefinition.listMetadata">
                   <v-chip color="blue darken-2">{{item}}</v-chip>
                </v-flex>
             </v-layout>
            </td>
            <td class="text-md-center">
              <v-layout row>
                <v-flex v-for="item in props.item.processDefinition.listIdProcessConsumer">
                  <v-chip color="orange lighten-2">{{ getProcessName(item) }} </v-chip>
                </v-flex>
              </v-layout>
            </td>
         </template>
     </v-data-table>
     <v-layout row wrap>
        <v-flex xs12 sm12 md12>
          <v-alert v-model="viewError" xs12 sm12 md12  color="error" icon="warning" value="true" dismissible>
               {{ msgError }}
          </v-alert>
        </v-flex>
     </v-layout>
  </v-container>

</template>

<style scoped>
  .borderbox {
      border-style: solid;
      border-width: 1px;
  }
</style>

<script>
  export default{
   data () {
      return {
        idProcess: '',
        listReferential: [],
        newEntry: "",
        itemToEdit: {"idReferential":"","listAssociatedKeys":[],"name":"","referentialKey":"","listMetadata":[]},
        dialogReferential: false,
        viewError: false,
        msgError: '',
        headers: [
                   { text: 'Action', align: 'center',sortable: 0,value: '',width: '10%' },
                   { text: 'Name', align: 'center',value: 'processDefinition.name',width: '10%' },
                   { text: 'Data Referential', align: 'center',value: 'processDefinition.referentialKey',width: '10%' },
                   { text: 'Key',align: 'center',sortable: 0,value: '', width: '25%'},
                   { text: 'Metadata',align: 'center',sortable: 0,value: '', width: '25%'},
                   { text: 'Process',align: 'center',sortable: 0,value: '', width: '25%'}
                 ],
        process,
        consumerProcess: new Map()
      }
   },
   mounted() {
      this.load();
   },
   methods: {
      load(){
        this.$http.get('/referential/findAll').then(response => {
           this.listReferential = response.data;
        }, response => {
           this.viewError=true;
           this.msgError = "Error during call service";
        });
      },
      deactivateReferential(idReferential){
        this.$http.get('/referential/deactivate', {params: {idReferential: idReferential}}).then(response => {
           this.load();
        }, response => {
           this.viewError=true;
           this.msgError = "Error during call service";
        });
      },
      activateReferential(idReferential){
        this.$http.get('/referential/activate', {params: {idReferential: idReferential}}).then(response => {
           this.load();
        }, response => {
           this.viewError=true;
           this.msgError = "Error during call service";
        });
      },
      editReferential(id){
        this.$router.push('/referential/add?idReferential='+id);
      },
      deleteReferential(id){
        this.$http.get('/referential/delete', {params: {idReferential: id}}).then(response => {
           this.listReferential = response.data;
        }, response => {
           this.viewError=true;
           this.msgError = "Error during call service";
        });
      },
      addView(){
         this.$router.push('/referential/add');
      },
      kibana(){
         // Not Hardcoded
         window.open('http://kibana.skalogs-demo.skalogs.com','_blank');
      },
      getProcessName(id){
        if (this.consumerProcess != undefined && this.consumerProcess.get(id) != undefined) {
            return this.consumerProcess.get(id);
        } else {
          this.$http.get('/referential/find', {params: {idReferential: id}}).then(response => {
            this.process = response.data;
            name = this.process.name;
            this.consumerProcess.set(id, name);
            return name;
          }, response => {
            this.viewError=true;
            this.msgError = "Error during call service";
        });
        }
      }
    }
  }
</script>
