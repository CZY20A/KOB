

export default {
    state: {
        is_record: false,
        search_username:"",
        a_steps: "",
        b_steps: "",
        record_loser: "",
        record_result: "none",
        record_a_username:"",
        record_a_photo:"",
        record_b_username:"",
        record_b_photo:"",
        record_back_page:1,
        record_is_back:false,
    },
    getters: {},
    mutations: {
        updateIsRecord(state, is_record) {
            state.is_record = is_record;
        },
        updateSteps(state, data) {
            state.a_steps = data.a_steps;
            state.b_steps = data.b_steps;
        },
        updateRecordLoser(state, data){
            state.record_loser = data;
        },
        updateRecordResult(state, data){
            state.record_result = data;
        },
        updateRecordUserInfo(state, data) {
            state.record_a_username = data.a_username;
            state.record_a_photo = data.a_photo;
            state.record_b_username = data.b_username;
            state.record_b_photo = data.b_photo;
        },
        updateSearchUsername(state, data) {
            state.search_username = data;
        },
        updateReocrdBackPage(state, data) {
            state.record_back_page = data;
        },
        updateRecordIsBack(state, data) {
            state.record_is_back = data;
        }
    },
    actions: {},
    modules: {}
}