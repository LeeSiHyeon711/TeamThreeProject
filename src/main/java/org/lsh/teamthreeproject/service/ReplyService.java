package org.lsh.teamthreeproject.service;

import org.lsh.teamthreeproject.dto.BoardDTO;
import org.lsh.teamthreeproject.dto.ReplyDTO;
import org.lsh.teamthreeproject.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReplyService {
    Optional<ReplyDTO> readReply(Long id);
    List<ReplyDTO> readAllReplies();
    List<ReplyDTO> readRepliesByUserId(Long userId);

    ReplyDTO updateReply(ReplyDTO replyDTO, String content);
    List<ReplyDTO> readRepliesByBoardId(Long boardId);
    ReplyDTO createReply(BoardDTO board, UserDTO user, String content);
    void saveReply(ReplyDTO replyDTO);
    boolean deleteReply(Long replyId);
    List<ReplyDTO> getRepliesByBoardId(Long boardId, int page, int size);
    Page<ReplyDTO> findRepliesByBoardId(Long boardId, Pageable pageable);
}
