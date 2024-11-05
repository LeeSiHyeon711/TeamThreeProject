package org.lsh.teamthreeproject.service;

import org.lsh.teamthreeproject.dto.BoardDTO;

import java.util.List;
import java.util.Optional;

public interface BoardService {
    public Optional<BoardDTO> readBoard(Long id);

//    public List<BoardDTO> readAllBoards();

    void deleteBoard(Long boardId);

    List<BoardDTO> readBoardsByUserId(Long userId);

    void increaseVisitCount(Long boardId);
}
