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
        Optional<User> userOptional = userRepository.findByLoginId(userDTO.getLoginId());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(userDTO.getPassword())) {
                // UserDTO에 추가 정보를 설정하여 반환
                UserDTO loggedInUserDTO = new UserDTO();
                loggedInUserDTO.setLoginId(user.getLoginId());
                loggedInUserDTO.setNickname(user.getNickname());
                loggedInUserDTO.setEmail(user.getEmail());
                loggedInUserDTO.setIntroduce(user.getIntroduce());
                loggedInUserDTO.setProfileImagePath(user.getProfileImage()); // 이미지 경로 설정

                // 세션에 사용자 정보 저장
                session.setAttribute("loginUser", loggedInUserDTO);
                session.setAttribute("nickname", loggedInUserDTO);
                return loggedInUserDTO;
            }
        }
        return null; // 로그인 실패 시 null 반환
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

    @Override
    public String saveProfileImage(MultipartFile profileImage) throws IOException {
        if (!profileImage.isEmpty()) {
            String uploadDir = "D:\\upload";
            String fileName = profileImage.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = profileImage.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                return filePath.toString(); // 이미지 경로 반환
            } catch (IOException e) {
                throw new IOException("이미지 파일 저장 중 에러 발생: " + fileName, e);
            }
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

}
