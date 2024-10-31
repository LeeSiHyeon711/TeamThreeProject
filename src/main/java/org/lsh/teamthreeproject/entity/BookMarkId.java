package org.lsh.teamthreeproject.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class BookMarkId {
    private long boardId;
    private long userId;
    public BookMarkId() {}

    public BookMarkId(long boardId, long userId) {
        this.boardId = boardId;
        this.userId = userId;
    }
}
