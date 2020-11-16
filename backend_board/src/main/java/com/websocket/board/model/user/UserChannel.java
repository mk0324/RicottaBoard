package com.websocket.board.model.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.websocket.board.model.Channel;
import lombok.*;
import org.javers.core.metamodel.annotation.DiffIgnore;

import javax.persistence.*;
import java.io.Serializable;

@DiffIgnore
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserChannel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "user_channel_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    @JsonBackReference
    private Channel channel;

}
