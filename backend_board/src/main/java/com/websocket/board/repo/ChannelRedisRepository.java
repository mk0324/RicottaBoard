package com.websocket.board.repo;

import com.websocket.board.model.Channel;
import com.websocket.board.model.SocketBoardMessage;
import com.websocket.board.model.kanban.Kanban;
import com.websocket.board.model.scheduler.Scheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChannelRedisRepository {
    // Redis CacheKeys
    private static final String CHANNEL = "CHANNEL"; // 채널 저장
    private static final String USER_COUNT = "USER_COUNT"; // 채널에 입장한 클라이언트 수 저장
    private static final String ENTER_INFO = "ENTER_INFO"; // 채널에 입장한 클라이언트의 sessionId와 채널 id를 맵핑한 정보 저장
    //private static final String USER_INFO = "USER_INFO"; // 채널에 입장한 멤버들의 정보를 채널 id와 매핑하여 저장
    private static final String BOARD = "BOARD"; // 채널이 가지고 있는 보드의 상태 저장

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Channel> hashOpsChannel;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsEnterInfo;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, SocketBoardMessage> hashOpsBoard;
//    @Resource(name = "redisTemplate")
//    private HashOperations<String, String, User> hashOpsMember;

//    모든 채널 조회
    public List<Channel> findAllChannel() {
        return hashOpsChannel.values(CHANNEL);
    }

    // 채널 생성 : 서버간? 채널 공유를 위해 redis hash에 저장한다.
    // -> 현 단계에서는 굳이 채널의 부하를 줄이기 위해 서버를 나누어 채널 정보를 공유하는 것이 중요하지 않은 작업이므로 보류
    public Channel createChannel(String channelName, String channelId) {
        Channel channel = Channel.builder()
                .channelName(channelName)
                .channelId(channelId)
                .build();
        hashOpsChannel.put(CHANNEL, channelId, channel);
        // 채널당 보드가 생성되므로 채널을 생성할때 레디스에 채널 아이디를 hashkey 값으로 보드도 생성
        createBoard(channelId);
        return channel;
    }

    // 보드 생성
    // 보드 리스트 널 값으로 가면 프론트 에러 발생 -> 빈 리스트 값 넣어주기
    private void createBoard(String channelId) {
        SocketBoardMessage board = new SocketBoardMessage()
                .builder()
                .id(channelId)
                .postitList(new ArrayList<>())
                .kanban(new Kanban())
                .scheduler(new Scheduler())
                .poll(new ArrayList<>())
                .editorList(new ArrayList<>())
                .build();
        hashOpsBoard.put(BOARD, channelId, board);
    }

    public Channel updateChannel(Channel channel) {
        hashOpsChannel.put(CHANNEL, channel.getChannelId(), channel);
        return channel;
    }

    public SocketBoardMessage updateBoard(SocketBoardMessage board) {
        hashOpsBoard.put(BOARD, board.getId(), board);
        return board;
    }

    // 특정 채널 조회
    public Channel findChannelById(String channelId) {
        return hashOpsChannel.get(CHANNEL, channelId);
    }

    // 특정 채널의 보드 조회
    public SocketBoardMessage findBoardByChannelId(String channelId) {
        SocketBoardMessage socketBoardMessage = hashOpsBoard.get(BOARD, channelId);
        return socketBoardMessage;
    }

    // 레디스에서 채널 삭제
    public void deleteChannel(String channelId) {
        hashOpsBoard.delete(BOARD, channelId);
        hashOpsChannel.delete(CHANNEL, channelId);
    }

    // 유저가 입장한 채널ID와 유저 세션ID 맵핑 정보 저장
    public void setUserEnterInfo(String sessionId, String channelId) {
        hashOpsEnterInfo.put(ENTER_INFO, sessionId, channelId);
    }

    // 유저 세션으로 입장해 있는 채널 ID 조회
    public String getUserEnterChannelId(String sessionId) {
        return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
    }

    // 유저 세션정보와 맵핑된 채널ID 삭제
    public void removeUserEnterInfo(String sessionId) {
        hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
    }

    // 채널 유저수 조회
    public long getUserCount(String channelId) {
        return Long.valueOf(
                Optional
                        .ofNullable(valueOps.get(USER_COUNT + "_" + channelId))
                        .orElse("0"));
    }

    // 채팅방에 입장한 유저수 +1
    public long plusUserCount(String channelId) {
        return Optional
                .ofNullable(valueOps.increment(USER_COUNT + "_" + channelId))
                .orElse(0L);
    }

    // 채팅방에 입장한 유저수 -1
    public long minusUserCount(String channelId) {
        return Optional
                .ofNullable(valueOps.decrement(USER_COUNT + "_" + channelId))
                .filter(count -> count > 0) // 0보다 크지 않다면
                .orElse(0L);    // 0으로 세팅
    }
}
