package org.lsh.teamthreeproject.service;

import lombok.RequiredArgsConstructor;
import org.lsh.teamthreeproject.dto.ReportedBoardDTO;
import org.lsh.teamthreeproject.entity.Board;
import org.lsh.teamthreeproject.entity.ReportedBoard;
import org.lsh.teamthreeproject.entity.User;
import org.lsh.teamthreeproject.repository.BoardRepository;
import org.lsh.teamthreeproject.repository.ReportedBoardRepository;
import org.lsh.teamthreeproject.repository.UserRepository;
import org.lsh.teamthreeproject.service.ReportedBoardService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportedBoardServiceImpl implements ReportedBoardService {

    private final ReportedBoardRepository reportedBoardRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Override
    public void saveReport(ReportedBoardDTO reportedBoardDTO) {
        // Board 객체 조회
        Board board = boardRepository.findById(reportedBoardDTO.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        // User 객체 조회
        User user = userRepository.findByNickname(reportedBoardDTO.getReportedBy())
                .orElseThrow(() -> new IllegalArgumentException("해당 닉네임의 유저가 존재하지 않습니다."));

        // ReportedBoard 객체 생성
        ReportedBoard report = ReportedBoard.builder()
                .board(board)
                .reportedUser(user)
                .reason(reportedBoardDTO.getReason())
                .build();

        // 신고 정보 저장
        reportedBoardRepository.save(report);
    }
}
