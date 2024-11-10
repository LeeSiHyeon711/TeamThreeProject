package org.lsh.teamthreeproject.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    private long boardId;
    private String title;
    private String content;
    private String purchaseLink;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate;
    private long visitCount = 0;
    private long userId;
    private String userLoginId;
    private String nickname;
    // 좋아요, 북마크 상태 저장
    private Boolean isLiked;
    private Boolean isBookmarked;
    private List<String> fileNames = new ArrayList<>(); // 파일 이름 저장용
    private List<MultipartFile> files = new ArrayList<>(); // 업로드된 파일 저장용
    private List<BoardImageDTO> images = new ArrayList<>(); // 이미지 정보 담을 리스트
}
