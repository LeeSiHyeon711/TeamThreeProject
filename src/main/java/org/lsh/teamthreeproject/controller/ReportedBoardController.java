package org.lsh.teamthreeproject.controller;

import lombok.RequiredArgsConstructor;
import org.lsh.teamthreeproject.dto.ReportedBoardDTO;
import org.lsh.teamthreeproject.service.ReportedBoardService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report/board")
@RequiredArgsConstructor
public class ReportedBoardController {

    private final ReportedBoardService reportedBoardService;

    @PostMapping
    public void reportChatRoom(@RequestBody ReportedBoardDTO reportedBoardDTO) {
        reportedBoardService.saveReport(reportedBoardDTO);
    }
}
