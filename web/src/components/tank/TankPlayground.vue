<template>
    <div class="playground">
        <TankGameMap></TankGameMap>
        <div class="result-board" v-if="show">
        <div class="result-board-text">
                {{index}}
        </div>
        </div>
    </div>
</template>

<script>
import TankGameMap from '@/components/tank/TankGameMap.vue'
import { useStore } from "vuex";
import {ref} from 'vue';

export default {
    components:{
        TankGameMap,
    },
    setup() {
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
    width:700px;
    height:700px;
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