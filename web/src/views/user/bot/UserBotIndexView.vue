<template>
    <div class="container">
        <div class="row">
            <div class="col-3">
                <div class="card box" style="margin-top:20px;padding: 0px;" >
                    <div class="card-body" style="margin:0px;">
                        <img :src="$store.state.user.photo" alt="" style="width: 100%;height: 100%;"/>
                    </div>
                </div>
            </div>

            <div class="col-9 box">
                
                <!-- Modal -->
                <div class="modal fade" id="add-bot-btn" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                <div class="modal-dialog modal-xl" >
                    <div class="modal-content" style="background: rgba(0,0,0,0.4);">
                    <div class="modal-header">
                        <h5 class="modal-title" id="staticBackdropLabel" style="font-style: italic;">创建Bot</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="mb-3">
                        <label for="add-bot-title" class="form-label">名称</label>
                        <input type="text" class="form-control" id="add-bot-title" placeholder="请输入Bot名称" v-model="botadd.title">
                        </div>
                        <div class="mb-3">
                        <label for="add-bot-description" class="form-label">简介</label>
                        <textarea class="form-control" id="add-bot-descriptio" rows="3" placeholder="请输入Bot简介" v-model="botadd.description"></textarea>
                        </div>
                        <div class="mb-3">
                        <label for="add-bot-code" class="form-label">代码</label>
                        <VAceEditor
                            id="add-bot-code"
                            v-model:value="botadd.content"
                            @init="editorInit"
                            lang="c_cpp"
                            theme="monokai"
                                        :options="{
                                            enableBasicAutocompletion: true,
                                            enableSnippets: true,
                                            enableLiveAutocompletion: true,
                                            fontSize: 17,
                                            tabSize: 2,
                                            showPrintMargin: false,
                                            highlightActiveLine: true,
                                            }"
                            style="height: 300px" />
                        </div>
                    </div>
                    <div class="modal-footer">
                        <div class="message" style="color:red">{{botadd.message}}</div>
                        <button type="button" class="btn btn-primary create" style="background:rgba(13,110,253,0.3)" @click="add_bot">创建</button>
                        <button type="button" class="btn btn-danger delete" data-bs-dismiss="modal" style="background:rgba(220,53,69,0.3)">取消</button>
                    </div>
                    </div>
                </div>
                </div>

                    <h3>
                        我的Bot 
                        <span>
                            <button type="button" class="btn btn-primary float-end create" data-bs-toggle="modal" data-bs-target="#add-bot-btn" style="background:rgba(13,110,253,0.3)">创建Bot</button>
                        </span>
                    </h3>
                   <div class="list" v-for="bot in bots" :key="bot.id">
                        <div class="content">
                        <div style="margin-bottom:5px;margin-left: 5px;"><span>{{bot.title}}</span> <button type="button" data-bs-toggle="modal" :data-bs-target="'#update-bot-btn-'+bot.id" class="btn btn-warning float-end update" style="background: rgba(255,193,7,0.3);color: white;margin-right: 5px;">修改</button></div>
                        <div style="margin-bottom:5px;margin-left: 5px;"><span>创建时间:{{bot.createtime}}</span> </div>
                        <div style="margin-bottom:10px;margin-left: 5px;"><span>rating:{{bot.rating}}</span><button @click="remove_bot(bot.id)" type="button" class="btn btn-danger float-end delete" style="background:rgba(220,53,69,0.3);margin-right: 5px;">删除</button></div>
                        </div>


                        <!-- Modal -->
                        <div class="modal fade" :id="'update-bot-btn-'+bot.id" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                        <div class="modal-dialog modal-xl" >
                            <div class="modal-content" style="background: rgba(0,0,0,0.4);">
                            <div class="modal-header">
                                <h5 class="modal-title" style="font-style: italic;">修改Bot</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <div class="mb-3">
                                <label :for="'update-bot-title'+bot.id" class="form-label">名称</label>
                                <input type="text" class="form-control" :id="'update-bot-title'+bot.id" placeholder="请输入Bot名称" v-model="bot.title">
                                </div>
                                <div class="mb-3">
                                <label :for="'update-bot-description'+bot.id" class="form-label">简介</label>
                                <textarea class="form-control" :id="'update-bot-description'+bot.id" rows="3" placeholder="请输入Bot简介" v-model="bot.description"></textarea>
                                </div>
                                <div class="mb-3">
                                <label :for="'update-bot-content'+bot.id" class="form-label">代码</label>
                                <VAceEditor
                                        :id="'update-bot-content'+bot.id"
                                        v-model:value="bot.content"
                                        @init="editorInit"
                                        lang="c_cpp"
                                        theme="monokai"
                                        :options="{
                                            enableBasicAutocompletion: true,
                                            enableSnippets: true,
                                            enableLiveAutocompletion: true,
                                            fontSize: 17,
                                            tabSize: 2,
                                            showPrintMargin: false,
                                            highlightActiveLine: true,
                                            }"
                                        style="height: 300px" />
                                </div>
                            </div>
                            <div class="modal-footer">
                                <div class="message" style="color:red">{{message}}</div>
                                <button type="button" class="btn btn-primary create" style="background:rgba(13,110,253,0.3)" @click="update_bot(bot)">修改</button>
                                <button type="button" class="btn btn-danger delete" data-bs-dismiss="modal" style="background:rgba(220,53,69,0.3)" @click="refresh_bots">取消</button>
                            </div>
                            </div>
                        </div>
                        </div>


                   </div>
            </div>
        </div>
    </div>
</template>

<script>
import $ from 'jquery';
import { useStore } from 'vuex';
import {reactive, ref} from 'vue'
import {Modal} from 'bootstrap/dist/js/bootstrap' 
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';

export default{
    components:{
        VAceEditor,
    },
    setup(){
        ace.config.set(
            "basePath", 
            "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/"
        )


        const store = useStore();
        let bots = ref([]);
        let message = ref("")

        const botadd  = reactive({
            title:"",
            description:"",
            content:"",
            message:"",
        })

        const add_bot = () => {
            botadd.message = "";
            console.log(botadd)
            $.ajax({
                type:"POST",
                url:"http://localhost:3000/user/bot/add/",
                headers:{
                        Authorization:"Bearer " + store.state.user.token,
                },
                data:{
                    title:botadd.title,
                    description:botadd.description,
                    content:botadd.content,
                },
                success(resp){
                    if(resp.message === 'success'){
                        botadd.content = "";
                        botadd.description = "";
                        botadd.title = "";
                        Modal.getInstance("#add-bot-btn").hide();
                        refresh_bots();
                    }else{
                        botadd.message = resp.message;
                    }
                }
            })
        }

        const remove_bot = (id) => {
            $.ajax({
                type:"POST",
                url:"http://localhost:3000/user/bot/remove/",
                headers:{
                        Authorization:"Bearer " + store.state.user.token,
                },
                data:{
                    id:id,
                },
                success(){
                    refresh_bots();
                }
            })

        }

        const update_bot = (bot) => {
            message.value = "";
            $.ajax({
                type:"POST",
                url:"http://localhost:3000/user/bot/update/",
                headers:{
                        Authorization:"Bearer " + store.state.user.token,
                },
                data:{
                    id:bot.id,
                    title:bot.title,
                    content:bot.content,
                    description:bot.description,
                },
                success(resp){
                    if(resp.message === 'success'){
                        Modal.getInstance("#update-bot-btn-"+bot.id).hide();
                        refresh_bots();
                    }else{
                        message.value = resp.message;
                    }
                }
            })
        }

        const refresh_bots = () => {
            message.value = "";
            $.ajax({
                type:"get",
                url:"http://localhost:3000/user/bot/getlist/",
                headers:{
                        Authorization:"Bearer " + store.state.user.token,
                },
                success(resp){
                    bots.value = resp;
                }
            })
        }
        refresh_bots();

        return {
            bots,
            botadd,
            add_bot,
            remove_bot,
            message,
            update_bot,
            refresh_bots,
        }
    }
}

</script>

<style scoped>
    .create:hover{
        background-color: rgb(13,110,253) !important;
        
    }

    .update:hover{
        background-color: rgb(255,193,7) !important;
    }

    .delete:hover{
        background-color: rgb(220,53,69) !important;
    }

   .box{
    background: rgba(0,0,0,0.4);
    box-shadow: 0 25px 45px rgba(0, 0, 0, 0.3);
    border: 1px solid rgba(255, 255, 255, 0.5);
    border-radius: 10px;
    margin-top: 20px;
    padding: 20px;
    color: white;
   }
   .box h3{
    font-style: italic;
    margin-bottom: 20px;
   }
   .list .content{
    background: rgba(0,0,0,0.6);
    padding: 10px 0;
    margin:10px 0;
    border-radius: 10px;
    cursor: pointer;
   }
   .list .content:hover{
        background:rgba(255, 255, 255, 0.3);
        box-shadow: -15px 30px 50px rgba(0,0,0,0.3);
        transform: scale(1.05) translateX(10px) translateY(-15px);
        transition: all 0.5s ease;
   }
</style>