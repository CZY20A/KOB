import {createRouter, createWebHistory} from 'vue-router'
import PkIndexView from '../views/pk/PkIndexView'
import RecordIndexView from '../views/record/RecordIndexView'
import RecordContentView from '../views/record/RecordContentView'
import UserBotIndexView from '../views/user/bot/UserBotIndexView'
import RanklistIndexView from '../views/ranklist/RanklistIndexView';
import NotFoud from '../views/error/NotFound';
import UserAccountLoginView from '../views/user/account/UserAccountLoginView';
import UserAccountRegisterView from '../views/user/account/UserAccountRegisterView';
import store from '../store/index';
import PkSnakeView from '../views/pk/PkSnakeView';
import PkTankView from '../views/pk/PkTankView';

const routes = [

    {
        path: '/pk/',
        name: 'pk_index',
        component: PkIndexView,
        meta: {
            requestAuth: true,
        }
    },
    {
        path: '/',
        name: "home",
        redirect: '/pk/',
        meta: {
            requestAuth: true,
        }
    },
    {
        path: '/pk/snake/',
        name: "snake",
        component: PkSnakeView,
        meta: {
            requestAuth: true,
        }
    },
    {
        path:'/pk/tank/',
        name:'tank',
        component: PkTankView,
        meta:{
            requestAuth:true,
        }
    },
    {
        path: '/record/',
        name: 'record_index',
        component: RecordIndexView,
        meta: {
            requestAuth: true,
        }
    },
    {
        path: '/record/:recordId/',
        name: 'record_content',
        component: RecordContentView,
        meta: {
            requestAuth: true,
        }
    }
    ,
    {
        path: '/ranklist/',
        name: 'ranklist_index',
        component: RanklistIndexView,
        meta: {
            requestAuth: true,
        }
    },
    {
        path: '/user/bot/',
        name: 'user_bot_index',
        component: UserBotIndexView,
        meta: {
            requestAuth: true,
        }
    },
    {
        path: '/user/account/login/',
        name: 'user_account_login',
        component: UserAccountLoginView,
    },
    {
        path: '/user/account/register',
        name: 'user_account_register',
        component: UserAccountRegisterView,
    },
    {
        path: '/404/',
        name: '404',
        component: NotFoud,
    },
    {
        path: '/:catchAll(.*)',
        redirect: '/404/'
    },
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    if (to.meta.requestAuth && !store.state.user.is_login) {
        next({name: "user_account_login"});
    } else {
        next();
    }
})

export default router
