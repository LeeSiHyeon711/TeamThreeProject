package org.lsh.teamthreeproject.controller;

import jakarta.servlet.http.HttpSession;
import org.lsh.teamthreeproject.dto.ChatMessageDTO;
import org.lsh.teamthreeproject.entity.ChatMessage;
import org.lsh.teamthreeproject.entity.ChatRoom;
import org.lsh.teamthreeproject.entity.User;
import org.lsh.teamthreeproject.repository.UserRepository;
import org.lsh.teamthreeproject.service.ChatMessageService;
import org.lsh.teamthreeproject.service.ChatRoomService;
import org.lsh.teamthreeproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class ChatController {

    public final UserRepository userRepository;
    public final ChatRoomService chatRoomService;
    public final ChatMessageService chatMessageService;
    public final UserService userService;

    @Autowired
    public ChatController(UserRepository userRepository, ChatRoomService chatRoomService, ChatMessageService chatMessageService, UserService userService) {
        this.userRepository = userRepository;
        this.chatRoomService = chatRoomService;
        this.chatMessageService = chatMessageService;
        this.userService = userService;
    }

    //채팅방 입장
    @GetMapping("/chat")
    public String chatPage(@RequestParam("roomId") Long roomId, @RequestParam("nickname") String nickname, Model model) {
        User user = userService.findUserIdByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("해당 닉네임의 유저가 존재하지 않습니다: " + nickname));
        model.addAttribute("roomId", roomId);
        model.addAttribute("nickname", nickname);
        model.addAttribute("profileImagePath", user.getProfileImagePath()); // 프로필 이미지 경로 추가
        return "chat/simpleChatRoom";
    }

    // 채팅 메시지 전송 API
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessageDTO sendMessage(ChatMessageDTO message) {
        // 채팅방 조회
        ChatRoom chatRoom = chatRoomService.findById(message.getChatRoomId());

        // 유저 조회
        User sender = userService.findUserIdByNickname(message.getSender())
                .orElseThrow(() -> new IllegalArgumentException("해당 닉네임의 유저가 존재하지 않습니다: " + message.getSender()));

        // 프로필 이미지 경로 설정
        message.setProfileImagePath(sender.getProfileImagePath());

        // 채팅 메시지 엔티티로 변환 후 저장
        ChatMessage chatMessage = ChatMessageDTO.toEntity(message, chatRoom, sender);
        chatMessageService.saveMessage(chatMessage);

        // 채팅 메시지 엔티티를 다시 DTO로 변환하여 반환
        return ChatMessageDTO.fromEntity(chatMessage);
    }

    //채팅방 입장 API
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessageDTO addUser(ChatMessageDTO message) {
        // 유저 조회
        User sender = userService.findUserIdByNickname(message.getSender())
                .orElseThrow(() -> new IllegalArgumentException("해당 닉네임의 유저가 존재하지 않습니다: " + message.getSender()));

        // 한국 시간으로 변환
        ZonedDateTime zonedDateTime = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul"));
        message.setSendDate(zonedDateTime.toLocalDateTime()); // DTO의 sendDate 설정
        message.setContent(message.getSender() + "님이 채팅방에 입장했습니다!");
        message.setProfileImagePath(sender.getProfileImagePath()); // 프로필 이미지 경로 설정
        return message;
    }

    //채팅방 퇴장 API
    @MessageMapping("/chat.removeUser")
    @SendTo("/topic/public")
    public ChatMessageDTO removeUser(ChatMessageDTO message) {
        // 유저 조회
        User sender = userService.findUserIdByNickname(message.getSender())
                .orElseThrow(() -> new IllegalArgumentException("해당 닉네임의 유저가 존재하지 않습니다: " + message.getSender()));

        message.setContent(message.getSender() + "님이 채팅방에서 퇴장했습니다.");
        message.setProfileImagePath(sender.getProfileImagePath()); // 프로필 이미지 경로 설정
        return message;
    }

}
