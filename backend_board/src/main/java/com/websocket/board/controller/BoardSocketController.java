package com.websocket.board.controller;

import com.websocket.board.model.SocketBoardMessage;
import com.websocket.board.repo.ChannelRedisRepository;
import com.websocket.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

//import com.websocket.board.service.DBSyncService;

@RequiredArgsConstructor
@Slf4j
@Controller
public class BoardSocketController {

    private final ChannelRedisRepository channelRedisRepository;
    private final BoardService boardService;

    /**
     * websocket "/pub/board/message"로 들어오는 메시징을 처리한다.
     * 클라이언트로 부터 받은 보드 상태 전달
     */
    @MessageMapping("/board/message")
    public void message(SocketBoardMessage message) {
        // Redis 세팅
        // 채널 인원수 세팅
        message.setUserCount(channelRedisRepository.getUserCount(message.getId()));
        // 레디스 보드 정보 업데이트
        channelRedisRepository.updateBoard(message);
        // 받아온 메시지 발송
        boardService.syncSocketBoardStatus(message);
    }
}
