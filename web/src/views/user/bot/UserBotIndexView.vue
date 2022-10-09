<template>
    <ContentField>
        我的Bot
    </ContentField>
</template>

<script>
import $ from "jquery";
import ContentField from "../../../components/ContentField.vue";
import { useStore } from 'vuex';

export default{
    components:{
        ContentField,
    },
    setup(){
        const store = useStore();
        // $.ajax({
        //     type:"POST",
        //     url:"http://localhost:3000/user/bot/add/",
        //     headers:{
        //             Authorization:"Bearer " + store.state.user.token,
        //     },
        //     data:{
        //         title:"bot的标题",
        //         content:"bot的代码",
        //         description:"bot的描述",
        //     },
        //     success(resp){
        //         console.log(resp);
        //     },
        //     error(resp){
        //         console.log(resp);
        //     }
        // })
            // $.ajax({
            //     type:"POST",
            //     url:"http://localhost:3000/user/bot/remove/",
            //     headers:{
            //         Authorization:"Bearer " + store.state.user.token,
            //     },
            //     data:{
            //         id:1
            //     },
            //     success(resp){
            //         console.log(resp);
            //     }
            // })
            // $.ajax({
            //     type:"POST",
            //     url:"http://localhost:3000/user/bot/update/",
            //     headers:{
            //         Authorization:"Bearer " + store.state.user.token,
            //     },
            //     data:{
            //         id:10,
            //         title:"bot的标题",
            //         description:"bot的描述",
            //         content:"bot的代码",
            //     },
            //     success(resp){
            //         console.log(resp);
            //     }
            // })
            $.ajax({
                type:"GET",
                url:"http://localhost:3000/user/bot/getlist/",
                headers:{
                    Authorization:"Bearer " + store.state.user.token,
                },
                success(resp){
                    console.log(resp);
                }
            })
    }
}

</script>

<style scoped>
    
</style>