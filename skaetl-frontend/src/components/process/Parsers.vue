<template>

  <v-card class="mb-5">
    <v-card-title>
      <div>
        <div class="headline">Select your parsers</div>
        <div><span class="grey--text"><small>Optional</small></span></div>
      </div>
    </v-card-title>
    <v-card-text>
      <v-dialog v-model="dialog" max-width="500px">
        <v-btn color="success"  slot="activator">add parser
          <v-icon>add</v-icon>
        </v-btn>

        <v-card>
          <v-card-title>
            <span class="headline">{{ formTitle }}</span>
          </v-card-title>
          <v-card-text>
            <v-select label="Choose Parser" v-model="editedItem.typeParser" v-bind:items="typeParser"/>
            <v-text-field label="Grok Pattern" v-model="editedItem.grokPattern" v-show="isGrok()"></v-text-field>
            <v-text-field label="CSV (separated by ;)" v-model="editedItem.schemaCSV" v-show="isCSV()"></v-text-field>
            <v-checkbox label="Active Fail Parser Forward " v-model="editedItem.activeFailForward"></v-checkbox>
            <v-text-field v-if="editedItem.activeFailForward" label="Topic Fail Parser" v-model="editedItem.failForwardTopic" required></v-text-field>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" flat @click.native="close">Cancel</v-btn>
            <v-btn color="blue darken-1" flat @click.native="save">Save</v-btn>
          </v-card-actions>

        </v-card>
      </v-dialog>
    </v-card-text>
    <v-data-table :headers="headers" :items="processParsers" hide-actions>
      <template slot="items" slot-scope="props">
        <td>{{props.item.typeParser}}</td>
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
      <v-btn color="primary" style="width: 120px" @click.native="$emit('nextStep')">Next
        <v-icon>navigate_next</v-icon>
      </v-btn>
    </v-card-actions>
  </v-card>

</template>


<script>
  export default {
    props: {
      processParsers: {
        type: Array,
        required: true
      }
    },
    data: function () {
      return {
        dialog: false,
        typeParser: ["CEF", "NITRO", "GROK", "CSV"],
        editedItem: {},
        editedIndex: -1,
        headers: [
          { text: 'Type', value: 'typeParser'},
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
      isGrok() {
        return this.editedItem.typeParser == "GROK";
      },
      isCSV() {
        return this.editedItem.typeParser == "CSV";
      },
      close () {
        this.dialog = false;
        this.editedItem = _.cloneDeep(this.defaultItem);
        this.editedIndex = -1;
      },
      editItem (item) {
        this.editedIndex = this.processParsers.indexOf(item);
        this.editedItem = _.cloneDeep(item);
        this.dialog = true;
      },
      deleteItem (item) {
        var index = this.processParsers.indexOf(item);
        confirm('Are you sure you want to delete this item?') && this.processParsers.splice(index, 1);
      },

      save() {
        if (this.editedIndex > -1) {
          Object.assign(this.processParsers[this.editedIndex], this.editedItem);
        } else {
          this.processParsers.push(this.editedItem);
        }
        this.close();
      }

    }
  }
</script>
