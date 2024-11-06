package org.lsh.teamthreeproject.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private long userId; //유저 고유 ID
    private String loginId; // 사용자 로그인 ID
    private String password; // 사용자 비밀번호
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate; //사용자 가입일
    private String email; // 사용자 이메일
    private String nickname; // 사용자 닉네임
    private String introduce; // 사용자 소개
    private MultipartFile profileImage; // 업로드한 이미지 파일을 받기 위한 필드
    private String profileImagePath;    // 실제로 저장된 이미지의 경로

}
