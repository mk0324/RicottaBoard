package com.websocket.board.model.editor;

import lombok.*;
import org.javers.core.metamodel.annotation.DiffIgnore;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Editor implements Serializable{
    private String title;
    private String text;
    @DiffIgnore
    private String left;
    @DiffIgnore
    private String top;
    @DiffIgnore
    private Boolean isHidden;
    private Long mdId;
}

