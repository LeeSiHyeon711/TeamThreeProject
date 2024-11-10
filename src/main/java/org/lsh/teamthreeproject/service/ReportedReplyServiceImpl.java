package org.lsh.teamthreeproject.service;

import lombok.RequiredArgsConstructor;
import org.lsh.teamthreeproject.dto.ReportedReplyDTO;
import org.lsh.teamthreeproject.entity.Reply;
import org.lsh.teamthreeproject.entity.ReportedReply;
import org.lsh.teamthreeproject.entity.User;
import org.lsh.teamthreeproject.repository.ReplyRepository;
import org.lsh.teamthreeproject.repository.ReportedReplyRepository;
import org.lsh.teamthreeproject.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportedReplyServiceImpl implements ReportedReplyService {

    private final ReportedReplyRepository reportedReplyRepository;
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;

    @Override
    public void saveReport(ReportedReplyDTO reportedReplyDTO) {
        // Reply 객체 조회
        Reply reply = replyRepository.findById(reportedReplyDTO.getReplyId())
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        // User 객체 조회
        User user = userRepository.findByNickname(reportedReplyDTO.getReportedBy())
                .orElseThrow(() -> new IllegalArgumentException("해당 닉네임의 유저가 존재하지 않습니다."));

        // ReportedReply 객체 생성
        ReportedReply report = ReportedReply.builder()
                .reply(reply)
                .reportedBy(user)
                .reason(reportedReplyDTO.getReason())
                .build();

        // 신고 정보 저장
        reportedReplyRepository.save(report);
    }
}
