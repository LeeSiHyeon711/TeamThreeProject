package org.lsh.teamthreeproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="reported_reply")
public class ReportedReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replyId", nullable = false)
    private Reply reply;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportedBy", nullable = false)
    private User reportedBy;

    @Column(length = 2000)
    private String reason;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 날짜 형식 지정
    @Column(name = "reported_date", nullable = false)
    @CreationTimestamp
    private LocalDateTime reportedDate;
}
