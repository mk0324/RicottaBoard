package com.hungrybird.back.AuthApp.model.payload;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "update user info request", description = "User Info Update Request Payload")
public class UserInfoUpdateRequest {
    private String email;
    private String username;
    private String nickname;

    private String password;

    public UserInfoUpdateRequest() {
    }

    @Override
    public String toString() {
        return "UserInfoUpdateRequest{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public UserInfoUpdateRequest(String email, String username, String nickname, String password) {
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
