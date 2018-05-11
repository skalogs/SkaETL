<template>
 <v-container fluid grid-list-md>
    <v-layout row wrap>
      <v-flex xs12 sm6 md4>
        <v-btn color="primary" v-on:click.native="newGrok">Create Grok Pattern</v-btn>
        <v-btn color="warning" v-on:click.native="reload">Reload</v-btn>
      </v-flex>
    </v-layout>
    <v-layout row wrap>
      <v-flex xs12 sm12 md12>
          <v-text-field label="Search on key" prepend-icon="search" hide-details single-line v-on:keyup.enter="searchKey" v-model="textSearch"></v-text-field>
          <v-flex v-for="grokItem in listGrok">
             <v-layout row wrap>
                 <v-flex xs1 sm1 md1>
                     <v-btn color="red" small v-on:click.native="deletePattern(grokItem.keyPattern,grokItem.valuePattern)">Delete</v-btn>
                 </v-flex>
                 <v-flex xs2 sm2 md2>
                     {{grokItem.keyPattern}}
                 </v-flex>
                 <v-flex xs8 sm8 md8>
                     {{grokItem.valuePattern}}
                 </v-flex>
             </v-layout>
         </v-flex>
      </v-flex>
    </v-layout row wrap>
    <v-layout row wrap>
      <v-flex xs12 sm12 md12 >
        <v-alert v-model="viewError" xs12 sm12 md12  color="error" icon="warning" value="true" dismissible>
             {{ msgError }}
        </v-alert>
       </v-flex>
     </v-layout row wrap>
 </v-container fluid grid-list-md>
</template>


<script>
  export default{
    data () {
         return {
           textSearch: '',
           listGrok: [],
           msgError: '',
           viewError: false,
           headers: [
             { text: 'Key',align:'center',value: 'keyPattern'},
             { text: 'Value',align:'center',value: 'valuePattern'}
           ]
         }
    },
    mounted() {
      this.load();
    },
    methods: {
      load(){
         this.$http.get('/admin/grok/find').then(response => {
              this.listGrok=response.data;
         }, response => {
              this.viewError=true;
             this.msgError = "Error during call service";
         });
      },
      searchKey(value){
        this.$http.get('/admin/grok/find',{params : {filter : this.textSearch}}).then(response => {
            this.listGrok=response.data;
         }, response => {
           this.viewError=true;
           this.msgError = "Error during call service";
         });
      },
      newGrok(){
        this.$router.push('/grok/add');
      },
      reload(){
        this.$http.get('/admin/grok/forceReload').then(response => {
            this.load();
         }, response => {
           this.viewError=true;
           this.msgError = "Error during call service";
         });
      },
      deletePattern(keyPattern,valuePattern){
        this.$http.post('/admin/grok/delete',{key: keyPattern, value: valuePattern }).then(response => {
            this.load();
         }, response => {
           this.viewError=true;
           this.msgError = "Error during call service";
         });
      }
    }
  }
</script>
