package com.websocket.board.model.kanban;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Task implements Serializable {

    private String id;
    private String taskTitle;
    private String taskContents;
    private List<String> taskAssigner;
    private List<String> taskDates;
}
