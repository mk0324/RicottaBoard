package com.websocket.board.model.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.javers.core.metamodel.annotation.DiffIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@DiffIgnore
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    //@Column(nullable = false, name = "user_id")
    private String userId;

    private String email;
    private String username;
    private String nickname;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    @Builder.Default
    private List<UserChannel> userChannels = new ArrayList<>();
}
