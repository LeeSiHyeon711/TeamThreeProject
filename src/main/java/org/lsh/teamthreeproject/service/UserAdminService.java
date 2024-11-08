package org.lsh.teamthreeproject.service;

import org.lsh.teamthreeproject.dto.BoardDTO;
import org.lsh.teamthreeproject.dto.UserDTO;
import org.lsh.teamthreeproject.entity.Board;
import org.lsh.teamthreeproject.entity.User;
import org.lsh.teamthreeproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public interface UserAdminService {

    // 모든 유저 조회
    List<UserDTO> getAllUsers();

    // 유저 검색 (닉네임으로 검색)
    List<UserDTO> searchUsersByNickname(String nickname);

    // 유저 삭제
    void deleteUser(long userId);


}
