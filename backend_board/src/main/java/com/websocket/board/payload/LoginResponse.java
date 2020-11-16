package com.websocket.board.payload;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse implements Serializable {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiryDuration;
    private UserInfoResponse user;

}
