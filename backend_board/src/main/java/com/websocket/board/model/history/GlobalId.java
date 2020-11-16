package com.websocket.board.model.history;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class GlobalId implements Serializable {

    public String entity;
    public String cdoId;

}
