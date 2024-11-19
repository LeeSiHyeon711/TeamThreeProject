package org.lsh.teamthreeproject.service;

import lombok.RequiredArgsConstructor;
import org.lsh.teamthreeproject.dto.ChatMessageDTO;
import org.lsh.teamthreeproject.entity.ChatMessage;
import org.lsh.teamthreeproject.repository.ChatMessageRepository;
import org.lsh.teamthreeproject.repository.ChatRoomRepository;
import org.lsh.teamthreeproject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Override
    public List<ChatMessageDTO> findMessagesByChatRoomId(Long chatRoomId) {
        List<ChatMessage> messages = chatMessageRepository.findByChatRoom_ChatRoomId(chatRoomId);
        return messages.stream()
                .map(ChatMessageDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void saveMessage(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage); // 엔티티 저장
    }


    @Override
    public void deleteMessage(Long messageId) {
        chatMessageRepository.deleteById(messageId);
    }

    @Override
    public void deleteMessagesByChatRoomId(Long chatRoomId) {
        chatMessageRepository.deleteByChatRoom_ChatRoomId(chatRoomId);
    }
}
