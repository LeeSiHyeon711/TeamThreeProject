package org.lsh.teamthreeproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="chat_participant")
public class ChatParticipant {
    @Id
    @ManyToOne
    @JoinColumn(name = "chatroom_id", nullable = false)
    private Board board;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
