package org.lsh.teamthreeproject.service;

import jakarta.servlet.http.HttpSession;
import org.lsh.teamthreeproject.dto.UserDTO;
import org.lsh.teamthreeproject.entity.User;
import org.lsh.teamthreeproject.repository.UserRepository;
import org.lsh.teamthreeproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO login(UserDTO userDTO, HttpSession session) {
        User user = userRepository.findByLoginId(userDTO.getLoginId()).orElse(null);
        if (user == null || !user.getPassword().equals(userDTO.getPassword())) {
            return null; // 로그인 실패 시 null 반환
        }
        // UserDTO를 빌더 패턴으로 생성
        UserDTO loggedInUserDTO = UserDTO.builder()
                .userId(user.getUserId())
                .loginId(user.getLoginId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .regDate(user.getRegDate())
                .introduce(user.getIntroduce())
                .profileImagePath(user.getProfileImage()) // 이미지 경로 설정
                .build();
        // 세션에 사용자 정보 저장
        session.setAttribute("loginUser", loggedInUserDTO);
        return loggedInUserDTO;
    }

    @Override
    public Optional<User> findUserIdByNickname(String nickname) {
        // 닉네임으로 유저를 UserRepository를 통해 찾기
        Optional<User> userOptional = userRepository.findByNickname(nickname);

        // 찾은 유저 정보를 콘솔에 출력 (디버깅용)
        userOptional.ifPresent(user -> {
            System.out.println("찾은 유저 ID: " + user.getUserId());
        });

        // 결과 반환
        return userOptional;
    }

    public String saveProfileImage(MultipartFile profileImage) throws IOException {
        if (profileImage != null && !profileImage.isEmpty()) {
            // 업로드 디렉토리 설정
            String uploadDir = "D:\\upload";
            String fileName = profileImage.getOriginalFilename(); // 업로드된 파일 이름 얻기
            Path uploadPath = Paths.get(uploadDir);

            // 업로드 경로가 존재하지 않으면 디렉토리 생성
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 파일 저장 경로 설정
            Path filePath = uploadPath.resolve(fileName);

            // 파일 저장
            try (InputStream inputStream = profileImage.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            // 저장된 이미지 경로 반환
            return filePath.toString();
        }
        return null; // 파일이 없을 때
    }

    @Override
    public void register(UserDTO userDTO) {
        // DTO를 엔티티로 변환하는 작업
        User user = new User();
        user.setLoginId(userDTO.getLoginId());
        user.setPassword(userDTO.getPassword()); // 비밀번호를 평문으로 저장
        user.setEmail(userDTO.getEmail());
        user.setNickname(userDTO.getNickname());
        user.setIntroduce(userDTO.getIntroduce());

        // 프로필 이미지 저장 로직 추가
        MultipartFile profileImage = userDTO.getProfileImage();
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                // 프로필 이미지를 서버에 저장하고 경로를 가져옴
                String profileImagePath = saveProfileImage(profileImage);
                user.setProfileImage(profileImagePath); // 엔티티에 이미지 경로 설정
            } catch (IOException e) {
                e.printStackTrace(); // 예외 처리
                throw new RuntimeException("프로필 이미지 저장 중 에러 발생", e);
            }
        }
        // User 엔티티 저장
        userRepository.save(user);
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO updatedUserDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 닉네임, 소개, 이메일 업데이트
        user.setNickname(updatedUserDTO.getNickname());
        user.setIntroduce(updatedUserDTO.getIntroduce());
        user.setEmail(updatedUserDTO.getEmail());

        // 프로필 이미지 업데이트
        MultipartFile profileImage = updatedUserDTO.getProfileImage();
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                // 프로필 이미지를 저장하고 경로를 얻음
                String imagePath = saveProfileImage(profileImage);
                user.setProfileImage(imagePath); // 저장된 파일의 경로를 User 엔티티에 설정
            } catch (IOException e) {
                throw new RuntimeException("Failed to save profile image", e);
            }
        }

        // 변경된 사용자 정보 저장
        userRepository.save(user);

        return convertToDTO(user);
    }


    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Optional<UserDTO> readUser(Long userId) {
        return userRepository.findById(userId).map(this::convertToDTO);
    }

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .loginId(user.getLoginId())
                .email(user.getEmail())
                .regDate(user.getRegDate())
                .profileImagePath(user.getProfileImage())
                .nickname(user.getNickname())
                .introduce(user.getIntroduce())
                .build();
    }

}
