package com.websocket.board.model.notice;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.javers.core.metamodel.annotation.DiffIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@DiffIgnore
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
public class Notice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String channelId;

    private String title;
    private String content;
    private String writer;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
