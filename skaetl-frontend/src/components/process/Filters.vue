<template>
  <v-card class="mb-5">
    <v-card-title>
      <div>
        <div class="headline">Select your filters</div>
        <div><span class="grey--text"><small>Optional</small></span></div>
      </div>
    </v-card-title>
    <v-card-text>
      <v-flex xs12 sm6 md6>
        <v-text-field label="Name" v-model="currentFilter.name"></v-text-field>
        <v-text-field label="criteria (SQL Like)" v-model="currentFilter.criteria"></v-text-field>
      </v-flex>
      <v-layout row wrap>
        <v-flex xs12 sm12 md12>
          <v-flex v-for="(filter, index) in processFilters">
            <v-chip color="blue-grey lighten-3" small close v-on:input="removeFilter(index)">{{index+1}} - {{filter.name}}</v-chip>
          </v-flex>
        </v-flex>
      </v-layout>
    </v-card-text>
    <v-card-actions>
      <v-btn color="primary" style="width: 120px" @click.native="$emit('previousStep')">
        <v-icon>navigate_before</v-icon>
        Previous
      </v-btn>
      <v-btn color="success" v-on:click.native="addFilter">add filter<v-icon>add</v-icon></v-btn>
      <v-btn color="primary" style="width: 120px" @click.native="$emit('nextStep')">Next
        <v-icon>navigate_next</v-icon>
      </v-btn>
    </v-card-actions>
  </v-card>
</template>


<script>
  export default {
    props: {
      processFilters: {
        type: Array,
        required: true
      }
    },
    data: function(){
      return {
        currentFilter: {}
      }
    },
    methods: {
      addFilter() {
        this.processFilters.push(_.cloneDeep(this.currentFilter));
      },
      removeFilter(index) {
        this.processFilters.splice(index,1);
      }
    }

  }
</script>
