package com.websocket.board.model.history;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
public class CommitMetadata implements Serializable {

    public String author;
    public List<Object> properties;
    public String commitDate;
    public String commitDateInstant;
    public Double id;
}
