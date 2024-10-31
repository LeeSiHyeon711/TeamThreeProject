package org.lsh.teamthreeproject.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

// BoardLike 테이블은 복합키를 가지므로 복합키를 하나의 클래스로 다시 만들어줌
@Embeddable
public class ChatParticipantId implements Serializable {
    private long chatRoomId;
    private long userId;

    public ChatParticipantId() {}

    public ChatParticipantId(long chatRoomId, long userId) {
        this.chatRoomId = chatRoomId;
        this.userId = userId;
    }

}
