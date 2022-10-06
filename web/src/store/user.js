import $ from 'jquery'

export default{
    state: {
            id:"",
            username:"",
            photo:"",
            token:"",
            is_login:false,
    },
    getters: {
    },
    mutations: {
        updateUser(state, user){
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;
            state.is_login = user.is_login;
        },
        updateToken(state, token){
            state.token = token;
        },
        logout(state){
            state.id = '',
            state.username = '';
            state.photo = '';
            state.token = '';
            state.is_login = false;
        }
    },
    actions: {
        login(context, data){
            $.ajax({
                type:"POST",
                url:"http://localhost:3000/user/account/token/",
                data:{
                    username:data.username,
                    password:data.password,
                },
                success(resp){
                    if(resp.message === 'success'){
                        context.commit('updateToken', resp.token);
                        data.success();
                    }else{
                        data.error();
                    }
                },
                error(){
                    data.error();
                }
            })
        },
        getInfo(context, data){
            $.ajax({
                type:"GET",
                url:"http://localhost:3000/user/account/info/",
                headers:{
                    Authorization:"Bearer " + context.state.token,
                },
                success(resp){
                    if(resp.message === 'success'){
                        context.commit("updateUser", {
                            ...resp,
                            is_login:true,
                        })
                        data.success();
                    }
                },
                error(){
                    data.error();
                }
            })
        },
    },
    modules: {
    }
}