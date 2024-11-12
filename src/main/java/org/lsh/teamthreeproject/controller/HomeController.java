package org.lsh.teamthreeproject.controller;

import jakarta.servlet.http.HttpSession;
import org.lsh.teamthreeproject.dto.BoardDTO;
import org.lsh.teamthreeproject.dto.BookmarkDTO;
import org.lsh.teamthreeproject.dto.ReplyDTO;
import org.lsh.teamthreeproject.dto.UserDTO;
import org.lsh.teamthreeproject.entity.ChatRoom;
import org.lsh.teamthreeproject.service.BoardService;
import org.lsh.teamthreeproject.service.BookMarkService;
import org.lsh.teamthreeproject.service.ChatRoomService;
import org.lsh.teamthreeproject.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {
    public final ChatRoomService chatRoomService;
    public final BoardService boardService;
    public final ReplyService replyService;
    public final BookMarkService bookmarkService;
    @Autowired
    public HomeController(ChatRoomService chatRoomService, BoardService boardService, ReplyService replyService, BookMarkService bookmarkService) {
        this.chatRoomService = chatRoomService;
        this.boardService = boardService;
        this.replyService = replyService;
        this.bookmarkService = bookmarkService;
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
    public String showMyPage(HttpSession session, Model model) {
        // 세션에서 userDTO 객체 가져오기
        UserDTO user = (UserDTO) session.getAttribute("user");

        // 디버그 로그 추가
        System.out.println("User retrieved from session: " + (user != null ? user.getUserId() : "No user in session"));

        if (user == null) {
            return "redirect:/user/login";
        }

        // 사용자의 게시물 목록을 가져오기 위해 BoardService 사용
        List<BoardDTO> boards = boardService.findBoardsByUserId(user.getUserId());
        List<ReplyDTO> replies = replyService.readRepliesByUserId(user.getUserId());
        List<BookmarkDTO> bookmarks = bookmarkService.readUserBookmarks(user.getUserId());

        // 모델에 사용자 정보와 게시물 목록 추가
        model.addAttribute("user", user);
        model.addAttribute("boards", boards);
        model.addAttribute("replies", replies);
        model.addAttribute("bookmarks", bookmarks);

        return "/my/mypage";
    }

}
