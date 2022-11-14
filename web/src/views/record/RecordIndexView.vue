<template>
        
    <div class="box">
    <div class="row">
        <div class="col-8">
            <div class="input-group w-50" >
              <input type="text"  class="form-control" placeholder="用户名查询特定对局(空白返回所有)" aria-label="Input group example" aria-describedby="basic-addon1" v-model="search_username">
              <span class="input-group-text search" id="basic-addon1" @click="search_by_username(1)" >
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-zoom-in" viewBox="0 0 16 16">
                <path fill-rule="evenodd" d="M6.5 12a5.5 5.5 0 1 0 0-11 5.5 5.5 0 0 0 0 11zM13 6.5a6.5 6.5 0 1 1-13 0 6.5 6.5 0 0 1 13 0z"></path>
                <path d="M10.344 11.742c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1 6.538 6.538 0 0 1-1.398 1.4z"></path>
                <path fill-rule="evenodd" d="M6.5 3a.5.5 0 0 1 .5.5V6h2.5a.5.5 0 0 1 0 1H7v2.5a.5.5 0 0 1-1 0V7H3.5a.5.5 0 0 1 0-1H6V3.5a.5.5 0 0 1 .5-.5z"></path>
                </svg>
              </span>
            </div>
        </div>
    </div>
    <br>
    <table class="table table-dark table-striped">
        <thead style="text-align:center;">
            <tr>
            <th >A</th>
            <th>B</th>
            <th >对战结果</th>
            <th>对战时间</th>
            <th>操作</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="record in records" :key="record.record.id">
                <td>
                    <img :src="record.a_photo" class="record-user-photo">
                    &nbsp;
                    <span>{{record.a_username}}</span>
                </td>
                <td>
                    <img :src="record.b_photo" class="record-user-photo">
                    &nbsp;
                    <span>{{record.b_username}}</span>
                </td>
                <td class="can-center">{{record.result}}</td>
                <td class="can-center">{{record.record.createtime}}</td>
                <td class="can-center"> <button type="button" class="btn btn-light" @click="open_record_content(record.record.id)">查看录像</button></td>
            </tr>
        </tbody>
    </table>
    <div class="pages">
        <ul>
            <li><a class="prev" @click="click_page(-2)">
                &laquo;
            </a></li>

            <li v-for="page in pages" :key="page" :class=" 'pageNumber ' + page.is_active "><a  @click="search_by_username(page.number)">{{page.number}}</a></li>

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
        let total_records = 0;
        let records = ref([]);
        let search_username = ref("");
        let pages = ref([]);


        if(store.state.record.record_is_back) {
            currentPage = store.state.record.record_back_page;
            store.commit('updateRecordIsBack', false);
        }


        search_username.value = store.state.record.search_username;

        const click_page = page => {
            if(page === -2) page = currentPage - 1;
            else if(page === -1) page = currentPage + 1;
            let max_pages = parseInt(Math.ceil(total_records / 10));

            if(page >= 1 && page <= max_pages) {
                search_by_username(page);
            }
        }

        const update_pages = () => {
            let max_pages = parseInt(Math.ceil(total_records / 10));
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
                url:"http://172.18.90.64:3000/api/record/getlist/",
                headers:{
                        Authorization:"Bearer " + store.state.user.token,
                },
                data:{
                    page:page,
                },
                success(resp){
                    currentPage = page;
                    total_records = resp.records_count;
                    records.value = resp.records;
                    update_pages();
                },
                error() {
                    alert("账号已在别的地方登录或登录过期，请重新登录");
                    store.commit("logout");
                    router.push({name:"user_account_login"});
                }
            })
        }
        

        const stringTo2D = (map) => {
            let g = [];
            for(let i = 0, k = 0; i < 13; ++i) {
                g[i] = [];
                for(let j = 0; j < 14; ++j) {
                    if(map[k] === '0') g[i][j] = false;
                    else g[i][j] = true;
                    k ++;
                }
            }
            return g;
        }

        const open_record_content = recordId => {
            store.commit('updateReocrdBackPage', currentPage);
            for(const record of records.value) {
                if(record.record.id === recordId) {
                    console.log(record);
                    store.commit("updateIsRecord", true);
                    store.commit('updateGame', {
                        map:stringTo2D(record.record.map),
                        a_id: record.record.aid,
                        a_sx: record.record.asx,
                        a_sy: record.record.asy,
                        b_id: record.record.bid,
                        b_sx: record.record.bsx,
                        b_sy: record.record.bsy,
                   })
                   store.commit('updateSteps', {
                        a_steps: record.record.asteps,
                        b_steps: record.record.bsteps,
                   });
                   store.commit('updateRecordLoser', record.record.loser);
                   store.commit('updateRecordUserInfo', {
                    a_username: record.a_username,
                    a_photo: record.a_photo,
                    b_username: record.b_username,
                    b_photo: record.b_photo
                   })
                    router.push({
                        name:'record_content',
                        params: {
                            recordId
                        }
                    });
                    break;
                }
            }
        }

        const search_by_username = (page) => {
            store.commit("updateSearchUsername", search_username.value);
            if(search_username.value === '') {
                pull_page(page);
            }
            else{
                $.ajax({
                    type:"GET",
                    url:"http://172.18.90.64:3000/api/record/getlistByUsername/",
                    headers:{
                            Authorization:"Bearer " + store.state.user.token,
                    },
                    data:{
                        page:page,
                        username:search_username.value,
                    },
                    success(resp){
                        currentPage = page;
                        if(resp.message === 'success') {
                            total_records = resp.records_count;
                            records.value = resp.records;
                        } else {
                            total_records = 0;
                            records.value = [];
                        }
                        update_pages();
                    },
                    error() {
                    alert("账号已在别的地方登录或登录过期，请重新登录");
                    store.commit("logout");
                    router.push({name:"user_account_login"});
                    }
                    
                })
            }
        }

        search_by_username(currentPage);

        return {
            pull_page,
            records,
            total_records,
            open_record_content,
            search_username,
            search_by_username,
            pages,
            click_page
        }
    }
}
</script>

<style scoped>
    .pages{
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

    .search {
        cursor: pointer;
    }

    .search:hover {
        background: rgba(255, 255, 255, 0.5);
    }

   img.record-user-photo {
    width: 3vw;
    border-radius: 50%;
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

   .can-center {
    text-align: center;
    line-height: 3vw;
   }


</style>