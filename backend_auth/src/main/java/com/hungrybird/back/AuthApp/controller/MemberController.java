package com.hungrybird.back.AuthApp.controller;

import com.hungrybird.back.AuthApp.model.User;
import com.hungrybird.back.AuthApp.service.MemberService;
import com.hungrybird.back.AuthApp.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/auth/member")
@Api(value = "Member Rest API", description = "Defines endpoints for the logged in member. It's secured by default")
@CrossOrigin(origins = "http://localhost:3000")
public class MemberController {

    private final MemberService memberService;
    private final UserService userService;
    @Autowired
    MemberController(MemberService memberService,UserService userService){
        this.memberService = memberService;
        this.userService = userService;
    }

    @GetMapping("/memberList")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "Returns the current member list")
    public ResponseEntity getUserInfo(@RequestParam String channelId) {
        return new ResponseEntity<>(memberService.findByChannelId(channelId), HttpStatus.OK);
    }

    @PostMapping("/register")
    @ApiOperation(value = "Registers the member")
    public ResponseEntity registerUser(@RequestParam String channelId, @RequestParam Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new NoSuchElementException());
        return new ResponseEntity<>(memberService.createMember(user,channelId),HttpStatus.ACCEPTED);
    }
}
