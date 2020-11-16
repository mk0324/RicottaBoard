package com.websocket.board.repo;

import com.websocket.board.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {
    Optional<Channel> findByChannelId(String channelId);

    @Override
    List<Channel> findAll();
}
