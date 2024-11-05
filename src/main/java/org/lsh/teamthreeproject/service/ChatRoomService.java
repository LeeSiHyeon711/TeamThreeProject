package org.lsh.teamthreeproject.service;

import org.lsh.teamthreeproject.dto.ChatRoomDTO;
import org.lsh.teamthreeproject.entity.ChatRoom;

import java.util.List;

public interface ChatRoomService {
    ChatRoom createChatRoom(String name);
    List<ChatRoom> findAllRooms();
    ChatRoom findById(Long chatRoomId);  // 이 부분 추가
    void deleteChatRoom(Long chatRoomId);
    List<ChatRoom> findAll();
}
