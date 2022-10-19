<template>
    <div class="container" style="margin:20px auto">
        <button class="carousel-control-prev" type="button">
            <span class="carousel-control-prev-icon" aria-hidden="true" style="background-color:black"></span>
            <span class="visually-hidden">Previous</span>
        </button>

        <div class="card" v-for="gameInfo in gameInfos" :key="gameInfo.id">
            <span>{{gameInfo.title}}</span>
            <div class="content">
                <h3>{{gameInfo.title}}</h3>
                <p>
                    {{gameInfo.description}}
                </p>
                <router-link :to="{name:gameInfo.name}">进入游戏</router-link>
            </div>
        </div>

        <div class="card" v-for="un in undetermined" :key="un.id">
            <span>{{un.title}}</span>
            <div class="content">
                <h3>{{un.title}}</h3>
                <p>{{un.description}}</p>
                <a href="#">Read More</a>
            </div>
        </div>


        <button class="carousel-control-next" type="button">
            <span class="carousel-control-next-icon" aria-hidden="true" style="background-color:black"></span>
            <span class="visually-hidden">Next</span>
        </button>
    </div>
</template>

<script>
import $ from 'jquery';
import { ref } from 'vue';
import { useStore } from 'vuex';

export default {
    components:{
        
    },
    setup() {
        const store = useStore();
        let gameInfos = ref([]);
        let undetermined = ref([]);
        let currentPage = ref(1);

            $.ajax({
                type:"GET",
                url:"http://localhost:3000/game/infopage/",
                headers:{
                        Authorization:"Bearer " + store.state.user.token,
                },
                data:{
                    page: currentPage.value,
                    size: 3,
                },
                success(resp){
                    gameInfos.value = resp;
                    let n = 3 - gameInfos.value.length;
                    let undetermined_tmp = [];
                    for(let i = 0; i < n; ++i){
                        undetermined_tmp.push({
                            id:i,
                            title:"待定",
                            description:"这将会有一个伟大的游戏",
                        })
                    }
                    undetermined.value = undetermined_tmp;
                }
            })

        return {
            gameInfos,
            undetermined
        }
    }
}

</script>

<style scoped>


*{
    /* 初始化 */
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    /* 设置字体 */
    font-family: "Poppins";
}
body{
    /* 100%窗口高度 */
    min-height: 100vh;
    /* 弹性布局 水平+垂直居中 */
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #161626;
}
/* 给背景增加两个渐变圆 */
body::before{
    content: "";
    /* 绝对定位 */
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    /* 渐变背景 */
    background: linear-gradient(#2193b0,#6dd5ed);
    /* 将元素剪切微一个圆形【30%表示圆的直径】【right 70%表示圆心位置】 */
    clip-path: circle(30% at right 70%);
}
body::after{
    content: "";
    /* 绝对定位 */
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    /* 渐变背景 */
    background: linear-gradient(#ee9ca7,#ffdde1);
    /* 将元素剪切微一个圆形【30%表示圆的直径】【right 70%表示圆心位置】 */
    clip-path: circle(20% at 10% 10%);
}
.container{
    font-style: italic;
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-wrap: wrap;
    z-index: 1;
    top: 10vh;
}
.container .card{
    /* 相对定位 */
    position: relative;
    width: 280px;
    height: 400px;
    background-color: rgba(255,255,255,0.1);
    margin: 30px;
    border-radius: 15px;
    /* 阴影 */
    box-shadow: 20px 20px 50px rgba(0,0,0,0.5);
    /* 溢出隐藏 */
    overflow: hidden;
    display: flex;
    justify-content: center;
    align-items: center;
    border-top: 1px solid rgba(255,255,255,0.5);
    border-left: 1px solid rgba(255,255,255,0.5);
    /* 背景模糊 */
    backdrop-filter: blur(5px);
}

.container .card span{
    position: absolute;
    font-size: 20px;
    color: #fff;
    font-weight: 500;
    font-style: italic;
}

.container .card .content{
    background: url("@/assets/images/gameback.jpeg") 45% 100%;
    width: 100%;
    height: 100%;
    padding: 20px;
    text-align: center;
    /* 默认下移+隐藏 */
    transform: translateY(100px);
    opacity: 0;
    transition: 0.5s;
}
.container .card:hover .content{
    /* 鼠标移入，上移+显示 */
    transform: translateY(0);
    opacity: 1;
}
.container .card .content h2{
    position: absolute;
    top: -80px;
    right: 25px;
    font-size: 128px;
    color: rgba(255,255,255,0.05);
}
.container .card .content h3{
    font-size: 28px;
    color: #fff;
    font-weight: bolder;
}
.container .card .content p{
    font-size: 16px;
    color: #000;
    font-weight: bold;
    margin: 10px 0 15px 0;
}
.container .card .content a{
    position: relative;
    display: inline-block;
    padding: 8px 20px;
    margin-top: 15px;
    background-color: #fff;
    color: #000;
    text-decoration: none;
    border-radius: 20px;
    font-weight: 500;
    box-shadow: 0 5px 15px rgba(0,0,0,0.2);
}

</style>