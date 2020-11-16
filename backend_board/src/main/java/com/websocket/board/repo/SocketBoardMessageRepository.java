package com.websocket.board.repo;

import com.websocket.board.model.SocketBoardMessage;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@JaversSpringDataAuditable
public interface SocketBoardMessageRepository extends MongoRepository<SocketBoardMessage, String> {
    Optional<SocketBoardMessage> findById(String id);
}
