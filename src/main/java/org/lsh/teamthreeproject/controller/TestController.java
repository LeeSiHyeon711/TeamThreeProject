package org.lsh.teamthreeproject.controller;

import org.lsh.teamthreeproject.dto.BoardDTO;
import org.lsh.teamthreeproject.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TestController {
    private final BoardService boardService;

    public TestController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/basic")
    public String basic(Model model) {
        List<BoardDTO> popularBoards = boardService.getPopularBoards();
        model.addAttribute("popularBoards", popularBoards);
        return "layout/basic";
    }
}
