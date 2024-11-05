package org.lsh.teamthreeproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private long reportId; // 신고 ID
    private String type; // 신고 유형: "Board", "ChatRoom", "Reply"
    private String reason; // 신고 사유
    private LocalDateTime reportedDate; // 신고 일시
    private String reportedUserName; // 신고된 사용자 이름
}
