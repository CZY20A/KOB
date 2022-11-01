<template>
        
    <div class="box">
    
        <div class="mcontainer">
            <div class="mcard" v-for="(user,idx) in users" :key="user.id">
                <div class="content">
                    <div class="img">
                        <img :src="user.photo" alt="">
                    </div>
                    <div class="detail">
                        <span>{{user.username}}</span>
                        <p style="color: black;font-weight:500;">天梯分:{{user.rating}}</p>
                    </div>
                </div>
                <a>#{{getRank(idx)}}</a>
            </div>
        </div>


        <div class="pages">
        <ul>
            <li><a class="prev" @click="click_page(-2)">
                &laquo;
            </a></li>

            <li v-for="page in pages" :key="page" :class=" 'pageNumber ' + page.is_active "><a  @click="pull_page(page.number)">{{page.number}}</a></li>

            <li><a   class="next" @click="click_page(-1)">
                &raquo;
            </a></li>

        </ul>
        </div>


    </div>

</template>

<script>
import { useStore } from "vuex";
import {ref} from 'vue';
import $ from 'jquery';
import router from "@/router/index";

export default{
    components:{
        
    },
    
    setup() {
        const store = useStore();
        let currentPage = 1;
        let total_users = 0;
        let users = ref([]);
        let pages = ref([]);

        const click_page = page => {
            if(page === -2) page = currentPage - 1;
            else if(page === -1) page = currentPage + 1;
            let max_pages = parseInt(Math.ceil(total_users / 10));

            if(page >= 1 && page <= max_pages) {
                pull_page(page);
            }
        }

        const update_pages = () => {
            let max_pages = parseInt(Math.ceil(total_users / 10));
            let new_pages = [];
            if(currentPage - 2 <= 0) {
                for(let i = 1; i <= 5 && i <= max_pages; ++i)
                    new_pages.push({
                        number:i,
                        is_active: i === currentPage ? "active":""
                    });
            } else {
                for(let i = currentPage - 2; i <= currentPage + 2; i++) {
                    if(i >= 1 && i <= max_pages) {
                        new_pages.push({
                        number:i,
                        is_active: i === currentPage ? "active":""
                        });
                    }
                }
            }
            pages.value = new_pages;
        }

        const pull_page = page => {
            $.ajax({
                type:"GET",
                url:"http://172.18.90.64:3000/ranklist/getlist/",
                headers:{
                        Authorization:"Bearer " + store.state.user.token,
                },
                data:{
                    page:page,
                },
                success(resp){
                    currentPage = page;
                    total_users = resp.users_count;
                    users.value = resp.users;
                    update_pages();
                },
                error() {
                    alert("账号已在别的地方登录或登录过期，请重新登录");
                    store.commit("logout");
                    router.push({name:"user_account_login"});
                }
            })
        }
        pull_page(currentPage);

        const getRank = idx => {
            return idx + 1 + 5 * (currentPage - 1);
        }


        return {
            pull_page,
            users,
            total_users,
            pages,
            click_page,
            currentPage,
            getRank,
        }
    }
}
</script>

<style scoped>
    .pages{
        margin-top: 10px;
        display: flex;
        justify-content: center;
        align-items: center;
        box-sizing: border-box;
    }

    ul{
        display: flex;
        padding: 10px 20px;
        border-radius: 50px;
        box-shadow: 0 5px 15px rgba(0,0,0,.2);
        background: #fff;
    }

    ul li{
        list-style: none;
        line-height: 40px;
        margin:0 5px;
        cursor: pointer;
        user-select: none;
    }

    ul li.pageNumber{
        width: 50px;
        height: 40px;
        line-height: 40px;
        text-align: center;
    }


    ul li a {
        display: block;
        text-decoration: none;
        color: #777;
        font-weight: 600;
        border-radius: 50%;
    }

    ul li.pageNumber a:hover, ul li.pageNumber.active a{
        background-color: #333;
        color: #fff;
    }

    ul li:first-child {
        margin-right: 30px;
        font-weight: 700;
        font-size: 20px;
    }

    ul li:last-child {
        margin-left: 30px;
        font-weight: 700;
        font-size: 20px;
    }


   .box{
    box-shadow: 0 25px 45px rgba(0, 0, 0, 0.3);
    border: 1px solid rgba(255, 255, 255, 0.5);
    border-radius: 10px;
    margin: 20px auto;
    padding: 20px;
    width: 70%;
    background: rgba(33,37,41, 0.3);
   }

.mcontainer{
    margin:0 auto;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
}
.mcard{
    /* 绝对定位 */
    margin-top: 10px;
    background-color: #fff;
    width: 430px;
    height: 80px;
    /* 弹性布局 */
    display: flex;
    /* 将元素靠边对齐 */
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;
    border-radius: 100px 20px 20px 100px;
}
.mcard .content{
    display: flex;
    align-items: center;
}
.mcard .img{
    width: 90px;
    height: 80px;
    position: relative;
    right: 17px;
    background-color: #fff;
    padding: 5px;
    border-radius: 50%;
    /* 阴影 */
    box-shadow: 0 0 5px rgba(0,0,0,0.2);
}
.mcard .img img{
    width: 100%;
    height: 100%;
    /* 对图片进行剪切,保留原始比例 */
    object-fit: cover;
    border-radius: 50%;
}
.mcard .detail{
    margin-left: 0px;
    font-style: italic;
}
.mcard .detail span{
    display: block;
    font-size: 23px;
    font-weight: 700;
    margin-bottom: 8px;
}
.mcard a{
    font-size: 20px;
    font-style: italic;
    text-decoration: none;
    background: rgb(161,185,221);
    padding: 7px 18px;
    color: #fff;
    border-radius: 25px;
}
   


</style>