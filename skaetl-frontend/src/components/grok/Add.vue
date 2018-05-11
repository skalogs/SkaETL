<template>
 <v-container fluid grid-list-md>
    <v-layout row wrap>
     <div>
        <div class="headline">Create your own Grok Pattern</div>
     </div>
    </v-layout>
    <v-layout row wrap>
      <v-flex xs12 sm4 md4>
        <v-text-field label="Name" v-model="nameGrok"></v-text-field>
      </v-flex>
    </v-layout>
    <v-layout row wrap>
      <v-flex xs12 sm12 md12>
        <v-text-field name="Value" label="value" textarea v-model="dataGrok"></v-text-field>
      </v-flex>
    </v-layout>
    <v-layout row wrap>
      <v-flex xs12 sm4 md4>
        <v-btn color="primary" v-on:click.native="create">Create</v-btn>
      </v-flex>
    </v-layout>
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
           nameGrok: '',
           dataGrok: '',
           msgError: '',
           viewError: false
         }
    },
    mounted() {
      //Nothing
    },
    methods: {
      create(value){
        this.$http.post('/admin/grok/create',{key : this.nameGrok, value: this.dataGrok }).then(response => {
            this.$router.push('/grok/view');
         }, response => {
           this.viewError=true;
           this.msgError = "Error during call service";
         });
      }
    }
  }
</script>
