package org.lsh.teamthreeproject.controller;

import lombok.RequiredArgsConstructor;
import org.lsh.teamthreeproject.dto.ReplyDTO;
import org.lsh.teamthreeproject.service.ReplyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/replies")
public class ReplyRestController {
    private final ReplyService replyService;

    @GetMapping
    public Page<ReplyDTO> getPagedReplyList(
            @RequestParam("userId") Long userId,
            @RequestParam(defaultValue = "0") int page
    ) {
        int size = 4;
        Pageable pageable = PageRequest.of(page, size);
        return replyService.getReplies(userId, pageable);
    }
}
