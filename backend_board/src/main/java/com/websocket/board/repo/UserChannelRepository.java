package com.websocket.board.repo;

import com.websocket.board.model.Channel;
import com.websocket.board.model.user.User;
import com.websocket.board.model.user.UserChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserChannelRepository extends JpaRepository<UserChannel, Long> {
    Optional<UserChannel> findByUserEmail(String email);
    Optional<UserChannel> findByUserAndChannel(User user, Channel channel);
    Optional<List<UserChannel>> findAllByUser(User user);
    Optional<List<UserChannel>> findAllByChannel(Channel channel);
}
