/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hungrybird.back.AuthApp.controller;

import com.hungrybird.back.AuthApp.annotation.CurrentUser;
import com.hungrybird.back.AuthApp.event.OnUserAccountChangeEvent;
import com.hungrybird.back.AuthApp.event.OnUserLogoutSuccessEvent;
import com.hungrybird.back.AuthApp.exception.UpdatePasswordException;
import com.hungrybird.back.AuthApp.model.CustomUserDetails;
import com.hungrybird.back.AuthApp.model.User;
import com.hungrybird.back.AuthApp.model.payload.*;
import com.hungrybird.back.AuthApp.model.payload.ApiResponse;
import com.hungrybird.back.AuthApp.model.payload.LogOutRequest;
import com.hungrybird.back.AuthApp.model.payload.UpdatePasswordRequest;
import com.hungrybird.back.AuthApp.service.AuthService;
import com.hungrybird.back.AuthApp.service.EmailVerificationTokenService;
import com.hungrybird.back.AuthApp.service.UserService;
import com.hungrybird.back.AuthApp.model.payload.UserInfoUpdateRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth/user")
@Api(value = "User Rest API", description = "Defines endpoints for the logged in user. It's secured by default")
@CrossOrigin(origins = "http://localhost:3000")
// @CrossOrigin(origins = "http://localhost:3001")
public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class);

    private final AuthService authService;

    private final UserService userService;

    private final EmailVerificationTokenService emailVerificationTokenService;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public UserController(AuthService authService, UserService userService, EmailVerificationTokenService emailVerificationTokenService, ApplicationEventPublisher applicationEventPublisher) {
        this.authService = authService;
        this.userService = userService;
        this.emailVerificationTokenService = emailVerificationTokenService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Gets the current user profile of the logged in user
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "Returns the current user profile")
    public ResponseEntity getUserProfile(@CurrentUser CustomUserDetails currentUser) {
        logger.info(currentUser.getEmail() + " has role: " + currentUser.getRoles());
        return ResponseEntity.ok("Hello. This is about me");
    }

    /**
     * Gets the current user profile of the logged in user
     */
    @GetMapping("/userInfo")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "Returns the current user profile")
    public ResponseEntity getUserInfo(@RequestParam String email) {
        return new ResponseEntity<>(userService.findByEmail(email), HttpStatus.OK);
    }

    /**
     * Sets the new user data of the logged in user
     */
    @PostMapping("/userInfo")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "Update user profile")
    public ResponseEntity setUserInfo(@RequestBody UserInfoUpdateRequest newUser){

        Optional<User> tempUserData = userService.findByEmail(newUser.getEmail());

        if(tempUserData.isPresent()){


            User user = tempUserData.get();

            boolean isCorrect = userService.checkPassword(user.getPassword(), newUser.getPassword());

            System.out.println("=================Check : " + isCorrect);

            if(!isCorrect) return new ResponseEntity(HttpStatus.BAD_REQUEST);

            user.setNickname(newUser.getNickname());
            user.setUsername(newUser.getUsername());
            userService.save(user);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "Delete", response = String.class)
    @DeleteMapping("/delete")
    public ResponseEntity deleteUser(@RequestParam String email, @RequestParam String password) {
        logger.debug("delete - 호출");
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        User user = userService.findByEmail(email).orElseThrow(() -> new NoResultException("ohlcv result set null"));
        System.out.println(hashedPassword + "hashed ");
        System.out.println(user.getPassword() + "get");
        userService.deleteDevice(user.getId());
        emailVerificationTokenService.deleteEmailVerificationToken(user);
        userService.deleteByEamil(user.getEmail(),user.getPassword());
        return new ResponseEntity<>("success", HttpStatus.OK);
    }


    /**
     * Returns all admins in the system. Requires Admin access
     */
    @GetMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Returns the list of configured admins. Requires ADMIN Access")
    public ResponseEntity getAllAdmins() {
        logger.info("Inside secured resource with admin");
        return ResponseEntity.ok("Hello. This is about admins");
    }

    /**
     * Updates the password of the current logged in user
     */
    @PostMapping("/password/update")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "Allows the user to change his password once logged in by supplying the correct current " +
            "password")
    public ResponseEntity updateUserPassword(@CurrentUser CustomUserDetails customUserDetails,
                                             @ApiParam(value = "The UpdatePasswordRequest payload") @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {

        return authService.updatePassword(customUserDetails, updatePasswordRequest)
                .map(updatedUser -> {
                    OnUserAccountChangeEvent onUserPasswordChangeEvent = new OnUserAccountChangeEvent(updatedUser, "Update Password", "Change successful");
                    applicationEventPublisher.publishEvent(onUserPasswordChangeEvent);
                    return ResponseEntity.ok(new ApiResponse(true, "Password changed successfully"));
                })
                .orElseThrow(() -> new UpdatePasswordException("--Empty--", "No such user present."));
    }

    /**
     * Log the user out from the app/device. Release the refresh token associated with the
     * user device.
     */
    @PostMapping("/logout")
    @ApiOperation(value = "Logs the specified user device and clears the refresh tokens associated with it")
    public ResponseEntity logoutUser(@CurrentUser CustomUserDetails customUserDetails,
                                     @ApiParam(value = "The LogOutRequest payload") @Valid @RequestBody LogOutRequest logOutRequest) {
        userService.logoutUser(customUserDetails, logOutRequest);
        Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();

        OnUserLogoutSuccessEvent logoutSuccessEvent = new OnUserLogoutSuccessEvent(customUserDetails.getEmail(), credentials.toString(), logOutRequest);
        applicationEventPublisher.publishEvent(logoutSuccessEvent);
        return ResponseEntity.ok(new ApiResponse(true, "Log out successful"));
    }
}
