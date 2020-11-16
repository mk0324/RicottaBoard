package com.websocket.board.model.postit;

import lombok.*;
import org.javers.core.metamodel.annotation.DiffIgnore;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Postit implements Serializable {

    private Long id;

    private String title;
    private String contents;
    @DiffIgnore
    private String left;
    @DiffIgnore
    private String top;
    @DiffIgnore
    private Long frontPostitId;
}
