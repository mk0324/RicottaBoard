package com.hungrybird.back.AuthApp.model.payload;


import com.hungrybird.back.AuthApp.model.Role;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserInfoRequest implements Serializable {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String id;
    private String email;
    private String username;
    private String password;
    private String nickname;
    private Boolean activate;
    @Builder.Default
    private List<Role> roles = new ArrayList<>();
    private Boolean emailVerified;

}
