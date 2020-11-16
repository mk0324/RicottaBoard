package com.websocket.board.model.history;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Module implements Serializable {

    public String valueObject;
    public OwnerId ownerId;
    public String fragment;
}
