package org.lsh.teamthreeproject.service;

import org.lsh.teamthreeproject.dto.UserDTO;
import org.lsh.teamthreeproject.entity.User;
import org.lsh.teamthreeproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAdminServiceImpl implements UserAdminService {

    private final UserRepository userRepository;

    @Autowired
    public UserAdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 모든 유저 조회
    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setUserId(user.getUserId());
                    userDTO.setLoginId(user.getLoginId());
                    userDTO.setEmail(user.getEmail());
                    userDTO.setNickname(user.getNickname());
                    userDTO.setRegDate(user.getRegDate());
                    return userDTO;
                })
                .collect(Collectors.toList());
    }


    // 유저 검색 (닉네임으로 검색)
    @Override
    public List<UserDTO> searchUsersByNickname(String nickname) {
        return userRepository.findByNicknameContaining(nickname).stream()
                .map(user -> {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setUserId(user.getUserId());
                    userDTO.setLoginId(user.getLoginId());
                    userDTO.setEmail(user.getEmail());
                    userDTO.setNickname(user.getNickname());
                    userDTO.setRegDate(user.getRegDate());
                    return userDTO;
                })
                .collect(Collectors.toList());
    }


    // 유저 삭제
    @Override
    public void deleteUser(long userId) {
        userRepository.findById(userId).ifPresent(user -> userRepository.delete(user));
    }

}
