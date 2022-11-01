<template>
    <PlayGround></PlayGround>
    <RecordResult v-if="$store.state.record.record_result !== 'none'"></RecordResult>
</template>

<script>
import PlayGround from '@/components/PlayGround.vue';
import RecordResult from '@/components/RecordResultBoard.vue'
import { onUnmounted } from 'vue';
import { useStore } from "vuex";

export default {
    components:{
    PlayGround,
    RecordResult,
},
    setup(){
        const store = useStore();
        onUnmounted(() => {
            store.commit('updateRecordResult', 'none');
        })


        return {
            
        }
    }
}

</script>

<style scoped>
</style>