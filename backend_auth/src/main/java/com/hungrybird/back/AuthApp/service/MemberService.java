package com.hungrybird.back.AuthApp.service;

import com.hungrybird.back.AuthApp.GlobalVariables;
import com.hungrybird.back.AuthApp.model.User;
import com.hungrybird.back.AuthApp.model.member.Member;
import com.hungrybird.back.AuthApp.model.payload.InviteChannelRequest;
import com.hungrybird.back.AuthApp.model.payload.InviteChannelResponse;
import com.hungrybird.back.AuthApp.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    UserService userService;

    @Autowired
    ApiService<InviteChannelResponse> apiService;

    public List<Member> findByChannelId(String channelId) {
        return memberRepository.findAllByChannelId(channelId).orElseThrow(() -> new NoSuchElementException());
    }

    public Member createMember(User user, String channelId) {
        Member newMember = new Member();
        newMember.setChannelId(channelId);
        newMember.setUser(user);
        newMember.setAttendance(0);
        newMember.setMemberRoleName("MEMBER_NORMAL");
        newMember.setEmail(user.getEmail());
        return memberRepository.save(newMember);
    }

    public Member isMemberExist(String email, String channelId){
        return memberRepository.findByEmailAndChannelId(email,channelId).orElse(null);
    }

    // 사용자 정보 + 채널 정보 POST
    public InviteChannelResponse callPostBoardServer(InviteChannelRequest inviteChannelRequest) {
//        return apiService.post("http://k3a204.p.ssafy.io/board/channel/invitation"
//                , HttpHeaders.EMPTY, inviteChannelRequest, InviteChannelResponse.class).getBody();
        InviteChannelRequest inviteChannelRequest1 = inviteChannelRequest;
        System.out.println(inviteChannelRequest1.getChannelId()+"--------"+inviteChannelRequest1.getUser().getEmail());
//        return apiService.post("http://"+ GlobalVariables.host + GlobalVariables.boardPort + "/board/channel/invitation"

        return apiService.post("https://"+ GlobalVariables.host + GlobalVariables.boardPort + "/api/board/channel/invitation"
                , HttpHeaders.EMPTY, inviteChannelRequest, InviteChannelResponse.class).getBody();
    }

}
