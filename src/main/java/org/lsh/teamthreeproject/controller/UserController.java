package org.lsh.teamthreeproject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.lsh.teamthreeproject.dto.UserDTO;
import org.lsh.teamthreeproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Log4j2
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService; // UserService 의존성 주입

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입 페이지 표시 (GET)
    @GetMapping("/register")
    public String register() {
        return "user/join";
    }

    // 회원가입 엔드포인트 (POST)
    @PostMapping("/register")
    public String register(@ModelAttribute UserDTO userDTO) {
        // 회원가입 DTO를 서비스에 전달하여 회원가입 처리
        userService.register(userDTO);
        return "redirect:/user/login"; // 회원가입 성공 후 로그인 페이지로 리다이렉트
    }

    // 로그인 페이지 요청에 대한 핸들러 (GET)
    @GetMapping("/login")
    public String login() {
        return "user/login"; // "user/login.html" 템플릿 반환
    }

    // 로그인 처리 (POST)
    @PostMapping("/login")
    public String login(@ModelAttribute UserDTO userDTO, HttpSession session, Model model) {
        UserDTO loggedInUser = userService.login(userDTO, session);

        if (loggedInUser != null) {
            // 로그인 성공 시 세션에 유저정보 모두 저장
            session.setAttribute("user", loggedInUser); // userDTO 저장

            // 로그인한 사용자의 정보 콘솔에 출력
            System.out.println("로그인 성공: " + loggedInUser);

            // 기본 페이지로 리다이렉트
            return "redirect:/list";
        } else {
            // 로그인 실패 시 다시 로그인 페이지로, 에러 메시지 추가
            model.addAttribute("loginError", "로그인 실패: 아이디 또는 비밀번호가 잘못되었습니다.");
            return "user/login";
        }
    }

    // 로그아웃 처리 (GET)
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화 (모든 세션 데이터 삭제)
        return "redirect:/user/login"; // 로그아웃 후 로그인 페이지로 리다이렉트
    }

    // 기본 이미지로 변경하는 메서드
    @PostMapping("/profile/{userId}/resetProfileImage")
    @ResponseBody  // AJAX 요청에 응답할 때 사용
    public ResponseEntity<?> resetProfileImage(@PathVariable("userId") Long userId, HttpSession session) {
        try {
            // 기본 이미지 경로로 설정하는 메서드 호출
            userService.setDefaultProfileImage(userId);

            // 세션에 있는 사용자 정보 업데이트
            UserDTO updatedUserDTO = userService.readUser(userId).orElseThrow();
            session.setAttribute("user", updatedUserDTO);

            // 성공 시 응답 반환
            return ResponseEntity.ok().body(Map.of("success", true));
        } catch (Exception e) {
            e.printStackTrace();
            // 실패 시 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false));
        }
    }
}
