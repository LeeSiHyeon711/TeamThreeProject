package org.lsh.teamthreeproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponseDTO {
    private UserDTO user;
    private Page<BoardDTO> boards;
}
