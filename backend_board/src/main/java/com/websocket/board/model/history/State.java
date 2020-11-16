package com.websocket.board.model.history;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class State implements Serializable {

    public Module scheduler;
    public String editUser;
    public List<Module> postitList;
    public List<String> memberList;
    public Integer idCount;
    public Integer userCount;
    public Module kanban;
    public String id;
    public List<Module> poll;
    public List<Module> editorList;

}
