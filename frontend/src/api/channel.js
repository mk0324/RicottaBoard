import { BOARD_BASE_URL, createInstance } from "./index.js";
// 보드 서버
const API_BASE_URL = BOARD_BASE_URL;

const instance = createInstance(API_BASE_URL);


function findAllChannel(data, config, success, fail) {
    instance
        .post('/channels', data, config)
        .then(success)
        .catch(fail);
}

function createChannel(data, config, success, fail) {
    instance
        .post('/channel', data, config)
        .then(success)
        .catch(fail);
}

function validateUserWithChannel(data, success, fail) {
    instance
        .post('/channel/validation', data)
        .then(success)
        .catch(fail);
}

export { findAllChannel, createChannel, validateUserWithChannel };