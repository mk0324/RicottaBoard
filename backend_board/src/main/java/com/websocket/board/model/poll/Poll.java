package com.websocket.board.model.poll;

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
public class Poll implements Serializable {

    private String pollId;
    @DiffIgnore
    private String left;
    @DiffIgnore
    private String top;
    private String question;

    private List<Answer> answers = new ArrayList<>();

    private boolean multipleVotes;
    private int totalVotes;

    private List<String> userVoted = new ArrayList<>();

    private boolean isSetAll;
    private boolean isEnd;

}
