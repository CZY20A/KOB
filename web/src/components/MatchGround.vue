<template>
    <div class="matchground">
        <div class="row">
            <div class="col-6">
                <div class="user-photo">
                    <img :src="$store.state.user.photo" alt="">
                </div>

                <div class="user-username">
                    {{$store.state.user.username}}
                </div>
            </div>
            <div class="col-6">
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


export default {
    components:{
        MatchBoard
    },
    setup() {
        let match_btn_info  = ref("开始匹配");
        const store = useStore();
        let finding = ref("stop");

        const click_match_btn = () => {
            console.log(match_btn_info.value);
            if(match_btn_info.value === '开始匹配'){
                finding.value = 'start';
                match_btn_info.value = '取消';
                store.state.pk.socket.send(JSON.stringify({
                    event:"start-matching",
                }))
            }else{
                match_btn_info.value = '开始匹配'
                store.state.pk.socket.send(JSON.stringify({
                    event:"stop-matching",
                }))
                finding.value = 'stop';
            }
        }

        return {
            match_btn_info,
            click_match_btn,
            finding
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

</style>