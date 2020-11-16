package com.websocket.board.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse implements Serializable {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String id;
    private String email;
    private String username;
    private String password;
    private String nickname;
    private Boolean activate;
    @Builder.Default
    private List<UserRole> roles = new ArrayList<>();
    private Boolean emailVerified;

}
