<template>
  <v-container>
    <v-layout row justify-center align-content-end>
      <v-flex d-flex xs7>

        <v-card height="300" width="300">
          <v-layout justify-center>
            <img  src="../../assets/skaetl.png" align="middle"/>
          </v-layout>
        </v-card>

        <v-card height="300" width="300">
          <v-card-text>

            <br/><h2 class="headline" align="center">Sign in to SkaETL</h2><br/>

            <v-form v-model="valid" ref="form" lazy-validation v-on:submit.prevent="onSubmit">
              <v-text-field name="login" label="Username or email" v-model="login" autofocus="true" required @focus="hideError()"></v-text-field>
              <v-text-field name="password" label="Password" v-model="password"
                :append-icon="pwdVisible ? 'visibility' : 'visibility_off'"
                :append-icon-cb="() => (pwdVisible = !pwdVisible)"
                :type="pwdVisible ? 'password' : 'text'" required @focus="hideError()"></v-text-field>
              <v-layout justify-center>
                <v-btn color="primary" type="submit" :disabled="!login || !password">Sign in</v-btn>
              </v-layout>
            </v-form>

            <v-layout>
              <v-flex xs12 sm12 md12 >
                <v-alert v-model="errorView" xs4 sm4 md4  color="error" icon="warning" value="true">
                  {{ msgError }}
                </v-alert>
              </v-flex>
            </v-layout>

          </v-card-text>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
  export default{
    data () {
         return {
           login: '',
           password: '',
           msgError: '',
           errorView: false,
           pwdVisible: true
         }
    },
    mounted() {
    },
    methods: {
        onSubmit(){
          var app = this;
          this.$http.post('/home/login', {login: this.login, password: this.password}).then(response => {
             var result = response.bodyText;
             if(result === 'OK'){
               app.$store.state.isLoggedIn = true
               window.localStorage.setItem('userDefine',this.login);
               app.$router.push('/main/process');
             }else{
               this.errorView=true;
               this.msgError=result;
             }
          }, response => {
             this.errorView=true;
             this.msgError = "Error during call service";
          });
        },
        hideError(){
          this.errorView=false;
        }
    }
  }
</script>
