<template>
  <NavBar></NavBar>
  <router-view/>
</template>

<script>
  import 'bootstrap/dist/css/bootstrap.min.css';
  import 'bootstrap/dist/js/bootstrap.js';
  import NavBar from './components/NavBar.vue';

  export default {
    name:"App",
    components:{
    NavBar,
},
    setup(){
    window.alert = alert;
    function alert(data, callback) { //回调函数
        let alert_bg = document.createElement('div');
        let alert_box = document.createElement('div');
        let alert_text = document.createElement('div');
        let alert_btn = document.createElement('div');
        let textNode = document.createTextNode(data ? data : '');
        let btnText = document.createTextNode('确 定');

        // 控制样式
        css(alert_bg, {//背景颜色设置
            'position': 'fixed',
            'top': '0',
            'left': '0',
            'right': '0',
            'bottom': '0',
            'background-color': 'rgba(0, 0, 0, 0.1)',
            'z-index': '999999999'
        });

        css(alert_box, {//控制盒子大小、背景颜色上下边距
            'width': '270px',
            'max-width': '90%',
            'font-size': '16px',
            'text-align': 'center',
            'background-color': '#fff',
            'border-radius': '15px',
            'position': 'absolute',
            'top': '50%',
            'left': '50%',
            'transform': 'translate(-50%, -50%)'
        });

        css(alert_text, {
            'padding': '10px 15px',
            'border-bottom': '1px solid #ddd'
        });

        css(alert_btn, {
            'padding': '10px 0',
            'color': '#007aff',
            'font-weight': '600',
            'cursor': 'pointer'
        });

        // 内部结构套入
        alert_text.appendChild(textNode);
        alert_btn.appendChild(btnText);
        alert_box.appendChild(alert_text);
        alert_box.appendChild(alert_btn);
        alert_bg.appendChild(alert_box);

        // 总体显示到页面内
        document.getElementsByTagName('body')[0].appendChild(alert_bg);

        // 肯定绑定点击事件删除标签
        alert_btn.onclick = function() {
            alert_bg.parentNode.removeChild(alert_bg);
            if (typeof callback === 'function') {
                callback(); //回调
            }
        }
    }

    function css(targetObj, cssObj) {
        var str = targetObj.getAttribute("style") ? targetObj.getAttribute('style') : '';
        for (var i in cssObj) {
            str += i + ':' + cssObj[i] + ';';
        }
        targetObj.style.cssText = str;
    }

    
    },
  }
</script>


<style>


body{
  background-image: url("@/assets/images/background.jpg");
  background-size: cover;
}
</style>
