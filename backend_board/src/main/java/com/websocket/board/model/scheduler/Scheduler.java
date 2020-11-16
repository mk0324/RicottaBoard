package com.websocket.board.model.scheduler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.javers.core.metamodel.annotation.DiffIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Scheduler implements Serializable {

    private String id;
    @DiffIgnore
    private String left;
    @DiffIgnore
    private String top;

    private List<Event> events = new ArrayList<>();
}
