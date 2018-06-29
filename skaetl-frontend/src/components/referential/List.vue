<template>
  <v-container fluid grid-list-md >
     <v-layout row wrap>
         <v-flex xs6 sm6 md6>
            <v-btn color="primary" v-on:click.native="addView">Create Referential</v-btn>
            <v-btn color="orange" :disabled="listReferential.length > 0 ? false : true" v-on:click.native="kibana">Visualise Referential<v-icon right>wifi</v-icon></v-btn>
            <v-tooltip right>
              <v-btn slot="activator" flat v-on:click.native="load" icon color="blue lighten-2">
                <v-icon>refresh</v-icon>
              </v-btn>
              <span>Refresh the list</span>
            </v-tooltip>
         </v-flex>
     </v-layout>
     <v-data-table v-bind:headers="headers" :items="listReferential" hide-actions >
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
                            <v-btn v-if="props.item.statusProcess == 'DEGRADED'" color="purple">{{props.item.statusProcess}}</v-btn>
                            <v-btn v-if="props.item.statusProcess == 'ERROR'" color="red">{{props.item.statusProcess}}</v-btn>
                            <v-btn v-if="props.item.statusProcess == 'DISABLE' || props.item.statusProcess == 'INIT'" color="orange">{{props.item.statusProcess}}</v-btn>
                            <v-btn v-if="props.item.statusProcess == 'ENABLE'" color="green">{{props.item.statusProcess}}</v-btn>
                      </v-list-tile-title>
                    </v-list-tile>
                    <v-list-tile v-on:click.native="editReferential(props.item.id)">
                      <v-list-tile-title class="justify-center layout px-0">Edit</v-list-tile-title>
                    </v-list-tile>
                    <v-list-tile :disabled="props.item.statusProcess == 'ENABLE' || props.item.statusProcess == 'ERROR' || props.item.statusProcess == 'DEGRADED'" v-on:click.native="deactivateReferential(props.item.id)">
                      <v-list-tile-title class="justify-center layout px-0">Deactivate</v-list-tile-title>
                    </v-list-tile>
                    <v-list-tile  :disabled="props.item.statusProcess == 'DISABLE' || props.item.statusProcess == 'INIT'" v-on:click.native="activateReferential(props.item.id)">
                      <v-list-tile-title class="justify-center layout px-0">Activate</v-list-tile-title>
                    </v-list-tile>
                    <v-list-tile v-on:click.native="deleteReferential(props.item.id)">
                      <v-list-tile-title class="justify-center layout px-0">Delete</v-list-tile-title>
                    </v-list-tile>
                  </v-list>
               </v-menu>
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
           <td class="text-md-center">
              <v-checkbox :disabled="true" v-model="props.item.processDefinition.isNotificationChange"></v-checkbox>
           </td>
           <td class="text-md-center">
              <v-checkbox :disabled="true" v-model="props.item.processDefinition.isValidationTimeAllField"></v-checkbox>
           </td>
           <td class="text-md-center">
              <v-chip v-if="props.item.processDefinition.fieldChangeValidation" color="blue-grey lighten-3">{{props.item.processDefinition.fieldChangeValidation}}</v-chip>
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
                   { text: 'Key',align: 'center',sortable: 0,value: '', width: '20%'},
                   { text: 'Metadata',align: 'center',sortable: 0,value: '', width: '20%'},
                   { text: 'Process',align: 'center',sortable: 0,value: '', width: '20%'},
                   { text: 'Tracking',align: 'center',sortable: 0,value: '', width: '5%'},
                   { text: 'Validation All',align: 'center',sortable: 0,value: '', width: '5%'},
                   { text: 'Validation Field',align: 'center',sortable: 0,value: '', width: '5%'}
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
        this.load();
        }
      }
    }
  }
</script>
