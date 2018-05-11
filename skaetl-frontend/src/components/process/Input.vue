<template>
  <v-card class="mb-5">
    <v-card-title>
      <div class="headline">Describe your input source</div>
    </v-card-title>
    <v-card-text>
      <v-text-field label="Kafka Host" v-model="processInput.host" required
                    :rules="[() => !!processInput.host || 'This field is required']"></v-text-field>
      <v-text-field label="Kafka Port" v-model="processInput.port" required
                    :rules="[() => !!processInput.port || 'This field is required']"></v-text-field>
      <v-text-field label="Topic Name" v-model="processInput.topicInput" required
                    :rules="[() => !!processInput.topicInput || 'This field is required']"></v-text-field>
    </v-card-text>
    <v-card-actions>
      <v-btn color="primary" style="width: 120px" @click.native="$emit('previousStep')">
        <v-icon>navigate_before</v-icon>
        Previous
      </v-btn>
      <v-btn color="primary" style="width: 120px" @click.native="$emit('nextStep')" :disabled="!processInput.host || !processInput.port || !processInput.topicInput">Next
        <v-icon>navigate_next</v-icon>
      </v-btn>
    </v-card-actions>
  </v-card>
</template>


<script>
  export default {
    props: {
      processInput: {
        type: Object,
        required: true
      }
    }
  }
</script>
