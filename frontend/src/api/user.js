import { AUTH_BASE_URL, createInstance } from "./index.js";
// auth 서버
const API_BASE_URL = AUTH_BASE_URL;

const instance = createInstance(API_BASE_URL);

function deleteUser(user, accessToken, success, fail) {
    instance
        .delete(`/user/delete?email=${user.email.trim()}&password=${user.password.trim()}`, {
            headers: {
                Authorization: 'Bearer ' + accessToken
            }
        })
        .then(success)
        .catch(fail);
}

function createUser(user, success, fail) {
    instance
        .post('/register', user)
        .then(success)
        .catch(fail);
}

function emailCheck(email, success, fail) {
    instance
        .get(`/checkEmailInUse?email=${email}`)
        .then(success)
        .catch(fail);
}

function nicknameCheck(nickName, success, fail) {
    instance
        .get(`/checkNicknameInUse?Nickname=${nickName}`)
        .then(success)
        .catch(fail);
}

function resetMyPasswordReq(email, success, fail) {
    instance
        .post('/password/resetlink', email)
        .then(success)
        .catch(fail);
}

function resetMyPassword(password, success, fail) {
    instance
        .post('/password/reset', password)
        .then(success)
        .catch(fail);
}

function userInfo(newUser, config, success, fail) {
    instance
        .post('/user/userInfo', newUser, config)
        .then(success)
        .catch(fail);
}

function updatePassword(updatePasswordRequest, config, success, fail) {
    instance
        .post('/user/password/update', updatePasswordRequest, config)
        .then(success)
        .catch(fail);
}
export { deleteUser, createUser, emailCheck, nicknameCheck, resetMyPasswordReq, resetMyPassword, userInfo, updatePassword };