<template>
  <v-expansion-panel expand>
    <v-expansion-panel-content >
      <div slot="header">Language Syntax</div>
      <v-card color="grey lighten-3">
        <v-card-title>
          <b>List of functions</b>
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
    </v-expansion-panel-content>
  </v-expansion-panel>
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
