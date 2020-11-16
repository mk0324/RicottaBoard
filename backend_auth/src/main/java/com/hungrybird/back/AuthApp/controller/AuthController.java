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

import com.hungrybird.back.AuthApp.GlobalVariables;
import com.hungrybird.back.AuthApp.event.*;
import com.hungrybird.back.AuthApp.exception.*;
import com.hungrybird.back.AuthApp.exception.*;
import com.hungrybird.back.AuthApp.model.CustomUserDetails;
import com.hungrybird.back.AuthApp.model.User;
import com.hungrybird.back.AuthApp.model.member.Member;
import com.hungrybird.back.AuthApp.model.payload.*;
import com.hungrybird.back.AuthApp.model.payload.*;
import com.hungrybird.back.AuthApp.model.token.EmailVerificationToken;
import com.hungrybird.back.AuthApp.model.token.RefreshToken;
import com.hungrybird.back.AuthApp.security.JwtTokenProvider;
import com.hungrybird.back.AuthApp.service.AuthService;
import com.hungrybird.back.AuthApp.service.MemberService;
import com.hungrybird.back.AuthApp.service.UserService;
import com.hungrybird.back.AuthApp.event.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.StringTokenizer;

//@RestController
@Controller
@RequestMapping("/api/auth")
@Api(value = "Authorization Rest API", description = "Defines endpoints that can be hit only when the user is not logged in. It's not secured by default.")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private static final Logger logger = Logger.getLogger(AuthController.class);
    private final AuthService authService;
    private final JwtTokenProvider tokenProvider;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserService userService;
    private final MemberService memberService;
    @Autowired
    public AuthController(MemberService memberService,UserService userService,AuthService authService, JwtTokenProvider tokenProvider, ApplicationEventPublisher applicationEventPublisher) {
        this.userService = userService;
        this.authService = authService;
        this.tokenProvider = tokenProvider;
        this.applicationEventPublisher = applicationEventPublisher;
        this.memberService = memberService;
    }


    @ApiOperation(value = "Checks if the given email is in use")
    @GetMapping("/checkEmailInUse")
    public ResponseEntity checkEmailInUse(@ApiParam(value = "Email id to check against") @RequestParam("email") String email) {
        Boolean emailExists = authService.emailAlreadyExists(email);
        return ResponseEntity.ok(new ApiResponse(true, emailExists.toString()));
    }

    @ApiOperation(value = "Checks if the given username is in use")
    @GetMapping("/checkUsernameInUse")
    public ResponseEntity checkUsernameInUse(@ApiParam(value = "Username to check against") @RequestParam(
            "username") String username) {
        Boolean usernameExists = authService.usernameAlreadyExists(username);
        return ResponseEntity.ok(new ApiResponse(true, usernameExists.toString()));
    }

    @ApiOperation(value = "Checks if the given nickname is in use")
    @GetMapping("/checkNicknameInUse")
    public ResponseEntity checkNicknameInUse(@ApiParam(value = "Nickname to check against") @RequestParam(
            "Nickname") String nickname) {
        Boolean nicknameAlreadyExists = authService.nicknameAlreadyExists(nickname);
        return ResponseEntity.ok(new ApiResponse(true, nicknameAlreadyExists.toString()));
    }

    @PostMapping("/login")
    @ApiOperation(value = "Logs the user in to the system and return the auth tokens")
    public ResponseEntity authenticateUser(@ApiParam(value = "The LoginRequest payload") @Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authService.authenticateUser(loginRequest)
                .orElseThrow(() -> new UserLoginException("Couldn't login user [" + loginRequest + "]"));

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        logger.info("Logged in User returned [API]: " + customUserDetails.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authService.createAndPersistRefreshTokenForDevice(authentication, loginRequest)
                .map(RefreshToken::getToken)
                .map(refreshToken -> {
                    String jwtToken = authService.generateToken(customUserDetails);
                    User user = userService.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new NoSuchElementException());
                    return ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken, refreshToken, tokenProvider.getExpiryDuration(),user));
                })
                .orElseThrow(() -> new UserLoginException("Couldn't create refresh token for: [" + loginRequest + "]"));
    }


    @PostMapping("/register")
    @ApiOperation(value = "Registers the user and publishes an event to generate the email verification")
    public ResponseEntity registerUser(@ApiParam(value = "The RegistrationRequest payload") @Valid @RequestBody RegistrationRequest registrationRequest) {

        return authService.registerUser(registrationRequest)
                .map(user -> {
                    UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.newInstance().scheme("http").host(GlobalVariables.host).port(9004).path("/api/auth/registrationConfirmation");
//                    UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/registrationConfirmation");
                    OnUserRegistrationCompleteEvent onUserRegistrationCompleteEvent = new OnUserRegistrationCompleteEvent(user, urlBuilder);
                    applicationEventPublisher.publishEvent(onUserRegistrationCompleteEvent);
                    logger.info("Registered User returned [API[: " + user);
                    return ResponseEntity.ok(new ApiResponse(true, "User registered successfully. Check your email for verification"));
                })
                .orElseThrow(() -> new UserRegistrationException(registrationRequest.getEmail(), "Missing user object in database"));
    }


    @PostMapping("/invite")
    @ApiOperation(value = "invite member to channel")
    public ResponseEntity inviteUser(@ApiParam(value = "invitation payload") @Valid @RequestBody MailSendRequest mailSendRequest) {

        List<Member> m = authService.inviteUser(mailSendRequest).orElse(null);
        return authService.inviteUser(mailSendRequest)
                .map(member -> {
                    UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.newInstance().scheme("http").host(GlobalVariables.host).port(9004).path("/api/auth/inviteConfirmation");
//                    UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/registrationConfirmation");
                    OnInvitationCompleteEvent onUserRegistrationCompleteEvent =
                            new OnInvitationCompleteEvent(mailSendRequest.getEmail(),mailSendRequest.getChannelId(),member, urlBuilder,
                                    mailSendRequest.getChannelName(), mailSendRequest.getFrom());
                    System.out.println(onUserRegistrationCompleteEvent.getMember().size()+" MEMBER SIZE");
                    applicationEventPublisher.publishEvent(onUserRegistrationCompleteEvent);
                    logger.info("Registered User returned [API[: " + member);
                    return ResponseEntity.ok(new InviteChannelResponse("data Object(json)", "String", true));
                })
                .orElseThrow(() -> new UserInvitationException(mailSendRequest.getEmail(), "Missing user object in database"));
    }


//    @GetMapping("/inviteConfirmation")
//    @ApiOperation(value = "Confirms the email verification token that has been generated for the user during registration")
//    public String confirmInvitation(@RequestParam String email, @RequestParam String channelId) {
//        StringTokenizer st = new StringTokenizer(email,",");
//        StringTokenizer st2 = new StringTokenizer(channelId,",");
//        System.out.println("----confirm----" + email);
//        String mail = "";
//        String channel = "";
//        System.out.println(st.countTokens());
//        for (int i=0; i<=st.countTokens();i++) {
//            mail = st.nextToken();
//            channel = st2.nextToken();
//            System.out.println("mail : "+ mail + " channel: "+channel);
//        }
//        User user = userService.findByEmail(mail).orElseThrow(() -> new NoSuchElementException());
//        if(memberService.isMemberExist(mail,channel)==null) {
//            if(user==null){
//                System.out.println(mail+" 님은 '이거모임'의 회원이 아닙니다.");
//            }else{
//                memberService.createMember(user, channel);
//            }
//            return "redirect:http://k3a204.p.ssafy.io";
//        }
//        else{
//            return "redirect:http://k3a204.p.ssafy.io";
//        }
//    }

    @GetMapping("/inviteConfirmation")
    @ApiOperation(value = "Confirms the email verification token that has been generated for the user during registration")
    public String confirmInvitation(@RequestParam String email, @RequestParam String channelId) {
        StringTokenizer st = new StringTokenizer(email,",");
        StringTokenizer st2 = new StringTokenizer(channelId,",");
        String mail = "";
        String channel = "";
        System.out.println(st.countTokens());
        for (int i=0; i<=st.countTokens();i++) {
            mail = st.nextToken();
            channel = st2.nextToken();
            System.out.println("mail : " + mail + " channel: " + channel);
        }
        System.out.println("--------------------------------------------------------"+email);
        User newUser = userService.findByEmail(mail).orElse(null);
        System.out.println("--------------------------------------------------------"+newUser.getEmail());
        InviteChannelRequest inviteChannelRequest = new InviteChannelRequest(newUser,channel);
        System.out.println("--------------------------------------------------------"+inviteChannelRequest.getUser().getEmail());
        InviteChannelResponse inviteChannelResponse = memberService.callPostBoardServer(inviteChannelRequest);
        System.out.println(inviteChannelResponse.getSuccess());
        System.out.println(inviteChannelResponse.getMessage());
        System.out.println(inviteChannelResponse.getData());
        // 멤버등록 후 리다이렉트

        if(inviteChannelResponse.getSuccess()){

            User user = userService.findByEmail(mail).orElseThrow(() -> new NoSuchElementException());
            if(memberService.isMemberExist(mail,channel)==null) {
                if(user==null){
                    System.out.println(mail+" 님은 '리코타보드'의 회원이 아닙니다.");
                }else{
                    memberService.createMember(user, channel);
                }
                return "redirect:http://"+ GlobalVariables.host + GlobalVariables.frontPort;
            }
            else{
                return "redirect:http://" + GlobalVariables.host + GlobalVariables.frontPort;
            }
        }else{
            return "redirect:http://"+ GlobalVariables.host + GlobalVariables.frontPort +"/error";
        }

    }
    @PostMapping("/password/resetlink")
    @ApiOperation(value = "Receive the reset link request and publish event to send mail containing the password " +
            "reset link")
    public ResponseEntity resetLink(@ApiParam(value = "The PasswordResetLinkRequest payload") @Valid @RequestBody PasswordResetLinkRequest passwordResetLinkRequest) {

        return authService.generatePasswordResetToken(passwordResetLinkRequest)
                .map(passwordResetToken -> {
                    UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.newInstance().scheme("http").host(GlobalVariables.host).port(9004).path("/password/reset");
//                    UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/password/reset");
                    OnGenerateResetLinkEvent generateResetLinkMailEvent = new OnGenerateResetLinkEvent(passwordResetToken,
                            urlBuilder);
                    applicationEventPublisher.publishEvent(generateResetLinkMailEvent);
                    return ResponseEntity.ok(new ApiResponse(true, "Password reset link sent successfully"));
                })
                .orElseThrow(() -> new PasswordResetLinkException(passwordResetLinkRequest.getEmail(), "Couldn't create a valid token"));
    }

    @PostMapping("/password/reset")
    @ApiOperation(value = "Reset the password after verification and publish an event to send the acknowledgement " +
            "email")
    public ResponseEntity resetPassword(@ApiParam(value = "The PasswordResetRequest payload") @Valid @RequestBody PasswordResetRequest passwordResetRequest) {

        return authService.resetPassword(passwordResetRequest)
                .map(changedUser -> {
                    OnUserAccountChangeEvent onPasswordChangeEvent = new OnUserAccountChangeEvent(changedUser, "Reset Password",
                            "Changed Successfully");
                    applicationEventPublisher.publishEvent(onPasswordChangeEvent);
                    return ResponseEntity.ok(new ApiResponse(true, "Password changed successfully"));
                })
                .orElseThrow(() -> new PasswordResetException(passwordResetRequest.getToken(), "Error in resetting password"));
    }


    @GetMapping("/registrationConfirmation")
    @ApiOperation(value = "Confirms the email verification token that has been generated for the user during registration")
    public String confirmRegistration(@ApiParam(value = "the token that was sent to the user email") @RequestParam("token") String token) {
        if(authService.confirmEmailRegistration(token)!=null){
//            return "redirect:http://k3a204.p.ssafy.io:3000/user/signup/done/registrationConfirmation";
            return "redirect:http://"+GlobalVariables.host+GlobalVariables.frontPort+"/user/signup/done/registrationConfirmation";
        }else{
            return "redirect:http://"+GlobalVariables.host+GlobalVariables.frontPort+"/error";
        }
    }


    @GetMapping("/resendRegistrationToken")
    @ApiOperation(value = "Resend the email registration with an updated token expiry. Safe to " +
            "assume that the user would always click on the last re-verification email and " +
            "any attempts at generating new token from past (possibly archived/deleted)" +
            "tokens should fail and report an exception. ")
    public ResponseEntity resendRegistrationToken(@ApiParam(value = "the initial token that was sent to the user email after registration") @RequestParam("token") String existingToken) {

        EmailVerificationToken newEmailToken = authService.recreateRegistrationToken(existingToken)
                .orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", existingToken, "User is already registered. No need to re-generate token"));

        return Optional.ofNullable(newEmailToken.getUser())
                .map(registeredUser -> {
                    UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/registrationConfirmation");
                    OnRegenerateEmailVerificationEvent regenerateEmailVerificationEvent = new OnRegenerateEmailVerificationEvent(registeredUser, urlBuilder, newEmailToken);
                    applicationEventPublisher.publishEvent(regenerateEmailVerificationEvent);
                    return ResponseEntity.ok(new ApiResponse(true, "Email verification resent successfully"));
                })
                .orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", existingToken, "No user associated with this request. Re-verification denied"));
    }


    @PostMapping("/refresh")
    @ApiOperation(value = "Refresh the expired jwt authentication by issuing a token refresh request and returns the" +
            "updated response tokens")
    public ResponseEntity refreshJwtToken(@ApiParam(value = "The TokenRefreshRequest payload") @Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {

        return authService.refreshJwtToken(tokenRefreshRequest)
                .map(updatedToken -> {
                    String refreshToken = tokenRefreshRequest.getRefreshToken();
                    logger.info("Created new Jwt Auth token: " + updatedToken);
                    return ResponseEntity.ok(new JwtAuthenticationResponse(updatedToken, refreshToken, tokenProvider.getExpiryDuration()));
                })
                .orElseThrow(() -> new TokenRefreshException(tokenRefreshRequest.getRefreshToken(), "Unexpected error during token refresh. Please logout and login again."));
    }

    @PostMapping("/verifyToken")
    public ResponseEntity validateToken(@RequestBody ValidTokenRequest validTokenRequest){
        StringTokenizer st = new StringTokenizer(validTokenRequest.getToken()," ");
        st.nextToken();
        String tokenBody = st.nextToken();
        String secretKey = tokenProvider.getJwtSecret();
        Claims claims =  Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .parseClaimsJws(tokenBody).getBody();
        String s = claims.getSubject();
        System.out.println("user id : "+s);
        User user = userService.findById(Long.parseLong(s)).orElse(null);
        if(user!=null) return ResponseEntity.ok(true);
        else return ResponseEntity.ok(false);
    }

}
