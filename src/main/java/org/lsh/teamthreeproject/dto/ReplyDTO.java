package org.lsh.teamthreeproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
    private long replyId;
    private String content;
    private LocalDateTime createdDate;
    private Boolean isDeleted = false;
    private Long boardId;
    private Long userId;
}
