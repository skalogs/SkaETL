<template>
  <v-card>
    <v-card-title>
      <b>Language Syntax</b>
      <v-spacer></v-spacer>
      <v-text-field
        v-model="search"
        append-icon="search"
        label="Search"
        single-line
        hide-details
      ></v-text-field></v-card-title>
    <v-card-text>
      <v-data-table :headers="headers" :items="filterFunctions" :search="search">
        <template slot="items" slot-scope="props">
          <td>{{props.item.name}}</td>
          <td>{{props.item.description}}</td>
          <td>{{props.item.example}}</td>
        </template>
      </v-data-table>
    </v-card-text>
  </v-card>
</template>


<script>
  export default {
    data: function () {
      return {
        headers: [
          {text: 'Function Name', value: 'name'},
          {text: 'Description', value: 'description', sortable: false},
          {text: 'Example', value: 'example', sortable: false}
        ],
        filterFunctions: [],
        search: ""
      }
    },
    mounted() {
      this.$http.get('/dsl/filterFunctions', {}).then(response => {
          this.filterFunctions = response.data;
      }, response => {
          this.viewError = true;
          this.msgError = "Error during call service";
        });
      }
  }
</script>
