<template>
  <v-card class="mb-5">
    <v-card-title>
      <div>
        <div class="headline">Select your filters</div>
        <div><span class="grey--text"><small>Optional</small></span></div>
      </div>
    </v-card-title>
    <v-card-text>
      <v-dialog v-model="dialog" max-width="500px">
        <v-btn color="success" slot="activator">add filter
          <v-icon>add</v-icon>
        </v-btn>

        <v-card>
          <v-card-title>
            <span class="headline">{{ formTitle }}</span>
          </v-card-title>
          <v-card-text>
            <v-text-field label="Name" v-model="editedItem.name"></v-text-field>
            <v-text-field label="criteria (SQL Like)" v-model="editedItem.criteria"></v-text-field>
            <v-checkbox label="Active Fail Topic Forward " v-model="editedItem.activeFailForward"></v-checkbox>
            <v-text-field v-if="editedItem.activeFailForward" label="Topic Fail Filter" v-model="editedItem.failForwardTopic" required></v-text-field>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" flat @click.native="close">Cancel</v-btn>
            <v-btn color="blue darken-1" flat @click.native="save">Save</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-card-text>
    <v-data-table :headers="headers" :items="processFilters" hide-actions>
      <template slot="items" slot-scope="props">
        <td>{{props.item.name}}</td>
        <td>{{props.item.criteria}}</td>
        <td class="justify-center layout px-0">
          <v-btn icon class="mx-0" @click="editItem(props.item)">
            <v-icon color="teal">edit</v-icon>
          </v-btn>
          <v-btn icon class="mx-0" @click="deleteItem(props.item)">
            <v-icon color="pink">delete</v-icon>
          </v-btn>
        </td>
      </template>
    </v-data-table>
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
        dialog: false,
        editedItem: {},
        editedIndex: -1,
        headers: [
          { text: 'Filter Name', value: 'name'},
          { text: 'Criteria', value: 'criteria'},
          { text: 'Actions', value: 'typeParser', sortable: false }
        ],

      }
    },
    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'New Item' : 'Edit Item';
      }
    },
    methods: {
      close () {
        this.dialog = false;
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      },
      editItem (item) {
        this.editedIndex = this.processFilters.indexOf(item);
        this.editedItem = Object.assign({}, item);
        this.dialog = true;
      },
      deleteItem (item) {
        var index = this.processFilters.indexOf(item);
        confirm('Are you sure you want to delete this item?') && this.processFilters.splice(index, 1);
      },

      save() {
        if (this.editedIndex > -1) {
          Object.assign(this.processFilters[this.editedIndex], this.editedItem);
        } else {
          this.processFilters.push(this.editedItem);
        }
        this.close();
      }
    }

  }
</script>
