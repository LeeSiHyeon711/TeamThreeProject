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

// UserDTO 생성
        UserDTO loggedInUserDTO = convertToDTO(user);

// 세션에 사용자 정보 저장
        session.setAttribute("loginUser", loggedInUserDTO);
        return loggedInUserDTO;
    }

    @Override
    public Optional<User> findUserIdByNickname(String nickname) {
        Optional<User> userOptional = userRepository.findByNickname(nickname);
        userOptional.ifPresent(user -> {
            System.out.println("찾은 유저 ID: " + user.getUserId());
        });
        return userOptional;
    }

    public String saveProfileImage(MultipartFile profileImage) throws IOException {
        if (profileImage != null && !profileImage.isEmpty()) {
            String uploadDir = "c:\\upload";
            String fileName = profileImage.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);

            try (InputStream inputStream = profileImage.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            return fileName; // 파일명만 반환
        }
        return null;
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
        } else {
            // 사용자가 이미지를 업로드하지 않았을 경우 기본 이미지 경로 설정
            user.setProfileImage("/images/noImage.jpg");
        }

        // User 엔티티 저장
        userRepository.save(user);
    }


    @Override
    public UserDTO updateUser(Long userId, UserDTO updatedUserDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setNickname(updatedUserDTO.getNickname());
        user.setIntroduce(updatedUserDTO.getIntroduce());
        user.setEmail(updatedUserDTO.getEmail());

        MultipartFile profileImage = updatedUserDTO.getProfileImage();
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String imagePath = saveProfileImage(profileImage);
                user.setProfileImage(imagePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save profile image", e);
            }
        }

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
        String profileImagePath = user.getProfileImage();

// 경로 설정 수정
        if (profileImagePath != null && !profileImagePath.isEmpty()) {
// "/upload/{파일명}" 형태로 경로 설정
            profileImagePath = "/upload/" + Paths.get(profileImagePath).getFileName().toString();
        } else {
            profileImagePath = "/images/defaultImage.png"; // 기본 이미지 경로
        }

        return UserDTO.builder()
                .userId(user.getUserId())
                .loginId(user.getLoginId())
                .email(user.getEmail())
                .regDate(user.getRegDate())
                .profileImagePath(profileImagePath)  // 이미지 경로 설정
                .nickname(user.getNickname())
                .introduce(user.getIntroduce())
                .build();
    }


    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setLoginId(userDTO.getLoginId());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setNickname(userDTO.getNickname());
        user.setIntroduce(userDTO.getIntroduce());
        user.setProfileImage(userDTO.getProfileImagePath()); // 파일명 저장

        return user;
    }
    @Override
    public void setDefaultProfileImage(Long userId) {
// 사용자 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        // 프로필 이미지 경로를 기본 이미지로 변경
        user.setProfileImage("/upload/noImage.jpg");
        // 기본 이미지 파일 경로와 업로드 경로 설정
        Path defaultImagePath = Paths.get("src/main/resources/static/images/noImage.jpg"); // 기본 이미지 위치
        Path uploadDir = Paths.get("c:/upload");
        Path uploadImagePath = uploadDir.resolve("noImage.jpg"); // 업로드 폴더에 저장될 경로

        try {
            // 업로드 폴더가 없으면 생성
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // DB에 저장된 사용자 정보 업데이트
            userRepository.save(user);
            // 기본 이미지를 upload 폴더로 복사
            Files.copy(defaultImagePath, uploadImagePath, StandardCopyOption.REPLACE_EXISTING);

            // 사용자 프로필 이미지를 기본 이미지 경로로 업데이트
            user.setProfileImage("/upload/noImage.jpg");

            // DB에 저장된 사용자 정보 업데이트
            userRepository.save(user);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("기본 이미지 복사 중 에러 발생", e);
        }
    }


}