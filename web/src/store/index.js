import {createStore} from 'vuex'
import ModuleUser from './user'
import ModulePk from './pk'
import ModuleMatchGameInfo from './matchGameInfo'

export default createStore({
    state: {},
    getters: {},
    mutations: {},
    actions: {},
    modules: {
        user: ModuleUser,
        pk: ModulePk,
        matchGameInfo:ModuleMatchGameInfo,
    }
})
