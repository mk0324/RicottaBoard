package com.websocket.board.config.handler;

import com.websocket.board.repo.ChannelRedisRepository;
import com.websocket.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final ChannelRedisRepository channelRedisRepository;
    private final BoardService boardService;

    // WebSocket 을 통해 들어온 요청이 처리 되기전 실행된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT == accessor.getCommand()) { // websocket 연결요청
//            String name = Optional.ofNullable((Principal) message.getHeaders().get("nativeHeaders")).map(Principal::getName).orElse("UnknownUser");
            List<String> nativeHeaders = accessor.getNativeHeader("userNickname");
            String userNickname = nativeHeaders.get(0);
            log.info("CONNECT {}", userNickname);
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) { // 해당 채널 구독요청
            // header정보에서 구독 destination정보를 얻고, 채널id 추출한다.
            String channelId = boardService.getChannelId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidChannelId"));
            // 채널에 들어온 클라이언트 sessionId를 channelId와 맵핑해 놓는다.(나중에 특정 세션이 어떤 채널에 들어가 있는지 알기 위함)
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            channelRedisRepository.setUserEnterInfo(sessionId, channelId);
            // 채널의 인원수를 +1한다.
            channelRedisRepository.plusUserCount(channelId);
            // 클라이언트 입장 메시지를 채널에 발송한다.(redis publish)
//            String name = Optional.ofNullable((Principal) message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");
            //boardService.syncBoardStatus(Board.builder().type(ChatMessage.MessageType.ENTER).roomId(roomId).sender(name).build());
//            List<String> nativeHeaders = accessor.getNativeHeader("userNickname");
//            String userNickname = nativeHeaders.get(0);
//            log.info("SUBSCRIBED {}, {}", userNickname, channelId);
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) { // Websocket 연결 종료
            // 연결이 종료된 클라이언트 sesssionId로 채널 id를 얻는다.
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String channelId = channelRedisRepository.getUserEnterChannelId(sessionId);
            // 채널의 인원수를 -1한다.
            channelRedisRepository.minusUserCount(channelId);
            // 클라이언트 퇴장 메시지를 채팅방에 발송한다.(redis publish)
            String name = Optional.ofNullable((Principal) message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");
            //boardService.syncBoardStatus(ChatMessage.builder().type(ChatMessage.MessageType.QUIT).roomId(roomId).sender(name).build());
            // 퇴장한 클라이언트의 roomId 맵핑 정보를 삭제한다.
            channelRedisRepository.removeUserEnterInfo(sessionId);
            log.info("DISCONNECTED {}, {}", sessionId, channelId);
        }
        return message;
    }
}
