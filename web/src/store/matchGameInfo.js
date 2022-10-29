
export default {
    state: {
        id: "",
    },
    getters: {},
    mutations: {
        updateMatchGameInfo(state, matchGameInfo) {
            state.id = matchGameInfo.id;
        }
    },
    actions: {
        
    },
    modules: {}
}