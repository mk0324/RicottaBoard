package com.websocket.board.model.poll;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Answer implements Serializable {

    private String answerId;
    private String answer;
    private int voted;
}
