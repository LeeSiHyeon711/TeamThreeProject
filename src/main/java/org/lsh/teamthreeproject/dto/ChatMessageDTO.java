package org.lsh.teamthreeproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lsh.teamthreeproject.entity.ChatMessage;
import org.lsh.teamthreeproject.entity.ChatRoom;
import org.lsh.teamthreeproject.entity.User;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
    private long messageId;
    private long chatRoomId; // ChatRoom ID를 나타냄
    private String sender; // User 엔티티의 nickname 사용
    private String content;
    private LocalDateTime sendDate;
    private String profileImagePath; // 프로필 이미지 경로 추가

    // DTO -> Entity 변환 메서드
    public static ChatMessage toEntity(ChatMessageDTO dto, ChatRoom chatRoom, User sender) {
        return ChatMessage.builder()
                .messageId(dto.getMessageId())
                .chatRoom(chatRoom)
                .sender(sender)
                .content(dto.getContent())
                .sentDate(dto.getSendDate())
                .build();
    }

    // Entity -> DTO 변환 메서드
    public static ChatMessageDTO fromEntity(ChatMessage chatMessage) {
        return ChatMessageDTO.builder()
                .messageId(chatMessage.getMessageId())
                .chatRoomId(chatMessage.getChatRoom().getChatRoomId())
                .sender(chatMessage.getSender().getNickname())
                .content(chatMessage.getContent())
                .sendDate(chatMessage.getSentDate())
                .profileImagePath(chatMessage.getSender().getProfileImagePath()) // 프로필 이미지 경로 설정
                .build();
    }
}
