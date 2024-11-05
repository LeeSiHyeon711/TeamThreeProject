package org.lsh.teamthreeproject.controller;

import org.lsh.teamthreeproject.dto.ReportedChatRoomDTO;
import org.lsh.teamthreeproject.service.ReportedChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
public class ReportedChatRoomController {
    private final ReportedChatRoomService reportedChatRoomService;

    @Autowired
    public ReportedChatRoomController(ReportedChatRoomService reportedChatRoomService) {
        this.reportedChatRoomService = reportedChatRoomService;
    }

    @PostMapping
    public void reportChatRoom(@RequestBody ReportedChatRoomDTO reportedChatRoomDTO) {
        reportedChatRoomService.saveReport(reportedChatRoomDTO);
    }
}
