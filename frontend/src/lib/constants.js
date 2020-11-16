export default {
    TITLE: 'Ricotta Board',
    LS_KEY: {
        USER_TOKEN: 'userToken',
    },
    ERROR: { 
        FRONT_ERROR: 'FE00' 
    },
    URL_TYPE: {
        USER: {
            LOGIN: 'login',
            JOIN: 'join', 
            JOINDONE: 'joinDone',
            CONFIRMDONE: 'confirmDone',
            MYPAGE: 'myPage',
            PASSWORDFIND: 'passwordFind',
            PASSWORDRESET: 'passwordReset',
            PASSWORDEDIT: 'passwordEdit',
            DELETEUSER: 'deleteUser',
        },
        POST: {
            ENTER : "enter",
            MAIN : "main",
        },
    },
    METHODS:{
        // 유저 관련 메소드
        DELETE_USER: 'deleteUser',
        UPDATE_USER: 'updateUser',
        LOGIN_USER:  'loginUser',
        LOGOUT_USER: 'logoutUser',
        CREATE_USER: 'createUser',
        GET_USER: 'getUser',
        USER_INFO:'userInfo',
        ERROR:'error',

        // 이메일 중복체크 메소드
        EMAILCHECK: 'emailCheck',
        // 닉네임 중복체크 메소드
        NICKNAMECHECK: 'nicknameCheck',
        
        // 비밀번호 재설정 메소드
        RESETMYPASSWORD: 'resetMyPassword',
        // 비밀번호 재설정 요청 메소드
        RESETMYPASSWORDREQ: 'resetMyPasswordReq', 
    }
}

