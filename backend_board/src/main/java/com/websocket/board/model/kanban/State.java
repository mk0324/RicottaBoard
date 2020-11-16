package com.websocket.board.model.kanban;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class State implements Serializable {

    private String id;
    private String columnTitle;

    private List<Task> tasks = new ArrayList<>();
}
