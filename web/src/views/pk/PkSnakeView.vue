<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'"></PlayGround>
    <MatchGround v-if="$store.state.pk.status === 'matching'"></MatchGround>
    <ResultBoard v-if="$store.state.pk.loser !== 'none'"></ResultBoard>
    
</template>

<script>
import PlayGround from '@/components/PlayGround.vue';
import { onMounted, onUnmounted } from 'vue';
import { useStore } from "vuex";
import router from "@/router/index";
import ResultBoard from '@/components/ResultBoard.vue';
import MatchGround from "../../components/MatchGround.vue";

export default {
    components:{
    PlayGround,
    MatchGround,
    ResultBoard,
},
    setup(){
        const store = useStore();
        store.commit("updateLoser", "none");
        const socketUrl =  `ws://172.18.90.64:3000/websocket/${store.state.user.token}/`


        let socket = null;
        onMounted(() => {
            store.commit('updateOpponent', {
                username: "我的对手",
                photo:"https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            })
            socket = new WebSocket(socketUrl);

            socket.onopen = () => {
                store.commit('updateSocket', socket);
            }

            socket.onmessage = msg => {
                const data = JSON.parse(msg.data);
                if(data.event === 'start-matching') { //匹配成功
                    store.commit('updateFound', 'yes');
                    store.commit('updateOpponent', {
                        username: data.opponent_username,
                        photo: data.opponent_photo,
                    });
                    store.commit('updateGame', data.game);
                    setTimeout(() => {
                        store.commit('updateStatus', 'playing');
                    }, 2000)
                    console.log(data.game)
                } else if (data.event === 'move') {
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes;
                    snake0.set_direction(data.a_direction);
                    snake1.set_direction(data.b_direction);
                } else if (data.event === 'result') {
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes;
                    store.commit("updateLoser", data.loser);
                    if(data.loser === 'all' || data.loser === 'A') {
                        snake0.status = 'die';
                    }
                    if(data.loser === 'all' || data.loser === 'B') {
                        snake1.status = 'die';
                    }
                } else if (data.event === 'logout') {
                    store.dispatch('logout');
                    router.push({name:'home'});
                }
            }

            socket.onclose = () => {

            }
        })

        onUnmounted(() => {
            socket.close();
            store.commit('updateStatus', 'matching');
        })

        return {
            
        }
    }
}

</script>

<style scoped>
</style>