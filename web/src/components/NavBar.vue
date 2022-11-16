<template>
    <nav class="navbar navbar-expand-lg navbar-dark" style="background-color:rgba(0,0,0,0.3);">
        <div class="container">
            <a class="navbar-brand" href="https://github.com/CZY20A/KOB" target="_blank">
                <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-github" viewBox="0 0 16 16">
                <path d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.012 8.012 0 0 0 16 8c0-4.42-3.58-8-8-8z"/>
                </svg>
                <i class="bi bi-github" style="margin-left:5px">CZY</i> 
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarText">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0" style="margin:0 auto;">
                <li class="nav-item">
                <router-link :class="route_name == 'pk_index' ? 'nav-link active':'nav-link' " aria-current="page" :to="{name:'pk_index'}">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-steam" viewBox="0 0 16 16">
                    <path d="M.329 10.333A8.01 8.01 0 0 0 7.99 16C12.414 16 16 12.418 16 8s-3.586-8-8.009-8A8.006 8.006 0 0 0 0 7.468l.003.006 4.304 1.769A2.198 2.198 0 0 1 5.62 8.88l1.96-2.844-.001-.04a3.046 3.046 0 0 1 3.042-3.043 3.046 3.046 0 0 1 3.042 3.043 3.047 3.047 0 0 1-3.111 3.044l-2.804 2a2.223 2.223 0 0 1-3.075 2.11 2.217 2.217 0 0 1-1.312-1.568L.33 10.333Z"/>
                    <path d="M4.868 12.683a1.715 1.715 0 0 0 1.318-3.165 1.705 1.705 0 0 0-1.263-.02l1.023.424a1.261 1.261 0 1 1-.97 2.33l-.99-.41a1.7 1.7 0 0 0 .882.84Zm3.726-6.687a2.03 2.03 0 0 0 2.027 2.029 2.03 2.03 0 0 0 2.027-2.029 2.03 2.03 0 0 0-2.027-2.027 2.03 2.03 0 0 0-2.027 2.027Zm2.03-1.527a1.524 1.524 0 1 1-.002 3.048 1.524 1.524 0 0 1 .002-3.048Z"/>
                    </svg>
                    <i class="bi bi-steam" style="margin-left:3px">对战</i>
                </router-link>
                </li>
                <li class="nav-item">
                <router-link :class="route_name == 'record_index' ? 'nav-link active':'nav-link' " :to="{name:'record_index'}" @click="click_to_record">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pen-fill" viewBox="0 0 16 16">
                    <path d="m13.498.795.149-.149a1.207 1.207 0 1 1 1.707 1.708l-.149.148a1.5 1.5 0 0 1-.059 2.059L4.854 14.854a.5.5 0 0 1-.233.131l-4 1a.5.5 0 0 1-.606-.606l1-4a.5.5 0 0 1 .131-.232l9.642-9.642a.5.5 0 0 0-.642.056L6.854 4.854a.5.5 0 1 1-.708-.708L9.44.854A1.5 1.5 0 0 1 11.5.796a1.5 1.5 0 0 1 1.998-.001z"/>
                    </svg>
                    <i class="bi bi-pen-fill" style="margin-left:3px">对局列表</i>
                    
                </router-link>
                </li>
                <li class="nav-item">
                <router-link :class="route_name == 'ranklist_index' ? 'nav-link active':'nav-link' " :to="{name:'ranklist_index'}">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-file-person-fill" viewBox="0 0 16 16">
                        <path d="M12 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2zm-1 7a3 3 0 1 1-6 0 3 3 0 0 1 6 0zm-3 4c2.623 0 4.146.826 5 1.755V14a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1v-1.245C3.854 11.825 5.377 11 8 11z"/>
                    </svg>  
                    <i class="bi bi-file-person-fill" style="margin-left:3px">排行榜</i>
                </router-link>
                </li>
            </ul>
            
            <ul class="navbar-nav" v-if="$store.state.user.is_login">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <img :src="$store.state.user.photo" class="record-user-photo">
                        {{$store.state.user.username}}
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                        <li><router-link class="dropdown-item" :to="{name:'user_bot_index'}">
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-person-fill-gear" viewBox="0 0 16 16">
                            <path d="M11 5a3 3 0 1 1-6 0 3 3 0 0 1 6 0Zm-9 8c0 1 1 1 1 1h5.256A4.493 4.493 0 0 1 8 12.5a4.49 4.49 0 0 1 1.544-3.393C9.077 9.038 8.564 9 8 9c-5 0-6 3-6 4Z"/>
                            <path fill-rule="evenodd" d="M11.886 9.46c.18-.613 1.048-.613 1.229 0l.043.148a.64.64 0 0 0 .921.382l.136-.074c.561-.306 1.175.308.87.869l-.075.136a.64.64 0 0 0 .382.92l.149.045c.612.18.612 1.048 0 1.229l-.15.043a.64.64 0 0 0-.38.921l.074.136c.305.561-.309 1.175-.87.87l-.136-.075a.64.64 0 0 0-.92.382l-.045.149c-.18.612-1.048.612-1.229 0l-.043-.15a.64.64 0 0 0-.921-.38l-.136.074c-.561.305-1.175-.309-.87-.87l.075-.136a.64.64 0 0 0-.382-.92l-.148-.045c-.613-.18-.613-1.048 0-1.229l.148-.043a.64.64 0 0 0 .382-.921l-.074-.136c-.306-.561.308-1.175.869-.87l.136.075a.64.64 0 0 0 .92-.382l.045-.148ZM14 12.5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0Z"/>
                            </svg>
                            <i class="bi bi-person-fill-gear icon-des">bot/修改信息</i>
                            
                        </router-link></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" @click="logout">
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-power" viewBox="0 0 16 16">
                            <path d="M7.5 1v7h1V1h-1z"/>
                            <path d="M3 8.812a4.999 4.999 0 0 1 2.578-4.375l-.485-.874A6 6 0 1 0 11 3.616l-.501.865A5 5 0 1 1 3 8.812z"/>
                            </svg>
                            <i class="bi bi-power icon-des">退出</i>
                        </a></li>
                    </ul>
                </li>
            </ul>

            <ul class="navbar-nav" v-else-if="!$store.state.user.pulling_info">
                <li class="nav-item">
                    <router-link :class="route_name === 'user_account_login' ? 'nav-link active':'nav-link'" :to="{name:'user_account_login'}">登录</router-link>
                </li>
                <li class="nav-item">
                    <router-link :class="route_name === 'user_account_register' ? 'nav-link active':'nav-link'" :to="{name:'user_account_register'}">注册</router-link>
                </li>
            </ul>


            </div>
        </div>

    </nav>

</template>


<script>
import router from '@/router';
import { computed } from '@vue/reactivity';
import { useRoute } from 'vue-router';
import { useStore } from 'vuex';


export default {
    name:"NavBar",
    components:{
        
    },
    setup(){
        const store = useStore();
        const route = useRoute();
        let route_name = computed(() => route.name);

        const logout = () => {
            store.commit("logout");
            router.push({name:"user_account_login"})
        }

        const click_to_record = () => {
            store.commit('updateSearchUsername', '');
        }

        return {
            route_name,
            logout,
            click_to_record,
        }
    }
}

</script>

<style scoped>

.icon-des {
    margin-left:3px
}
img.record-user-photo {
    height: 3vh;
    width: 2vw;
    border-radius: 50%;
   }

</style>