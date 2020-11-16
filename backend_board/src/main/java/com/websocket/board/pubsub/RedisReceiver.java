package com.websocket.board.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.board.model.SocketBoardMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisReceiver implements MessageListener {
    // MessageListener : Listener of messages published in Redis

    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis에서 메시지가 발행(publish)되면 대기하고 있던 Receiver 가 해당 메시지를 받아 처리한다.
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // redis 에서 발행된 데이터를 받아 deserialize
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            // Board 객채로 맵핑
            SocketBoardMessage board = objectMapper.readValue(publishMessage, SocketBoardMessage.class);
            // 구독자에게 보드 상태 Send
            messagingTemplate.convertAndSend("/sub/board/channel/" + board.getId(), board);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
