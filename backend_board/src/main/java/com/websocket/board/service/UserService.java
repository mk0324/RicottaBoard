package com.websocket.board.service;

import com.websocket.board.payload.LoginResponse;
import com.websocket.board.payload.UserInfoResponse;
import com.websocket.board.model.user.User;
import com.websocket.board.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void saveUser(UserInfoResponse userInfoResponse) {
        Optional<User> user = userRepository.findByUserId(userInfoResponse.getId());
        if(!user.isPresent()) {
            User boardUser = new User().builder()
                    .userId(userInfoResponse.getId())
                    .email(userInfoResponse.getEmail())
                    .nickname(userInfoResponse.getNickname())
                    .build();

            userRepository.save(boardUser);
        }
    }

//    public void saveUserTest(LoginResponse loginResponse, UserInfoResponse userResponse) {
//
//        // UserInfoResponse userInfoResponse = loginResponse.getUserInfoResponse();
//        Optional<User> user = userRepository.findByUserId(userResponse.getId());
//
//        if(!user.isPresent()) {
//
//            User boardUser = new User().builder()
//                    .userId(userResponse.getId())
//                    .email(userResponse.getEmail())
//                    .nickname(userResponse.getNickname())
//                    .token(loginResponse.getTokenType() + loginResponse.getAccessToken())
//                    .build();
//
//            userRepository.save(boardUser);
//        }
//    }

}
