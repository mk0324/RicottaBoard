package com.websocket.board.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.websocket.board.model.scheduler.Scheduler;
import com.websocket.board.model.kanban.Kanban;
import com.websocket.board.model.postit.Postit;
import com.websocket.board.model.user.UserChannel;
import lombok.*;
import org.javers.core.metamodel.annotation.DiffIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@DiffIgnore
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties("userList")
public class Channel implements Serializable {

    @Id
    private String channelId;
    private String channelName;

    @OneToMany(mappedBy = "channel")
    @JsonManagedReference
    @Builder.Default
    private List<UserChannel> userList = new ArrayList<>();

    public Channel createChannel() {
        Channel channel = new Channel().builder().build();
        channel.setChannelId(UUID.randomUUID().toString());
        return channel;
    }

    public Channel(String channelName) {
        this.channelName = channelName;
    }

}
