package org.lsh.teamthreeproject.service;

import org.lsh.teamthreeproject.dto.ChatMessageDTO;
import org.lsh.teamthreeproject.entity.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    // 특정 채팅방의 모든 메시지를 조회
    List<ChatMessageDTO> findMessagesByChatRoomId(Long chatRoomId);

    // 새로운 메시지를 저장
    void saveMessage(ChatMessage chatMessage);

    // 특정 메시지를 삭제
    void deleteMessage(Long messageId);

    // 특정 채팅방의 모든 메시지를 삭제 (채팅방 삭제 시 사용)
    void deleteMessagesByChatRoomId(Long chatRoomId);
}
