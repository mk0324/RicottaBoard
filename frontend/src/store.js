import Vue from 'vue';
import Vuex from 'vuex';
import cookies from 'vue-cookie';
import constants from './lib/constants.js';
import router from './router/index.js';
// import authConnect from './auth-connector';
import * as Cookies from 'js-cookie';
import * as userApi from './api/user.js';
import * as authApi from './api/auth.js';
import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex);

export const store = new Vuex.Store({
    plugins: [createPersistedState({
        storage: {
            getItem: key => Cookies.get(key),
            setItem: (key, value) => Cookies.set(key, value, { expires: 3, secure: true }),
            removeItem: key => Cookies.remove(key)
        }
    })],
    state: {
        host: 'http://127.0.0.1:3000',
        token: '',
        role: '',
        userData: {
            email: '',
            name: '',
            password: '',
            nickname: '',
        },
        errorcode: '',
        accessData: '',
        accessToken: '',
        modal: false,
        // 캘린더
        scheduler: {
            left: '600px',
            top: '270px',
            event: {
                startDate: '',
                startTime: '',
                endDate: '',
                endTime: '',
                content: '',
                name: '',
            },
            events: [{ "name": "오프라인", "content": "hello", "start": "2020-10-21T12:30:00", "end": "2020-10-21T18:00:00" }],
            dialog: false,
            eventDetail: false,
        },
        joining: {
            canIUseIt: "",
            canNameUseIt: "",
        },

        finding: {
            status: "",
        },
        kanban: {
            left: '200px',
            top: '200px',
            kanbanName: 'kanban',
            task: {
                taskTitle: '',
                taskContents: '',
                taskAssigner: [],
                taskDates: [],
            },
            states: [{
                    columnTitle: 'TO DO',
                    tasks: [],
                },
                {
                    columnTitle: "IN PROGRESS",
                    tasks: [],
                },
                {
                    columnTitle: "DONE",
                    tasks: [],
                },
            ],
        },
        poll: [],
        videoList: [],
        editorList: [],
        inviteModal: false,
        withdrawalModal: false,
        updateOccur: false,
        memberList: [],
    },
    actions: {
        async REQUEST_ADD_EVENT(context, event) {
            try {
                //console.log(event);
                const addedEvent = makeEvent(event);
                context.commit('ADD_EVENT', addedEvent);
            } catch (e) {
                //console.log('일정 추가 에러' + e);
            }

            function makeEvent(event) {
                const start = `${event.startDate}T${event.startTime}:00`;
                const end = `${event.endDate}T${event.endTime}:00`;
                return {
                    name: event.title,
                    content: event.content,
                    start: start,
                    end: end,
                    // color: colors[Math.floor(Math.random() * 6)]
                }
            }
        },
        
        /** 
         * 로그아웃 메소드
         */
        [constants.METHODS.LOGOUT_USER]: (store) => {
            store.commit(constants.METHODS.LOGOUT_USER);
            state.commit("reSetAll");
            this.$router.go(0);
        },
        /**
         * 이메일 중복 체크 메소드
         */
        [constants.METHODS.EMAILCHECK]: (store, payload) => {
            const checkEmail = payload;
            if (checkEmail == "") {
                store.commit(constants.METHODS.EMAILCHECK, "nothing");
                return;
            }
            var exptext = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;
            if (exptext.test(checkEmail) == false) {
                //이메일 형식이 알파벳+숫자@알파벳+숫자.알파벳+숫자 형식이 아닐경우            
                store.commit(constants.METHODS.EMAILCHECK, "invaild");
                return;
            }

            userApi.emailCheck(checkEmail,
                    res => {
                        store.commit(constants.METHODS.EMAILCHECK, res.data.data);
                    },
                    err => {
                        store.dispatch("throwError", err);
                        store.commit(constants.METHODS.EMAILCHECK, 0);
                    })
                // authConnect.get(url)
                //     .then(res => {
                //         //console.log(res.data.data);
                //         store.commit(constants.METHODS.EMAILCHECK, res.data.data);
                //     })
                //     .catch(exp => {
                //         store.dispatch("throwError", exp);
                //         store.commit(constants.METHODS.EMAILCHECK, 0);
                //     })
        },
        /**
         * 닉네임 중복 체크 메소드
         */
        [constants.METHODS.NICKNAMECHECK]: (store, payload) => {
            const checkNickname = payload;
            //console.log('first', checkNickname)
            if (checkNickname == "") {
                //console.log('nothing')
                store.commit(constants.METHODS.NICKNAMECHECK, "nothing");
                return;
            }

            //console.log('second', checkNickname.length)
            if (checkNickname.length == 0) {
                store.commit(constants.METHODS.NICKNAMECHECK, "nothing");
                return;
            }

            userApi.nicknameCheck(checkNickname,
                res => {
                    //console.log('res', res.data.data)
                    store.commit(constants.METHODS.NICKNAMECHECK, res.data.data);
                },
                err => {
                    //console.log('err')
                    store.dispatch("throwError", err);
                    store.commit(constants.METHODS.NICKNAMECHECK, 0);
                })
        },
        /** 
         * 비밀번호 초기화 요청 메소드 
         */
        [constants.METHODS.RESETMYPASSWORDREQ]: (store, payload) => {
            const email = {
                "email": payload
            }
            userApi.resetMyPasswordReq(email,
                    res => {
                        if (res.data.success) {
                            store.commit(constants.METHODS.RESETMYPASSWORDREQ, "비밀번호 재설정 메일이 발송되었습니다.\n 3초뒤 되돌아갑니다.")
                            setTimeout(() => {
                                router.push('/');
                                store.commit(constants.METHODS.RESETMYPASSWORDREQ, "");
                            }, 3000)

                        }
                    },
                    err => {
                        store.dispatch("throwError", err);
                    })
                // authConnect.post(url, {
                //         "email": data,
                //     })
                //     .then(res => {
                //         //console.log(res);
                //         //console.log(res.data.success);
                //         if (res.data.success) {
                //             store.commit(constants.METHODS.RESETMYPASSWORDREQ, "비밀번호 재설정 메일이 발송되었습니다.\n 3초뒤 되돌아갑니다.")
                //             setTimeout(() => {
                //                 router.push('/');
                //                 store.commit(constants.METHODS.RESETMYPASSWORDREQ, "");
                //             }, 3000)

            //         }
            //     })
            //     .catch(exp => {
            //         store.dispatch("throwError", exp);
            //     })
        },

        /**
         * 비밀번호 초기화 메소드
         */
        [constants.METHODS.RESETMYPASSWORD]: (store, payload) => {
            const passwordInfo = {
                "password": payload.password,
                "confirmPassword": payload.passwordConfirm,
                "token": payload.token,
            }
            userApi.resetMyPassword(passwordInfo,
                    () => {
                        store.commit(constants.METHODS.RESETMYPASSWORDREQ, "비밀번호가 성공적으로 변경되었습니다!\n 3초뒤 되돌아갑니다.")
                        setTimeout(() => {
                            router.push('/');
                            store.commit(constants.METHODS.RESETMYPASSWORDREQ, "");
                        }, 3000)
                    },
                    err => {
                        store.dispatch("throwError", err);
                    })
                // authConnect.post(url, {
                //         "password": data.password,
                //         "confirmPassword": data.passwordConfirm,
                //         "token": data.token,
                //     })
                //     .then(res => {
                //         store.commit(constants.METHODS.RESETMYPASSWORDREQ, "비밀번호가 성공적으로 변경되었습니다!\n 3초뒤 되돌아갑니다.")
                //         setTimeout(() => {
                //             router.push('/');
                //             store.commit(constants.METHODS.RESETMYPASSWORDREQ, "");
                //         }, 3000)
                //     })
                //     .catch(exp => {
                //         store.dispatch("throwError", exp);
                //     })

        },

        /**
         * 에러페이지 이동 메소드
         */
        throwError: (store, exp) => {
            router.push('/error');
            store.commit(constants.METHODS.ERROR, exp)
            //console.log(exp);
        },

    },
    mutations: {
        OPEN_SCHEDULER_DIALOG(state, payload) {
            state.scheduler.event.startDate = payload.date;
            state.scheduler.event.endDate = payload.date;
            state.scheduler.event.startTime = payload.time;
            state.scheduler.dialog = true;
        },
        CLOSE_SCHEDULER_DIALOG(state) {
            //console.log("CLOSE_DIALOG");
            state.scheduler.dialog = false;
        },
        ADD_EVENT(state, getEvent) {
            //console.log("ADD_EVENT");
            state.scheduler.events.push(getEvent);
            state.scheduler.dialog = false;
            state.scheduler.event = {
                startDate: '',
                startTime: '',
                endDate: '',
                endTime: '',
                content: '',
                title: '',
            };
        },
        OPEN_SCHEDULER_EVENT(state, payload) {
            state.scheduler.event = {
                startDate: payload.eventParsed.start.date,
                startTime: payload.eventParsed.start.time,
                endDate: payload.eventParsed.end.date,
                endTime: payload.eventParsed.end.time,
                content: payload.event.content,
                name: payload.event.name,
            }
            state.scheduler.eventDetail = true;
        },
        CLOSE_SCHEDULER_EVENT(state) {
            state.scheduler.event = {
                startDate: '',
                startTime: '',
                endDate: '',
                endTime: '',
                content: '',
                title: '',
            };
            state.scheduler.eventDetail = false;

        },
        [constants.METHODS.LOGIN_USER]: (state, payload) => {
            // state.password = payload.password;
            //console.log("In Store, payload is : ", payload);
            state.userData.email = payload[0].email;
            state.accessData = {
                email: state.userData.email,
                name: state.userData.name,
                nickname: state.userData.nickname,
            };
            state.accessToken = payload[1];
            state.modal = !state.modal;
        },
        [constants.METHODS.LOGOUT_USER]: (state) => {
            state.userData.email = '';
            state.userData.password = '';
            state.userData.nickname = "";
            state.userData.name = "";
            state.accessToken = '';

            cookies.delete('AccessToken');
            cookies.delete('AccessData');
        },
        [constants.METHODS.GET_USER]: (state, payload) => {
            //console.log(payload.dataWhatINeed);
            state.userData.email = payload.dataWhatINeed.email;
            state.userData.password = payload.dataWhatINeed.password;
            state.userData.nickname = payload.dataWhatINeed.nickname;
            state.userData.name = payload.dataWhatINeed.username;
        },
        [constants.METHODS.USER_INFO]: (state, payload) => {
            state.userData.email = payload.newUser.email
            state.userData.nickname = payload.newUser.nickname
            state.userData.name = payload.newUser.username
            //console.log(state.userData);
        },
        [constants.METHODS.DELETE_USER]: (state) => {
            state.userData.email = "";
            state.userData.password = "";
            state.userData.nickname = "";
            state.userData.name = "";
        },
        [constants.METHODS.ERROR]: (state, exp) => {
            state.errorcode = exp;
        },
        [constants.METHODS.EMAILCHECK]: (state, result) => {
            // //console.log("In store, result is : ", result);
            switch (result) {
                case "true":
                    state.joining.canIUseIt = "이미 사용 중인 이메일입니다.";
                    break;
                case "false":
                    state.joining.canIUseIt = "이 이메일은 사용가능합니다.";
                    break;
                case "invaild":
                    state.joining.canIUseIt = "이메일형식이 올바르지 않습니다.";
                    break;
                default:
                    state.joining.canIUseIt = "";
                    break;
            }
        },
        [constants.METHODS.NICKNAMECHECK]: (state, result) => {
            switch (result) {
                case "true":
                    state.joining.canNameUseIt = "이미 사용중인 닉네임입니다.";
                    break;
                case "false":
                    state.joining.canNameUseIt = "이 닉네임은 사용가능합니다.";
                    break;
                case "nothing":
                    state.joining.canNameUseIt = "";
                    break;
                default:
                    state.joining.canNamedUseIt = "";
                    break;
            }
        },
        [constants.METHODS.RESETMYPASSWORDREQ]: (state, result) => {
            state.finding.status = result;
        },
        reSetAll: (state) => {
            state.token = '';
            state.role = '';
            state.userData.email = "";
            state.userData.name = "";
            state.userData.password = "";
            state.userData.nickname = "";

            state.errorcode = '';
            state.accessToken = '';
            state.modal = false;
            state.joining.canIUseIt = "";
            state.joining.canNameUseIt = "";
            state.finding.status = "";

        },
        setDataAgain: (state, payload) => {
            state.userData.email = payload.AccessData.email;
            state.userData.name = payload.AccessData.name;
            state.userData.nickname = payload.AccessData.nickname;

            state.accessData = payload.AccessData;
            state.accessToken = payload.AccessToken;
        },
        toggleModal: (state) => {
            state.modal = !state.modal;
        },
        toggleUpdate: (state) => {
            //console.log('update Occured')
            state.updateOccur = !state.updateOccur;
        },
    },
    getters: {
        userData: function(state) {
            return state.userData;
        },
        userDataStr: function(state) {
            const dataStr = `email:${state.userData.email},name:${state.userData.name},nickname:${state.userData.nickname}`
                //   //console.log("In store, userDataStr is : ",
                //   dataStr);
            return dataStr;
        },
        accessToken: function(state) {
            return state.accessToken;
        },
        canIUseIt: function(state) {
            return state.joining.canIUseIt;
        },
        canNameUseIt: function(state) {
            return state.joining.canNameUseIt;
        },
        status: function(state) {
            return state.finding.status;
        },
        modal: function(state) {
            return state.modal;
        },
    },
});