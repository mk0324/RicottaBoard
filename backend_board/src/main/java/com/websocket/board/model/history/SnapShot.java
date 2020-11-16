package com.websocket.board.model.history;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class SnapShot implements Serializable {

    public CommitMetadata commitMetadata;
    public GlobalId globalId;
    public State state;
    public List<String> changedProperties;
    public String type;
    public Integer version;

}
