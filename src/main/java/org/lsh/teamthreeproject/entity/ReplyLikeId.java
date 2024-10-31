package org.lsh.teamthreeproject.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

// BoardLike 테이블은 복합키를 가지므로 복합키를 하나의 클래스로 다시 만들어줌
@Embeddable
public class ReplyLikeId implements Serializable {
    private long replyId;
    private long userId;

    public ReplyLikeId() {}

    public ReplyLikeId(long replyId, long userId) {
        this.replyId = replyId;
        this.userId = userId;
    }

}
