package com.websocket.board.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InviteChannelRequest implements Serializable {
    private String channelId;
    private UserInfoResponse user;
}
