package org.lsh.teamthreeproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lsh.teamthreeproject.entity.ChatRoom;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDTO {
    private long chatRoomId; //채팅방 ID
    private String name; //채팅방 이름
    private LocalDateTime createdDate; //채팅방 생성 시간

    // 엔티티를 DTO로 변환하는 정적 메소드
    public static ChatRoomDTO fromEntity(ChatRoom chatRoom) {
        return ChatRoomDTO.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .name(chatRoom.getName())
                .createdDate(chatRoom.getCreatedDate())
                .build();
    }

    // (선택) DTO를 엔티티로 변환하는 정적 메소드
    public static ChatRoom toEntity(ChatRoomDTO chatRoomDTO) {
        return ChatRoom.builder()
                .chatRoomId(chatRoomDTO.getChatRoomId())
                .name(chatRoomDTO.getName())
                .createdDate(chatRoomDTO.getCreatedDate())
                .build();
    }
}

