package org.lsh.teamthreeproject.service;

import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.lsh.teamthreeproject.dto.ReportDTO;
import org.lsh.teamthreeproject.entity.*;
import org.lsh.teamthreeproject.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ReportServiceImpl implements ReportService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ReplyRepository replyRepository;
    private final ReportedBoardRepository reportedBoardRepository;
    private final ReportedChatRoomRepository reportedChatRoomRepository;
    private final ReportedReplyRepository reportedReplyRepository;

    // 통합 조회 메서드
    @Override
    public List<ReportDTO> getAllReports() {
        return getAllReports(null); // 모든 보고서 반환
    }

    // 특정 유형의 신고 조회 메서드
    @Override
    public List<ReportDTO> getAllReports(String type) {
        List<ReportDTO> reports = new ArrayList<>();

        if (type == null || type.equalsIgnoreCase("Board")) {
            reports.addAll(reportedBoardRepository.findAll().stream()
                    .map(board -> new ReportDTO(
                            board.getReportId(),
                            "Board",
                            board.getReason(),
                            board.getReportedDate(),
                            board.getReportedUser().getNickname()))
                    .collect(Collectors.toList()));
        }

        if (type == null || type.equalsIgnoreCase("ChatRoom")) {
            reports.addAll(reportedChatRoomRepository.findAll().stream()
                    .map(chatRoom -> new ReportDTO(
                            chatRoom.getReportId(),
                            "ChatRoom",
                            chatRoom.getReason(),
                            chatRoom.getReportedDate(),
                            chatRoom.getReportedBy().getNickname()))
                    .collect(Collectors.toList()));
        }

        if (type == null || type.equalsIgnoreCase("Reply")) {
            reports.addAll(reportedReplyRepository.findAll().stream()
                    .map(reply -> new ReportDTO(
                            reply.getReportId(),
                            "Reply",
                            reply.getReason(),
                            reply.getReportedDate(),
                            reply.getReportedBy().getNickname()))
                    .collect(Collectors.toList()));
        }

        return reports;
    }

    // 신고 삭제 메서드
    @Override
    public void deleteReport(long reportId) {
        Optional<ReportedBoard> boardReport = reportedBoardRepository.findById(reportId);
        if (boardReport.isPresent()) {
            reportedBoardRepository.delete(boardReport.get());
            return;
        }

        Optional<ReportedChatRoom> chatRoomReport = reportedChatRoomRepository.findById(reportId);
        if (chatRoomReport.isPresent()) {
            reportedChatRoomRepository.delete(chatRoomReport.get());
            return;
        }

        Optional<ReportedReply> replyReport = reportedReplyRepository.findById(reportId);
        if (replyReport.isPresent()) {
            reportedReplyRepository.delete(replyReport.get());
        }
    }

    // 신고 저장 메서드
    @Override
    public void saveReport(ReportDTO reportDTO) {
        log.info("!!!!!!!!!!!!!!!!!!!!!!!!!Received report data: {}", reportDTO); // 추가된 로그

        String reportType = reportDTO.getType();
        String reason = reportDTO.getReason();
        String reportedUserName = reportDTO.getReportedUserName();

        log.info("!!!!!!!!!!!!!!!!!!!!!!!!!Report Type: {}, Reason: {}, Reported User: {}", reportType, reason, reportedUserName);

        User reportedUser = userRepository.findByNickname(reportedUserName)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        switch (reportType) {
            case "Board":
                log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!Processing Board report"); // 추가된 로그
                Board board = boardRepository.findById(reportDTO.getReportId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
                ReportedBoard boardReport = ReportedBoard.builder()
                        .board(board)
                        .reportedUser(reportedUser)
                        .reason(reason)
                        .reportedDate(LocalDateTime.now())
                        .build();
                reportedBoardRepository.save(boardReport);
                break;

            case "ChatRoom":
                log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!Processing ChatRoom report"); // 추가된 로그
                ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(reportDTO.getReportId())
                        .orElseThrow(() -> {
                            log.error("해당 채팅방이 존재하지 않습니다. 요청된 채팅방 ID: " + reportDTO.getReportId());
                            return new IllegalArgumentException("해당 채팅방이 존재하지 않습니다.");
                        });
                ReportedChatRoom chatRoomReport = ReportedChatRoom.builder()
                        .chatRoom(chatRoom)
                        .reportedBy(reportedUser)
                        .reason(reason)
                        .reportedDate(LocalDateTime.now())
                        .build();
                reportedChatRoomRepository.save(chatRoomReport);
                break;

            case "Reply":
                log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Processing Reply report"); // 추가된 로그
                Reply reply = replyRepository.findById(reportDTO.getReportId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
                ReportedReply replyReport = ReportedReply.builder()
                        .reply(reply)
                        .reportedBy(reportedUser)
                        .reason(reason)
                        .reportedDate(LocalDateTime.now())
                        .build();
                reportedReplyRepository.save(replyReport);
                break;

            default:
                log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Invalid report type: " + reportType); // 추가된 로그
                throw new IllegalArgumentException("Invalid report type: " + reportType);
        }
    }

}