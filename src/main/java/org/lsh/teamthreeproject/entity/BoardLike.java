package org.lsh.teamthreeproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="board_like")
public class BoardLike {
    @EmbeddedId
    private BoardLikeId id;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("boardId")
    @ManyToOne
    @JoinColumn(name = "board_Id", nullable = false)
    private Board board;

    @Column(name = "liked_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime likedDate;

    @Column(name = "is_deleted")
    @ColumnDefault("0")
    private Boolean isDeleted;
}
