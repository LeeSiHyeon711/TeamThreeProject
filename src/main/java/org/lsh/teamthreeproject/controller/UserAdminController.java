package org.lsh.teamthreeproject.controller;

import org.lsh.teamthreeproject.dto.UserDTO;
import org.lsh.teamthreeproject.service.UserAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserAdminController {

    private final UserAdminService userAdminService;

    @Autowired
    public UserAdminController(UserAdminService userAdminService) {
        this.userAdminService = userAdminService;
    }

    // 유저 검색 API
    @GetMapping("/api/search")
    @ResponseBody
    public List<UserDTO> searchUsers(@RequestParam String query) {
        return userAdminService.searchUsersByNickname(query);  // 검색 기능 처리
    }

    // 유저 삭제
    @DeleteMapping("/{userId}")
    @ResponseBody
    public void deleteUser(@PathVariable long userId) {
        userAdminService.deleteUser(userId);  // 유저 삭제 기능 처리
    }
}
