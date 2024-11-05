package org.lsh.teamthreeproject.service;

import org.lsh.teamthreeproject.dto.ChatRoomDTO;
import org.lsh.teamthreeproject.entity.ChatRoom;
import org.lsh.teamthreeproject.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Override
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.builder()
                .name(name)
                .build();
        return chatRoomRepository.save(chatRoom); // 엔티티 반환
    }

    @Override
    public List<ChatRoom> findAllRooms() {
        return chatRoomRepository.findAll();
    }

    @Override
    public ChatRoom findById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다: " + chatRoomId));
    }

    @Override
    public void deleteChatRoom(Long chatRoomId) {
        chatRoomRepository.deleteById(chatRoomId);
    }

    @Override
    public List<ChatRoom> findAll() {
        // 채팅방 목록을 데이터베이스에서 가져오기
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();

        // 디버깅용으로 가져온 목록을 출력
        chatRooms.forEach(room -> {
            System.out.println("채팅방 ID: " + room.getChatRoomId() + ", 이름: " + room.getName());
        });

        return chatRooms;
    }
}
