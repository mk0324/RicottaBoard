import axios from "axios";
import * as boardApi from './api/board.js'

export default axios.create({
  // baseURL: "http://k3a204.p.ssafy.io/api",
  baseURL: boardApi.API_BASE_URL,
  // baseURL: "http://218.146.39.122:8080",
  // headers: {
  //   "Content-type": "application/json",
  // }
});