package org.lsh.teamthreeproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.lsh.teamthreeproject.dto.UserDTO;
import org.lsh.teamthreeproject.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log4j2
public class MyController {
    private final UserService userService;

    // 테스트
//    @GetMapping("/test")
//    public String testPage() {
//        log.info("aaa");
//        return "/mypage/test";
//    }

    // 프로필 조회
    @GetMapping("/profile/{userId}")
    public String readUserProfile(@PathVariable Long userId, Model model) {
//        User user = userService.readUser(userId).orElse(null);
//        model.addAttribute("user", user);
//        return "/my/profile";
        Optional<UserDTO> userDTO = userService.readUser(userId);
        userDTO.ifPresent(value -> model.addAttribute("user", value));
        return "my/profile";
    }

    // 프로필 수정 폼
    @GetMapping("/profile/{userId}/updateMyProfile")
    public String updateProfileForm(@PathVariable Long userId, Model model) {
        Optional<UserDTO> userDTO = userService.readUser(userId);
        userDTO.ifPresent(value -> model.addAttribute("user", value));
        return "my/updateMyProfile";
    }

    // 프로필 수정 처리
    @PostMapping("/profile/{userId}/updateMyProfile")
    public String updateProfile(@PathVariable Long userId,
                                @ModelAttribute UserDTO updatedUser,
                                Model model) {
        UserDTO userDTO = userService.updateUser(userId, updatedUser);
        model.addAttribute("user", userDTO);
        return "redirect:/profile/" + userId;
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long userId) {
        log.info("delete");
        userService.deleteUser(userId);
        return "redirect:/profile";
    }
}
