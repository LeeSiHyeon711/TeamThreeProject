package org.lsh.teamthreeproject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.lsh.teamthreeproject.dto.UserDTO;
import org.lsh.teamthreeproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
            return "redirect:/basic";
        } else {
            // 로그인 실패 시 다시 로그인 페이지로, 에러 메시지 추가
            model.addAttribute("errorMessage", "로그인 실패: 아이디 또는 비밀번호가 잘못되었습니다.");
            return "user/login";
        }
    }
}
