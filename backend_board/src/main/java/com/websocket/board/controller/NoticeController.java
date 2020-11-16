package com.websocket.board.controller;

import com.websocket.board.model.notice.Notice;
import com.websocket.board.repo.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
//@RequestMapping("/api/board")   // 로컬
public class NoticeController {
    private final NoticeRepository noticeRepository;

    @GetMapping("/{channelId}/notice")
    public List<Notice> getAllNotice(@PathVariable String channelId) {
        return noticeRepository.findAllByChannelId(channelId, Sort.by("id").descending());
    }

    @PostMapping("/{channelId}/notice")
    public Notice createNotice(@PathVariable String channelId, @RequestBody Notice newNotice) {
        newNotice.setChannelId(channelId);
        noticeRepository.save(newNotice);
        return newNotice;
    }

    @GetMapping("/{channelId}/notice/{noticeId}")
    public Notice getNotice(@PathVariable String noticeId) {
        Long id = Long.parseLong(noticeId);
        Optional<Notice> notice = noticeRepository.findById(id);
        return notice.get();
    }

    @PutMapping("/{channelId}/notice/{noticeId}")
    public Notice putNotice(@PathVariable String noticeId, @RequestBody Notice updateNotice) {
        Long id = Long.parseLong(noticeId);
        Optional<Notice> notice = noticeRepository.findById(id);
        notice.get().setTitle(updateNotice.getTitle());
        notice.get().setContent(updateNotice.getContent());
        return notice.get();
    }

    @DeleteMapping("/{channelId}/notice/{noticeId}")
    public String deleteNotice(@PathVariable String noticeId) {
        Long id = Long.parseLong(noticeId);
        Optional<Notice> notice;
        noticeRepository.deleteById(id);
        return "Delete Success!";
    }
}
