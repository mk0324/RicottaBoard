package com.websocket.board.model.video;

import lombok.*;
import org.javers.core.metamodel.annotation.DiffIgnore;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Video implements Serializable {
  private String id;
  private String vdId;
  private String userEmail;
  private String userNickname;
  @DiffIgnore
  private String left;
  @DiffIgnore
  private String top;
  @DiffIgnore
  private Boolean isHidden;
}
