package org.lsh.teamthreeproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardImageDTO {
    private long imageId; // 이미지 고유 ID
    private long boardId; // 연관된 게시물 ID
    private String imageUrl; // 이미지 URL

    private List<MultipartFile> files; // 실제 파일 데이터
    private List<BoardImageDTO> images; // 파일 경로 정보를 담을 리스트
}
