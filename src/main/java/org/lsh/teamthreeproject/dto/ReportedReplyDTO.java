package org.lsh.teamthreeproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportedReplyDTO {
    private Long reportId;         // 신고 ID
    private Long replyId;          // 신고된 댓글 ID
    private String reportedBy;     // 신고자 ID
    private String reason;         // 신고 사유
    private LocalDateTime reportedDate; // 신고 날짜
}
