import axios from "axios";

export default axios.create({
   //baseURL: "http://localhost/auth",
  // 나중에 nginx 연결해줄때 auth 로 바꿀것
  // 인증서버니까 아예 안주는것도 괜찮을듯.
  baseURL: "http://localhost:9004",
  // baseURL: "http://k3a204.p.ssafy.io:9004",
  headers: {
    "Content-type": "application/json",
  }
}); 