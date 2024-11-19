package org.lsh.teamthreeproject.controller;

import lombok.RequiredArgsConstructor;
import org.lsh.teamthreeproject.dto.ReportedChatRoomDTO;
import org.lsh.teamthreeproject.service.ReportedChatRoomService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportedChatRoomController {

    private final ReportedChatRoomService reportedChatRoomService;

    @PostMapping
    public void reportChatRoom(@RequestBody ReportedChatRoomDTO reportedChatRoomDTO) {
        reportedChatRoomService.saveReport(reportedChatRoomDTO);
    }
}
