<template>
    <div class="playground">
        <GameMap></GameMap>
    <div class="result-board" v-if="show && !$store.state.record.is_record">
       <div class="result-board-text">
            {{index}}
       </div>
    </div>
    </div>
</template>

<script>
import { useStore } from "vuex";
import {ref} from 'vue';
import GameMap from "./GameMap.vue";

export default {
    components:{
        GameMap,
    },
    setup(){
        const store = useStore();
        let show = ref(true);
        let index = parseInt(store.state.user.id) === store.state.pk.a_id ? "你的位置在左下角":"你的位置在右上角";

        setTimeout(() => {
            show.value = false;
        }, 2000);

        return {
            index,
            show,
        }
    }
}

</script>

<style scoped>

div.playground{
    width:60vw;
    height:70vh;
    margin:40px auto;
}

.result-board {
    position: fixed;
    top:30vh;
    left: 35vw;
    height: 30vh;
    width: 30vw;
    background-color: rgba(50,50,50,0.5);
}

div.result-board-text {
    text-align: center;
    color: #fff;
    font-size: 50px;
    font-style: italic;
    font-weight: 600;
    padding-top: 9vh;
}

</style>
