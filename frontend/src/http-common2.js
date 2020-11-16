import axios from "axios";

// axios for chatting log

export default axios.create({
    baseURL: "http://localhost:3030/api",
    //baseURL: "http://localhost:3000/test",
    headers:{
        "Content-type": "application/json"
    }
});