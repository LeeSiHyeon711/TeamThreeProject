package org.lsh.teamthreeproject.repository;

import org.lsh.teamthreeproject.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // 채팅방 목록 띄울 때 모든 채팅방 id를 불러옴
    Optional<ChatRoom> findByChatRoomId(Long chatRoomId);

}
