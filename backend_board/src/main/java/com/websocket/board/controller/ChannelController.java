package com.websocket.board.controller;

import com.websocket.board.model.Channel;
import com.websocket.board.payload.*;
import com.websocket.board.repo.ChannelRedisRepository;
import com.websocket.board.repo.ChannelRepository;
import com.websocket.board.service.BoardClientService;
import com.websocket.board.service.ChannelService;
import com.websocket.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
//@RequestMapping("/api/board") // 로컬
public class ChannelController {

    private final ChannelRedisRepository channelRedisRepository;
    private final ChannelRepository channelRepository;
    private final ChannelService channelService;
    private final UserService userService;
    private final BoardClientService boardClientService;

    @GetMapping("/channels")
    public List<Channel> findAllChannels() {
        List<Channel> channels = channelRepository.findAll();
        return channels;
    }

    @PostMapping("/channel/validation")
    public ValidUserWithChannelResponse validUserWithChannel(
            @RequestBody ValidUserWithChannelRequest validUserWithChannelRequest) {

        if(channelService.validUserWithChannel(validUserWithChannelRequest)) {
            return new ValidUserWithChannelResponse().builder().isValid(true).build();
        } else {
            return new ValidUserWithChannelResponse().builder().isValid(false).build();
        }
    }

    @PostMapping("/channels")
    public List<Channel> myChannel(
            @RequestHeader(name = "Authorization") String Authorization,
            @RequestBody UserInfoRequest userInfoRequest) {
//        if(boardClientService.checkToken(Authorization).getIsValid()) {
//            channelService.saveChannel(createChannelRequest, channelId);
//        }
        // 각 사용자가 가진 채널 리스트 전달하기 <- 디비에서 가져오기
        List<Channel> channels = channelService.getMyChannels(userInfoRequest.getEmail());
        //List<Channel> channels = channelRedisRepository.findAllChannel();
        // 현재 채널에 접속해 있는 인원을 가져오는 부분
        // channels.stream().forEach(channel -> channel.setUserCount(channelRedisRepository.getUserCount(channel.getChannelId())));
        return channels;
    }

    @PostMapping("/channel")
    public Channel createChannel(
            @RequestHeader(name = "Authorization") String Authorization,
            @RequestBody CreateChannelRequest createChannelRequest) {
        
        // check token validation before creating channel
        boolean isValid = boardClientService.checkToken(Authorization).getIsValid();
        // save channel in mariadb
        Channel channel = null;
        if(isValid) {
            channel = channelService.createChannel(createChannelRequest);
            // save channel in redis
            channelRedisRepository.createChannel(createChannelRequest.getChannelName(), channel.getChannelId());
        }

        return channel;
    }

    @PostMapping("/channel/invitation")
    public InviteChannelResponse enterInvitedChannel(@RequestBody InviteChannelRequest inviteChannelRequest) {
        String channelId = inviteChannelRequest.getChannelId();
        // save channel in mariadb
        // 사용자를 먼저 저장
        userService.saveUser(inviteChannelRequest.getUser());
        // 채널과 사용자를 매핑해서 저장
        if(channelService.saveInvitedChannel(inviteChannelRequest, channelId)) {
            return new InviteChannelResponse().builder().message("Success Invitation").success(true).build();
        } else {
            return new InviteChannelResponse().builder().message("Fail Invitation").success(false).build();
        }
    }

    @DeleteMapping("/channel/withdrawal")
    public WithdrawalResponse channelInfo(@RequestBody WithdrawalRequest request) {
        //channelRedisRepository.removeUserEnterInfo(request.getChannelId());

        // 채널에 남아있는 사용자가 없으면 레디스에서 채널 삭제 -> 레디스 부하를 줄이기 위함
        if(channelService.getChannelMember(request.getChannelId()).size() == 0) {
            channelRedisRepository.deleteChannel(request.getChannelId());
        }
        channelService.withdrawalChannel(request);
        return new WithdrawalResponse().builder().message("Success Withdrawal Channel").success(true).build();
    }
}