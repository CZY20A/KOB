<template>
    <div class="result-board">
       <div class="result-board-text">
            {{result}}
       </div>
       <div class="result-board-btn">
            <button type="button" class="btn btn-warning btn-lg" @click="restart">
                再来!
            </button>
       </div>
    </div>
</template>

<script>
import { useStore } from 'vuex';
import {ref} from 'vue';

export default {
    setup() {
        const store = useStore();
        let result = ref("");


        if(store.state.pk.loser === 'all') {
            result.value = "平局"
        } else {
            let loserId = -1;
            if (store.state.pk.loser === 'A') {
                loserId = store.state.pk.a_id;
            }else {
                loserId = store.state.pk.b_id;
            }

            if(loserId === parseInt(store.state.user.id)) {
                result.value = "输了";
            } else {
                result.value = "赢了"
            }
        }

        const restart = () => {
            store.commit("updateStatus", "matching");
            store.commit("updateLoser", "none");
            store.commit("updateOpponent", {
                username: "我的对手",
                photo:"https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            });
            store.commit('updateFound', 'no');
        }

        return {
            result,
            restart,
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