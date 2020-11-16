import http from "../http-common2";

class ChatlogDataService{
    getAll(){
        return http.get("/chatlogs");
    }

    // get(id){
    //     return http.get(`/tutorials/${id}`);
    // }

    create(data){
        return http.post("/chatlogs", data);
    }

    // update(id, data){
    //     return http.put(`/tutorials/${id}`, data);
    // }

    // delete(id){
    //     return http.delete(`/tutorials/${id}`);
    // }

    // deleteAll(){
    //     return http.delete(`/tutorials`);
    // }

    // findByTitle(title){
    //     return http.get(`/tutorials?title=${title}`);
    // }
}

export default new ChatlogDataService();