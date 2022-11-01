import {createStore} from 'vuex'
import ModuleUser from './user'
import ModulePk from './pk'
import ModuleMatchGameInfo from './matchGameInfo'
import ModuleRecord from './record'

export default createStore({
    state: {},
    getters: {},
    mutations: {},
    actions: {},
    modules: {
        user: ModuleUser,
        pk: ModulePk,
        matchGameInfo:ModuleMatchGameInfo,
        record:ModuleRecord,
    }
})
