package com.hungrybird.back.AuthApp.model.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InviteChannelResponse {
    String data;
    String message;
    Boolean success;
}
