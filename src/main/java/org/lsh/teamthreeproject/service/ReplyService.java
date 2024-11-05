package org.lsh.teamthreeproject.service;

import org.lsh.teamthreeproject.dto.ReplyDTO;

import java.util.List;
import java.util.Optional;

public interface ReplyService {
    Optional<ReplyDTO> readReply(Long id);
    public List<ReplyDTO> readAllReplies();
    public List<ReplyDTO> readRepliesByUserId(Long userId);
}
