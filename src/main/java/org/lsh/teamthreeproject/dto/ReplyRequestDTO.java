package org.lsh.teamthreeproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyRequestDTO {
    private Long boardId;
    private int page;
    private int size;
}
