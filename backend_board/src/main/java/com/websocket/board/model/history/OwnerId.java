package com.websocket.board.model.history;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class OwnerId implements Serializable {

    public String entity;
    public String cdoId;

}
