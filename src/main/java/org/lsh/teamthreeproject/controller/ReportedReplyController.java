package org.lsh.teamthreeproject.controller;

import lombok.RequiredArgsConstructor;
import org.lsh.teamthreeproject.dto.ReportedReplyDTO;
import org.lsh.teamthreeproject.service.ReportedReplyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report/reply")
@RequiredArgsConstructor
public class ReportedReplyController {

    private final ReportedReplyService reportedReplyService;

    @PostMapping
    public void reportReply(@RequestBody ReportedReplyDTO reportedReplyDTO) {
        reportedReplyService.saveReport(reportedReplyDTO);
    }
}
