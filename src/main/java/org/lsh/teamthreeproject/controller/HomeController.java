package org.lsh.teamthreeproject.controller;

import jakarta.servlet.http.HttpSession;
import org.lsh.teamthreeproject.dto.UserDTO;
import org.lsh.teamthreeproject.entity.ChatRoom;
import org.lsh.teamthreeproject.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    public final ChatRoomService chatRoomService;
    @Autowired
    public HomeController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    // 채팅방 리스트 페이지 입장
    @GetMapping("/chatList")
    public String showChatList(HttpSession session, Model model) {
        // 세션에서 userDTO 객체 가져오기
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login"; // 로그인 안 되어 있으면 로그인 페이지로 리다이렉트
        }

        String nickname = user.getNickname(); // 세션의 userDTO 에서 nickname 추출
        model.addAttribute("nickname", nickname); // 모델에 닉네임 추가

        // 채팅방 리스트를 서비스로부터 가져와 모델에 추가
        List<ChatRoom> chatRooms = chatRoomService.findAll();
        System.out.println("조회된 채팅방 목록: " + chatRooms); // 가져온 채팅방 목록 출력
        model.addAttribute("chatRooms", chatRooms);

        return "chat/chatRooms"; // 채팅방 페이지로 이동
    }

    // 마이 페이지 입장
    @GetMapping("/mypage")
    public String showMyPage(HttpSession session ,Model model) {
        // 세션에서 userDTO 객체 가져오기
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }
        Long userId = user.getUserId();
        model.addAttribute("userId", userId);

        return "/my/mypage";
    }

    // 로그아웃 처리 (GET)
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화 (모든 세션 데이터 삭제)
        return "redirect:/user/login"; // 로그아웃 후 로그인 페이지로 리다이렉트
    }
}
