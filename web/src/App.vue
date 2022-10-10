<template>
  <NavBar></NavBar>
  <router-view/>
</template>

<script>
  import 'bootstrap/dist/css/bootstrap.min.css';
  import 'bootstrap/dist/js/bootstrap.js';
  import NavBar from './components/NavBar.vue';

  export default {
    name:"App",
    components:{
    NavBar,
},
    setup(){
      
    },
  }
</script>

<style>
body{
  background-image: url("@/assets/images/background.jpg");
  background-size: cover;
  font-family:  sans-serif;
}
</style>
