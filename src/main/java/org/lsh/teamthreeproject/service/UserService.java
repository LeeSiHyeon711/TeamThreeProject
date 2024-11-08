package org.lsh.teamthreeproject.service;

import jakarta.servlet.http.HttpSession;
import org.lsh.teamthreeproject.dto.UserDTO;
import org.lsh.teamthreeproject.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface UserService {
    UserDTO login(UserDTO userDTO, HttpSession session);
    Optional<User> findUserIdByNickname(@Param("nickname") String nickname);
    String saveProfileImage(MultipartFile profileImage) throws IOException;
    void register(UserDTO userDTO);
    UserDTO updateUser(Long userId, UserDTO updatedUserDTO);
    void deleteUser(Long userId);
    public Optional<UserDTO> readUser(Long userId);
    void setDefaultProfileImage(Long userId);
}
