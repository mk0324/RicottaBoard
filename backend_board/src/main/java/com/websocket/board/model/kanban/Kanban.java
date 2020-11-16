package com.websocket.board.model.kanban;

import lombok.*;
import org.javers.core.metamodel.annotation.DiffIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Kanban implements Serializable {

    private String id;
    private String kanbanName;
    @DiffIgnore
    private String left;
    @DiffIgnore
    private String top;
    
    private List<State> states = new ArrayList<>();
}
