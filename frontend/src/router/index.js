import Vue from 'vue'
import Vuex from 'vuex'
import Router from 'vue-router'
import VueCookie from 'vue-cookie'

import constants from '../lib/constants'
// 서버
import ChannelDetail from '../page/post/channelDetail.vue'

// 유저
import Join from '../page/user/Join.vue'
import JoinDone from '../page/user/JoinDone.vue'
// import JoinDone from "../page/etc/loading.vue"
import MyPage from '../page/user/MyPage.vue'
import ConfirmDone from '../page/user/ConfirmationCheck.vue'
import PasswordFind from '../page/user/PasswordFind.vue'
import PasswordReset from '../page/user/PasswordReset.vue'
import PasswordEdit from '../page/user/PasswordEdit.vue'
import DeleteUser from '../page/user/ReallyDelete.vue'

// 포스트
import Enter from '../page/post/Enter.vue'
import Guide from '../page/post/Guide.vue'
import List from '../page/post/List.vue'
import createTeam from '../page/post/createTeam.vue'

// test
import Poll from '../components/common/Poll.vue'
import Kanban from '../components/module/Kanban.vue'
import Scheduler from '../components/module/Scheduler.vue'
import InviteModal from '../components/common/InviteModal.vue'

import bus from "../utils/bus.js"

Vue.use(Router)
Vue.use(Vuex)
Vue.use(VueCookie)

export default new Router({
    mode: 'history',
    routes: [{
            path: '/channel/:channelId/:channelName',
            name: 'channelDetail',
            component: ChannelDetail,
            props: route => ({ channelId: Number(route.params.ChannelId) }),
            beforeEnter: (to,from,next) => {
                bus.$emit('start:Loading'); 
                next();
            }
        },
        // 로그인/가입
        {
            path: '/user/signup',
            name: constants.URL_TYPE.USER.JOIN,
            component: Join,
            beforeEnter: (to,from,next) => {
                bus.$emit('start:Loading'); 
                next();
            }
        },
        {
            // path: '',
            path: '/user/joinDone',
            name: constants.URL_TYPE.USER.JOINDONE,
            component: JoinDone,
            beforeEnter: (to,from,next) => {
                bus.$emit('start:Loading'); 
                next();
            }
        },
        { // 유저 정보 - 가입 확인
            path: '/user/signup/done/registrationConfirmation',
            name: constants.URL_TYPE.USER.CONFIRMDONE,
            component: ConfirmDone,
            beforeEnter: (to,from,next) => {
                bus.$emit('start:Loading'); 
                next();
            }
        },
        { // 유저 - 비밀번호 찾기
            path: '/user/PasswordFind',
            name: constants.URL_TYPE.USER.PASSWORDFIND,
            component: PasswordFind,
            beforeEnter: (to,from,next) => {
                bus.$emit('start:Loading'); 
                next();
            }
        },
        { // 유저 - 비밀번호 리셋
            path: '/user/PasswordReset',
            name: constants.URL_TYPE.USER.PASSWORDRESET,
            component: PasswordReset,
            beforeEnter: (to,from,next) => {
                bus.$emit('start:Loading'); 
                next();
            }
        },
        { // 유저 - 계정 삭제
            path: '/user/DeleteAccount',
            name: constants.URL_TYPE.USER.DELETEUSER,
            component: DeleteUser,
            beforeEnter: (to,from,next) => {
                bus.$emit('start:Loading'); 
                next();
            }
        },
        { // 유저 - 비밀번호 변경
            path: '/user/PasswordEdit',
            name: constants.URL_TYPE.USER.PASSWORDEDIT,
            component: PasswordEdit,
            beforeEnter: (to,from,next) => {
                bus.$emit('start:Loading'); 
                next();
            }
        },
        { // 유저 정보 - 마이 페이지
            path: '/user/info',
            name: constants.URL_TYPE.USER.MYPAGE,
            component: MyPage,
            beforeEnter: (to,from,next) => {
                bus.$emit('start:Loading'); 
                next();
            }
        },
        // 포스트
        {
            path: '/',
            name: constants.URL_TYPE.POST.ENTER,
            component: Enter,
            beforeEnter: (to,from,next) => {
                bus.$emit('start:Loading'); 
                next();
            }
        },
        {
            path: '/guide',
            component: Guide,
            beforeEnter: (to,from,next) => {
                bus.$emit('start:Loading'); 
                next();
            }
        },
        {
            path: '/@hungrybird',
            component: createTeam,
            beforeEnter: (to,from,next) => {
                bus.$emit('start:Loading'); 
                next();
            }
        },
        {
            path: '/main',
            name: constants.URL_TYPE.POST.MAIN,
            component: List,
            beforeEnter: (to,from,next) => {
                bus.$emit('start:Loading'); 
                next();
            }
        },

        {
            path: '/error',
            name: constants.ERROR.FRONT_ERROR,
            component: () =>
                import ('../page/etc/error.vue'),
            beforeEnter: (to,from,next) => {
                bus.$emit('start:Loading'); 
                next();
            }
        },


        // 그 외 페이지 (404, ERROR)
        {
            path: '*',
            name: 'e404',
            component: () =>
                import ('../page/etc/e404.vue'),
            beforeEnter: (to,from,next) => {
                bus.$emit('start:Loading'); 
                next();
            }
        },

        //test
        {
            path: '/poll',
            name: 'poll',
            component: Poll,
        },
        {
            path: '/kanban',
            name: 'kanban',
            component: Kanban,
        },
        {
            path: '/scheduler',
            name: 'Scheduler',
            component: Scheduler,
        },
        {
            path: '/invite',
            name: 'InviteModal',
            component: InviteModal,
        },
    ]
})