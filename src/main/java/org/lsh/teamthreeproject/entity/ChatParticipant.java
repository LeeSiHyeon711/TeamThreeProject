package org.lsh.teamthreeproject.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="chat_participant")
public class ChatParticipant {
    @EmbeddedId
    private ChatParticipantId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("chatRoomId") // 복합키의 chatRoomId와 매핑
    @JoinColumn(name = "chatroom_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId") // 복합키의 userId와 매핑
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
