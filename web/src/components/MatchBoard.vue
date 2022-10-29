<template>
        
        <div :class="'loading-inner ' + (store.state.pk.found === 'no'?'noFound':'found')">
            <div class="loading-cover">
                
            </div>
        </div>
</template>

<script>
import { useStore } from 'vuex';



export default {
    setup() {
        const store = useStore();
        

        return {
            store,
        }
    }
}

</script>

<style scoped>

* {
    padding: 0;
    margin: 0;
}

.loading-inner {
    position: absolute;
    top:50%;
    left: 50%;
    width: Min(20vh, 20vw);
    height: Min(20vh, 20vw);
    background-color: #d0f4ff;
    border-radius: 50%;
    transform: translate(-50%, -50%);
    overflow: hidden;
    animation: loadingBreath 5s infinite;
}

.noFound::before {
    content: "匹配中...";
    z-index: 100;
    position: absolute;
    top:50%;
    left: 50%;
    transform: translate(-50%, -50%);
    color: #10a7b9;
    font-size: Min(2.4vh,2.4vw);
    font-family: sans-serif;
}

.found::before {
    content: "匹配成功";
    z-index: 100;
    position: absolute;
    top:50%;
    left: 50%;
    transform: translate(-50%, -50%);
    color: #10a7b9;
    font-size: Min(2.4vh,2.4vw);
    font-family: sans-serif;
}

.loading-inner::after {
    content: "";
    width: 100%;
    height: 25%;
    background-image: linear-gradient(to top, #12c8e0, #36e9ff, #5ffbf1);
    position: absolute;
    bottom: 0;
    animation:  loadingRun 5s ease-in-out infinite;
}

@keyframes loadingBreath {
    0% {
        box-shadow: 0 0 5px 0 #85f7fb;
    }
    25% {
        box-shadow: 0 0 20px 0 #85f7fb;
    }
    50% {
        box-shadow: 0 0 5px 0 #85f7fb;
    }
    75% {
        box-shadow: 0 0 20px 0 #85f7fb;
    }
    100% {
        box-shadow: 0 0 5px 0 #85f7fb;
    }
}

@keyframes loadingRun {
    0% {
        height: 25%;
    }
    100%{
        height: 100%;
    }
}

.loading-cover::before{
    content: "";
    position: absolute;
    width: 200%;
    height: 200%;
    background-color: #85f7fb;
    border-radius: 52% 25% 62% 69% / 25% 38%;
    left: -50%;
    z-index: 10;
    animation: loadingWave 5s linear infinite; 
}

.loading-cover::after{
    content: "";
    position: absolute;
    width: 200%;
    height: 200%;
    background-color: #d0f4ff;
    border-radius: 42% 38% 40% 62% / 48% 35%;
    left: -50%;
    z-index: 20;
    animation: loadingWave 5s linear infinite; 
}

@keyframes loadingWave {
    0% {
        top: -100%;
        transform: rotate(0);
    }
    100%{
        top:-200%;
        transform: rotate(360deg);
    }
}

</style>