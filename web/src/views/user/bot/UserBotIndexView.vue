<template>
    <div class="container">

       

        <div class="row">
            <div class="col-3">
                <div class="card box" style="margin-top:20px;padding: 0px;" >
                    <div class="card-body" style="margin:0px;">
                        <img :src="$store.state.user.photo" alt="" style="width: 100%;height: 100%;"/>
                        <span class="rating">rating: {{$store.state.user.rating}}</span>
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
                                    <label class="form-label">所用游戏</label>
                                    <select class="form-select" aria-label="Default select example" v-model="botadd.gameId">
                                        <option v-for="game in allGame" :key="game.id" :value="game.id" :selected="game.id === currentGameId? true:false">{{game.title}}</option>
                                    </select>
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
                        <button type="button" class="btn btn-danger delete" data-bs-dismiss="modal" style="background:rgba(220,53,69,0.3)" @click="() => botadd.message = '' ">取消</button>
                    </div>
                    </div>
                </div>
                </div>

                    






                <nav class="navbar navbar-expand-lg">
                <div class="container" style="width:100%;background-color: rgba(255,255,255,0.6);">
                    <a class="navbar-brand" style="font-style:italic;margin-right: 0;">我的Bot</a>
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarText">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0" style="margin:0 auto">
                        <li class="nav-item" v-for="gameinfo in gameInfos" :key="gameinfo.id">
                            <button type="button" :class="'btn ' + (gameinfo.id===currentGameId?'btn-dark':'btn-outline-dark')" style="margin-left:20px" @click="changeGame(gameinfo.id)">{{gameinfo.title}}</button>
                        </li>
                    </ul>
                    <span class="navbar-text">
                        <button class="btn btn-primary create" data-bs-toggle="modal" data-bs-target="#add-bot-btn">创建Bot</button>
                    </span>
                    </div>
                </div>
                </nav>

                    
                   <div class="list" v-for="bot in bots" :key="bot.id">
                        <div class="content">
                        <div style="margin-bottom:5px;margin-left: 5px;"><span>{{bot.title}}</span> <button type="button" data-bs-toggle="modal" :data-bs-target="'#update-bot-btn-'+bot.id" class="btn float-end update" style="background: rgba(255,193,7);color: white;margin-right: 5px;">修改</button></div>
                        <div style="margin-bottom:5px;margin-left: 5px;"><span>创建时间:{{bot.createtime}}</span> </div>
                        <div style="margin-bottom:5px;margin-left: 5px;"><span>修改时间:{{bot.modifytime}}</span> </div>
                        <div style="margin-bottom:10px;margin-left: 5px;"> <span>所用游戏:{{map.get(bot.gameId)}}</span>  <button @click="remove_bot(bot.id)" type="button" class="btn float-end delete" style="background:rgba(220,53,69);margin-right: 5px;color:white;">删除</button></div>
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
                                    <label class="form-label">所用游戏</label>
                                    <select class="form-select" aria-label="Default select example" v-model="bot.gameId">
                                        <option v-for="game in allGame" :key="game.id" :value="game.id" :selected="game.id === bot.gameId? true:false">{{game.title}}</option>
                                    </select>
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
import router from "@/router/index";

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
            gameId:1,
        })

        botadd.content = "package com.kob.botrunningsystem.utils;\n" +
                "\n" +
                "/**\n* 当前支持java语言\n" +
                "* input参数是本局对战的所有信息该参数由系统提供,格式为：\n" +
                "* 地图信息#我的x坐标#我的y坐标#(我的操作)#对手的x坐标#对手的y坐标#(对手的操作) \n" +
                "* 地图共13行14列x表示竖轴y表示横轴从0开始，地图信息为一个13*14的一维字符串，若该处为墙则为1，否则为0\n"+
                "*/ \n" +
                "public class Bot implements com.kob.botrunningsystem.utils.BotInterface{\n" +
                "    @Override\n" +
                "    public Integer nextMove(String input) { //返回值:0123分别表示上右下左 \n" +
                "        return 0;\n" +
                "    }\n\n" +
                "    //该函数不需要更改后台执行时使用\n"+
                "    @Override\n" +
                "    public Integer get() {\n" +
                "        File file = new File(\"input.txt\");\n" +
                "        try {\n" +
                "            Scanner sc = new Scanner(file);\n" +
                "            return nextMove(sc.next());\n" +
                "        } catch (FileNotFoundException e) {\n" +
                "            throw new RuntimeException(e);\n" +
                "        }\n" +
                "    }"
                "}\n";

        const add_bot = () => {
            botadd.message = "";
            $.ajax({
                type:"POST",
                url:"https://app3844.acapp.acwing.com.cn/api/user/bot/add/",
                headers:{
                        Authorization:"Bearer " + store.state.user.token,
                },
                data:{
                    title:botadd.title,
                    description:botadd.description,
                    content:botadd.content,
                    gameId:botadd.gameId,
                },
                success(resp){
                    if(resp.message === 'success'){
                        botadd.content = "package com.kob.botrunningsystem.utils;\n" +
                "\n" +
                "/**\n* 当前支持java语言\n" +
                "* input参数是本局对战的所有信息该参数由系统提供,格式为：\n" +
                "* 地图信息#我的x坐标#我的y坐标#(我的操作)#对手的x坐标#对手的y坐标#(对手的操作) \n" +
                "* 地图共13行14列x表示竖轴y表示横轴从0开始，地图信息为一个13*14的一维字符串，若该处为墙则为1，否则为0\n"+
                "*/ \n" +
                "public class Bot implements com.kob.botrunningsystem.utils.BotInterface{\n" +
                "    @Override\n" +
                "    public Integer nextMove(String input) { //返回值:0123分别表示上右下左 \n" +
                "        return 0;\n" +
                "    }\n" +
                "    @Override\n\n" +
                "    //该函数不需要更改后台执行时使用\n"+
                "    public Integer get() {\n" +
                "        File file = new File(\"input.txt\");\n" +
                "        try {\n" +
                "            Scanner sc = new Scanner(file);\n" +
                "            return nextMove(sc.next());\n" +
                "        } catch (FileNotFoundException e) {\n" +
                "            throw new RuntimeException(e);\n" +
                "        }\n" +
                "    }"
                "}\n";
                        botadd.description = "";
                        botadd.title = "";
                        botadd.gameId = 1;
                        Modal.getInstance("#add-bot-btn").hide();
                        refresh_bots();
                    }else{
                        botadd.message = resp.message;
                    }
                },
                error() {
                    alert("账号已在别的地方登录或登录过期，请重新登录");
                    store.commit("logout");
                    router.push({name:"user_account_login"});
                }
            })
        }

        const remove_bot = (id) => {
            $.ajax({
                type:"POST",
                url:"https://app3844.acapp.acwing.com.cn/api/user/bot/remove/",
                headers:{
                        Authorization:"Bearer " + store.state.user.token,
                },
                data:{
                    id:id,
                },
                success(){
                    refresh_bots();
                },
                error() {
                    alert("账号已在别的地方登录或登录过期，请重新登录");
                    store.commit("logout");
                    router.push({name:"user_account_login"});
                }
            })

        }

        const update_bot = (bot) => {
            message.value = "";
            $.ajax({
                type:"POST",
                url:"https://app3844.acapp.acwing.com.cn/api/user/bot/update/",
                headers:{
                        Authorization:"Bearer " + store.state.user.token,
                },
                data:{
                    id:bot.id,
                    title:bot.title,
                    content:bot.content,
                    description:bot.description,
                    gameId:bot.gameId,
                },
                success(resp){
                    if(resp.message === 'success'){
                        Modal.getInstance("#update-bot-btn-"+bot.id).hide();
                        refresh_bots();
                    }else{
                        message.value = resp.message;
                    }
                },
                error() {
                    alert("账号已在别的地方登录或登录过期，请重新登录");
                    store.commit("logout");
                    router.push({name:"user_account_login"});
                }
            })
        }

        let gameInfos = ref([]);
        let currentPage = ref(1);
        const get_game_pages = (page) => {
            $.ajax({
                type:"GET",
                url:"https://app3844.acapp.acwing.com.cn/api/game/infopage/",
                headers:{
                        Authorization:"Bearer " + store.state.user.token,
                },
                data:{
                    page: page,
                    size: 3,
                },
                success(resp){
                    gameInfos.value = resp;
                },
                error() {
                    alert("账号已在别的地方登录或登录过期，请重新登录");
                    store.commit("logout");
                    router.push({name:"user_account_login"});
                }
            })
        }
        get_game_pages(currentPage.value);
        console.log(gameInfos)
        

        let currentGameId = ref(1);
        const refresh_bots = () => {
            message.value = "";
            $.ajax({
                type:"get",
                url:"https://app3844.acapp.acwing.com.cn/api/user/bot/getlistByGameId/",
                headers:{
                        Authorization:"Bearer " + store.state.user.token,
                },
                data:{
                    gameId:currentGameId.value,
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

        let map = new Map();
        let allGame = ref([]);
        $.ajax({
                type:"GET",
                url:"https://app3844.acapp.acwing.com.cn/api/game/all/",
                headers:{
                        Authorization:"Bearer " + store.state.user.token,
                },
                success(resp){
                    allGame.value = resp;
                    for(let i = 0; i < resp.length; ++i)
                        map.set(resp[i].id, resp[i].title);
            
                },
                error() {
                    store.commit("logout");
                    router.push({name:"user_account_login"});
                }
            })

        const changeGame = (gameId) => {
            currentGameId.value = gameId;
            botadd.gameId = gameId;
            refresh_bots();
        }

        
        return {
            bots,
            botadd,
            add_bot,
            remove_bot,
            message,
            update_bot,
            refresh_bots,
            allGame,
            map,
            gameInfos,
            currentPage,
            currentGameId,
            changeGame,
        }
    }
}

</script>

<style scoped>
    .create:hover{
        background-color: rgb(13,110,253,0.5) !important;
        
    }

    .update:hover{
        background-color: rgb(255,193,7,0.5) !important;
    }

    .delete:hover{
        background-color: rgb(220,53,69,0.5) !important;
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
   /* .list .content:hover{
        background:rgba(255, 255, 255, 0.3);
        box-shadow: -15px 30px 50px rgba(0,0,0,0.3);
        transform: scale(1.05) translateX(10px) translateY(-15px);
        transition: all 0.5s ease;
   } */

   .rating {
    font-style: italic;
    font-size: 20px;
    font-weight: 600;
   }
</style>