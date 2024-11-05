package org.lsh.teamthreeproject.service;

import org.lsh.teamthreeproject.dto.ReportDTO;
import org.lsh.teamthreeproject.entity.ReportedBoard;
import org.lsh.teamthreeproject.entity.ReportedChatRoom;
import org.lsh.teamthreeproject.entity.ReportedReply;
import org.lsh.teamthreeproject.repository.ReportedBoardRepository;
import org.lsh.teamthreeproject.repository.ReportedChatRoomRepository;
import org.lsh.teamthreeproject.repository.ReportedReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportedBoardRepository reportedBoardRepository;
    private final ReportedChatRoomRepository reportedChatRoomRepository;
    private final ReportedReplyRepository reportedReplyRepository;

    @Autowired
    public ReportServiceImpl(ReportedBoardRepository reportedBoardRepository,
                             ReportedChatRoomRepository reportedChatRoomRepository,
                             ReportedReplyRepository reportedReplyRepository) {
        this.reportedBoardRepository = reportedBoardRepository;
        this.reportedChatRoomRepository = reportedChatRoomRepository;
        this.reportedReplyRepository = reportedReplyRepository;
    }
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
        // 신고 ID에 해당하는 신고가 존재하는지 확인
        Optional<ReportedBoard> boardReport = reportedBoardRepository.findById(reportId);
        if (boardReport.isPresent()) {
            reportedBoardRepository.delete(boardReport.get());
            return; // 삭제 완료 후 종료
        }

        Optional<ReportedChatRoom> chatRoomReport = reportedChatRoomRepository.findById(reportId);
        if (chatRoomReport.isPresent()) {
            reportedChatRoomRepository.delete(chatRoomReport.get());
            return; // 삭제 완료 후 종료
        }

        Optional<ReportedReply> replyReport = reportedReplyRepository.findById(reportId);
        if (replyReport.isPresent()) {
            reportedReplyRepository.delete(replyReport.get());
            return; // 삭제 완료 후 종료
        }

        // 삭제할 신고가 없을 경우 적절한 예외 처리를 추가할 수 있음
    }
}
