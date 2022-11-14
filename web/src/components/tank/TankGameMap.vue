<template>
    <span class="player player-left">
        <div class="user-photo">
                    <img :src="left_photo" alt="">
                </div>

                <div class="user-username">
                    {{left_name}}
                </div>
    </span>
    <span class="player player-right">
        <div class="user-photo">
                    <img :src="right_photo" alt="">
                </div>

                <div class="user-username">
                    {{right_name}}
                </div>
    </span>
    <div class="gamemap" ref="parent">
        <canvas tabindex="0" ref="canvas"></canvas>
    </div>
</template>

<script>
import {ref, onMounted} from 'vue';
import {TankGameMap} from '@/assets/scripts/tank/TankGameMap';
import { useStore } from 'vuex';

export default {
    setup() {
        let parent = ref(null);
        let canvas = ref(null);
        const store = useStore();



        let left_name = ref("");
        let left_photo = ref("");
        let right_name = ref("");
        let right_photo = ref("");

        let id = parseInt(store.state.user.id);
        if(!store.state.record.is_record){
            if (store.state.pk.a_id === id) {
                left_name.value = "您:";
                left_name.value  += store.state.user.username;
                left_photo.value = store.state.user.photo;
                right_name.value = "您的对手:";
                right_name.value += store.state.pk.opponent_username;
                right_photo.value = store.state.pk.opponent_photo;
            }else {
                right_name.value = "您:";
                right_name.value += store.state.user.username;
                right_photo.value = store.state.user.photo;
                left_name.value += store.state.pk.opponent_username
                left_photo.value = store.state.pk.opponent_photo;
            }
        } else {
            left_name.value = store.state.record.record_a_username;
            left_photo.value = store.state.record.record_a_photo;
            right_name.value = store.state.record.record_b_username;
            right_photo.value = store.state.record.record_b_photo;
        }

        onMounted(() => {
            
            store.commit("updateGameObject", new TankGameMap(canvas.value.getContext('2d'), parent.value, store));
        })

        return {
            left_name,
            left_photo,
            right_name,
            right_photo,
            parent,
            canvas,
        }

        
    }
}

</script>

<style scoped>

div.gamemap {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
}

.player {
    position: fixed;
    z-index: -1;
}

.player-left {
    top: 40vh;
    left: 10vw;
}

.player-right {
    top: 40vh;
    right: 10vw;
}

.player .user-photo {
    text-align: center;
    padding-top: 0;
}

.player .user-photo img {
    border-radius: 50%;
    width: 10vw;
}

.user-username{
    text-align: center;
    font-size: 20px;
    font-weight: 600;
    color: #fff;
}

</style>