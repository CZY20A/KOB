<template>
    <div class="matchground">
        <div class="row">
            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.user.photo" alt="">
                </div>

                <div class="user-username">
                    {{$store.state.user.username}}
                </div>
            </div>

            <div class="col-4">
                <div class="user-select-bot">
                    <select class="form-select" aria-label="Default select example" v-model="select_bot">
                    <option selected value="-1">亲自出马</option>
                    <option v-for="bot in bots" :key="bot.id" :value="bot.id">{{bot.title}}</option>
                    </select>
                </div>
            </div>

            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.pk.opponent_photo" alt="">
                </div>

                <div class="user-username">
                    {{$store.state.pk.opponent_username}}
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-12" style="text-align:center;padding-top: 20vh;">
                <button type="button" class="btn start" @click="click_match_btn">{{match_btn_info}}</button>
            </div>
        </div>
    </div>
    <MatchBoard v-if="finding === 'start'"></MatchBoard>
</template>

<script>
import { ref } from 'vue';
import { useStore } from 'vuex';
import MatchBoard from '@/components/MatchBoard.vue'
import $ from 'jquery';
import router from "@/router/index";

export default {
    components:{
        MatchBoard
    },
    setup() {
        let match_btn_info  = ref("开始匹配");
        const store = useStore();
        let finding = ref("stop");
        let bots = ref([]);
        let select_bot = ref("-1");

        const click_match_btn = () => {
            if(match_btn_info.value === '开始匹配'){
                $.ajax({
                    type:"GET",
                    url:"http://172.18.90.64:3000/api/heartbeat/",
                     headers:{
                        Authorization:"Bearer " + store.state.user.token,
                    },
                    error() {
                        alert("账号已在别的地方登录或登录过期，请重新登录");
                        store.commit("logout");
                        router.push({name:"user_account_login"});
                    }
                })

                finding.value = 'start';
                match_btn_info.value = '取消';
                store.state.pk.socket.send(JSON.stringify({
                    event:"start-matching",
                    bot_id:select_bot.value,
                }))
            }else{
                match_btn_info.value = '开始匹配'
                store.state.pk.socket.send(JSON.stringify({
                    event:"stop-matching",
                }))
                finding.value = 'stop';
            }
        }

        let gameId = store.state.matchGameInfo.id;
        const refresh_bots = () => {
            $.ajax({
                type:"get",
                url:"http://172.18.90.64:3000/api/user/bot/getlistByGameId/",
                headers:{
                        Authorization:"Bearer " + store.state.user.token,
                },
                data: {
                    gameId: gameId,                   
                },
                success(resp){
                    bots.value = resp;
                },
                error() {
                    alert("账号已在别的地方登录或登录过期，请重新登录");
                    store.commit("logout");
                    router.push({name:"user_account_login"});
                }
            })
        }
        refresh_bots();


        return {
            match_btn_info,
            click_match_btn,
            finding,
            bots,
            select_bot,
        }
    }
}

</script>

<style scoped>
div.matchground{
    width: 60vw;
    height: 70vh;
    margin: 40px auto;
    background-color: rgba(50,50,50, 0.5);
}

div.user-photo{
    text-align: center;
    padding-top: 10vh;
}

div.user-photo img {
    border-radius: 50%;
    width: 20vh;
}

div.user-username{
    text-align: center;
    font-size: 24px;
    font-weight: 600;
    color: #fff;
    padding-top: 2vh;
}

.start{
    height: 6vh;
    width:9vw;
    background-color: #fff;
    color: #000;
    text-decoration: none;
    border-radius: 20px;
    font-weight: 500;
    box-shadow: 0 5px 15px rgba(0,0,0,0.2);
    letter-spacing: 2px;
}

.start:hover{
    background-color: rgba(255, 255, 255, 0.7);
}

div.user-select-bot {
    padding-top: 15vh;
}

div.user-select-bot > select {
    width: 60%;
    margin:0 auto;
}

</style>