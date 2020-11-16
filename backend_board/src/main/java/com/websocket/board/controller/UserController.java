package com.websocket.board.controller;

import com.websocket.board.payload.LoginRequest;
import com.websocket.board.payload.LoginResponse;
import com.websocket.board.service.BoardClientService;
import com.websocket.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final BoardClientService boardClientService;
    private final UserService userService;

    @PostMapping("/user/login")
    @ResponseBody
    public LoginResponse createChannel(@RequestBody LoginRequest request) {
        // auth 서버로 로그인 요청 후 토큰과 사용자 정보 받기
        LoginResponse loginResponse = boardClientService.callPostExternalServer(request);
//        UserInfoResponse userInfoResponse = memberClientService.callPostUserInfoExternalServer(
//                loginResponse.getAccessToken(),
//                request.getEmail());

        //loginResponse.setUserInfoResponse(userInfoResponse);

        // 보드 서버에 로그인한 사용자 정보 저장
        userService.saveUser(loginResponse.getUser());
        // 분기되서 요청할때
        // userService.saveUserTest(loginResponse, userInfoResponse);

        return loginResponse;
    }
}

