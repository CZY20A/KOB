<template>
    <div class="container content-field" style="margin:20px auto">
        <div class="card">
            <span>蛇蛇作战</span>
            <div class="content">
                <h3>蛇蛇作战</h3>
                <p>
                    前十回合每一步蛇增长一格,后十回合每三步蛇增长一格,非法输入、撞墙、撞到敌方身体皆会导致死亡,
                    为了保证公平只有双方都完成输入后两条蛇才会行动(5s内输入否则视为认输)。
                </p>
                <router-link :to="{name:'snake'}">进入游戏</router-link>
            </div>
        </div>
        <div class="card">
            <span>待定</span>
            <div class="content">
                <h2>02</h2>
                <h3>Card Two</h3>
                <p>Realistic glass card hover effect, realistic glass card hover effect, realistic glass card hover effect.</p>
                <a href="#">Read More</a>
            </div>
        </div>
        <div class="card">
            <span>待定</span>
            <div class="content">
                <h2>03</h2>
                <h3>Card Three</h3>
                <p>Realistic glass card hover effect, realistic glass card hover effect, realistic glass card hover effect, realistic glass card hover effect.</p>
                <a href="#">Read More</a>
            </div>
        </div>
    </div>
</template>

<script>


export default {
    components:{
        
    },
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