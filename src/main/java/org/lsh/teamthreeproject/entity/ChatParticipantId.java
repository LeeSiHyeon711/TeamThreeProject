package org.lsh.teamthreeproject.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

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
