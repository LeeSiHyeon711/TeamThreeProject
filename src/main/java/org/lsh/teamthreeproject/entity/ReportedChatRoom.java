package org.lsh.teamthreeproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="reported_chatroom")
public class ReportedChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private long reportId;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "reported_by", nullable = false)
    private User reportedBy;

    @Column(length = 1000)
    private String reason;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 날짜 형식 지정
    @Column(name = "reported_date", nullable = false)
    @CreationTimestamp
    private LocalDateTime reportedDate;
}
