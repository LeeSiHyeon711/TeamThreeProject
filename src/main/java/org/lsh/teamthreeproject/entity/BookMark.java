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
@Table(name ="bookmark")
public class BookMark {
    @EmbeddedId
    private BookMarkId id;

    @MapsId("boardId")
    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 날짜 형식 지정
    @Column(name = "bookmarked_date", nullable = false)
    @CreationTimestamp
    private LocalDateTime bookmarkedDate;
}
