package org.lsh.teamthreeproject.service;

import org.lsh.teamthreeproject.dto.ReplyDTO;
import org.lsh.teamthreeproject.entity.Reply;
import org.lsh.teamthreeproject.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;
    @Autowired
    public ReplyServiceImpl(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    @Override
    public Optional<ReplyDTO> readReply(Long replyId) {
        return replyRepository.findById(replyId).map(this::convertEntityToDTO);
    }

    @Override
    public List<ReplyDTO> readAllReplies() {
        return replyRepository.findAll().stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReplyDTO> readRepliesByUserId(Long userId) {
        return replyRepository.findByUserUserId(userId).stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    private ReplyDTO convertEntityToDTO(Reply reply) {
        return ReplyDTO.builder()
                .replyId(reply.getReplyId())
                .content(reply.getContent())
                .createdDate(reply.getCreatedDate())
                .isDeleted(reply.getIsDeleted())
                .boardId(reply.getBoard().getBoardId())
                .userId(reply.getUser().getUserId())
                .build();
    }
}
