<template>
    <div class="result-board">
       <div class="result-board-text">
            {{$store.state.record.record_result}}
       </div>
       <div class="result-board-btn">
            <button type="button" class="btn btn-warning btn-lg" @click="back">
                返回
            </button>
       </div>
    </div>
</template>

<script>
import { useStore } from 'vuex';
import router from '@/router';

export default {
    setup() {
        const store = useStore();


        const back = () => {
           store.commit('updateRecordResult', 'none');
           router.push({name:'record_index'});
        }

        return {
            back,
        }
    }
}

</script>

<style scoped>
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
    padding-top: 5vh;
}

div.result-board-btn {
    text-align: center;
    padding-top: 5vh;
}
</style>