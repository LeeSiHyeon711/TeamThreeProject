package org.lsh.teamthreeproject.repository;

import org.lsh.teamthreeproject.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 특정 채팅방의 모든 메시지 조회
    List<ChatMessage> findByChatRoom_ChatRoomId(Long chatRoomId);
    // 관리자의 채팅방 삭제 시 채팅방 id로 해당 채팅방의 채팅 메세지 삭제
    void deleteByChatRoom_ChatRoomId(Long chatRoomId);
}
