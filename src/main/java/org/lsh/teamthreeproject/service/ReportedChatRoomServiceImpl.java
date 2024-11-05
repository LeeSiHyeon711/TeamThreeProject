package org.lsh.teamthreeproject.service;

import org.lsh.teamthreeproject.dto.ReportedChatRoomDTO;
import org.lsh.teamthreeproject.entity.ChatRoom;
import org.lsh.teamthreeproject.entity.ReportedChatRoom;
import org.lsh.teamthreeproject.entity.User;
import org.lsh.teamthreeproject.repository.ChatRoomRepository;
import org.lsh.teamthreeproject.repository.ReportedChatRoomRepository;
import org.lsh.teamthreeproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportedChatRoomServiceImpl implements ReportedChatRoomService {
    private final ReportedChatRoomRepository reportedChatRoomRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReportedChatRoomServiceImpl(ReportedChatRoomRepository reportedChatRoomRepository, ChatRoomRepository chatRoomRepository, UserRepository userRepository) {
        this.reportedChatRoomRepository = reportedChatRoomRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveReport(ReportedChatRoomDTO reportedChatRoomDTO) {
        ChatRoom chatRoom = chatRoomRepository.findById(reportedChatRoomDTO.getChatRoomId())
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다."));
        User user = userRepository.findByNickname(reportedChatRoomDTO.getReportedBy())
                .orElseThrow(() -> new IllegalArgumentException("해당 닉네임의 유저가 존재하지 않습니다."));

        ReportedChatRoom report = ReportedChatRoom.builder()
                .chatRoom(chatRoom)
                .reportedBy(user)
                .reason(reportedChatRoomDTO.getReason())
                .build();

        reportedChatRoomRepository.save(report);
    }
}
