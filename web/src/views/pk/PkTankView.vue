<template>
    <TankPlayground v-if="store.state.pk.status === 'playing'"></TankPlayground>
    <MatchGround v-if="store.state.pk.status === 'matching'"></MatchGround>
    <ResultBoard v-if="$store.state.pk.loser !== 'none'"></ResultBoard>
</template>

<script>
import TankPlayground from '@/components/tank/TankPlayground.vue'
import { useStore } from 'vuex';
import MatchGround from "../../components/MatchGround.vue";
import ResultBoard from '@/components/ResultBoard.vue';
import {onMounted, onUnmounted} from 'vue'

export default {
    components:{
    TankPlayground,
    MatchGround,
    ResultBoard
},
    setup() {
        const store = useStore();
        store.commit("updateLoser", "none");
        const socketUrl = `ws://172.18.90.64:3000/websocket/tank/${store.state.user.token}/`

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
                } else if(data.event === 'operate') {
                    const game = store.state.pk.gameObject;
                    const [tank0, tank1] = game.tanks;
                    if(data.a_operate !== null || data.a_operate !== '')
                        tank0.update_operate(parseInt(data.a_operate))
                    if(data.b_operate !== null || data.b_operate !== '')
                        tank1.update_operate(parseInt(data.b_operate))
                } else if(data.event === 'unoperate') {
                    const game = store.state.pk.gameObject;
                    const [tank0, tank1] = game.tanks;
                    if(data.a_operate !== null || data.a_operate !== '')
                        tank0.update_unoperate(parseInt(data.a_operate))
                    if(data.b_operate !== null || data.b_operate !== '')
                        tank1.update_unoperate(parseInt(data.b_operate))
                }
            }

            socket.onclose = () => {

            }

        })

        onUnmounted(() => {
            socket.close();
            store.commit('updateStatus', 'matching');
            store.commit('updateFound', 'no');
        })

        return {
            store,
        }
    }
}

</script>

<style scoped>
</style>