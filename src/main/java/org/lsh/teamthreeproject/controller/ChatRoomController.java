package org.lsh.teamthreeproject.controller;

import jakarta.servlet.http.HttpSession;
import org.lsh.teamthreeproject.dto.ChatRoomDTO;
import org.lsh.teamthreeproject.entity.ChatRoom;
import org.lsh.teamthreeproject.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chatroom")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @Autowired
    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    // 채팅방 생성
    @PostMapping("/create")
    public ChatRoomDTO createChatRoom(@RequestBody ChatRoomDTO chatRoomDTO) {
        // ChatRoom 엔티티를 생성하는 메서드에서 반환된 ChatRoom 엔티티를 ChatRoomDTO로 변환
        ChatRoom chatRoom = chatRoomService.createChatRoom(chatRoomDTO.getName());
        return ChatRoomDTO.fromEntity(chatRoom);
    }

    // 모든 채팅방 조회
    @GetMapping("/all")
    public List<ChatRoomDTO> getAllChatRooms() {
        // 엔티티를 DTO로 변환하여 반환
        List<ChatRoom> chatRooms = chatRoomService.findAllRooms();
        return chatRooms.stream()
                .map(ChatRoomDTO::fromEntity) // 엔티티를 DTO로 변환
                .collect(Collectors.toList());
    }

    // 특정 채팅방 조회
    @GetMapping("/{id}")
    public ChatRoomDTO getChatRoomById(@PathVariable Long id) {
        // 특정 채팅방을 조회하고 DTO로 변환하여 반환
        ChatRoom chatRoom = chatRoomService.findById(id);
        return ChatRoomDTO.fromEntity(chatRoom);
    }

    // 채팅방 삭제
    @DeleteMapping("/delete/{chatRoomId}")
    public ResponseEntity<?> deleteChatRoom(@PathVariable Long chatRoomId) {
        chatRoomService.deleteChatRoom(chatRoomId);
        return ResponseEntity.ok().build();
    }
}
