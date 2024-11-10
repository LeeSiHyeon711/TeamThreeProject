package org.lsh.teamthreeproject.controller;

import org.lsh.teamthreeproject.dto.ReportedBoardDTO;
import org.lsh.teamthreeproject.dto.ReportedChatRoomDTO;
import org.lsh.teamthreeproject.service.ReportedBoardService;
import org.lsh.teamthreeproject.service.ReportedChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report/board")
public class ReportedBoardController {
    private final ReportedBoardService reportedBoardService;

    @Autowired
    public ReportedBoardController(ReportedBoardService reportedBoardService) {
        this.reportedBoardService = reportedBoardService;
    }

    @PostMapping
    public void reportChatRoom(@RequestBody ReportedBoardDTO reportedBoardDTO) {
        reportedBoardService.saveReport(reportedBoardDTO);
    }
}
