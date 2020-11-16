package com.websocket.board.repo;

import com.websocket.board.model.notice.Notice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long>{

    List<Notice> findAllByChannelId(String channelId, Sort sort);
    Optional<Notice> findById(Long id);
    void deleteById(Long id);
}
