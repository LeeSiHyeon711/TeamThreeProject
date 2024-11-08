package org.lsh.teamthreeproject.controller;

import org.lsh.teamthreeproject.dto.BoardDTO;
import org.lsh.teamthreeproject.service.BoardService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class PopularBoardsController {
    private final BoardService boardService;

    public PopularBoardsController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 모든 페이지에 인기 게시물 속성 추가
    @ModelAttribute("popularBoards")
    public List<BoardDTO> getPopularBoards() {
        return boardService.getPopularBoards();
    }
}
