package org.lsh.teamthreeproject.controller;

import lombok.RequiredArgsConstructor;
import org.lsh.teamthreeproject.dto.BoardDTO;
import org.lsh.teamthreeproject.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardRestController {

    private final BoardService boardService;

    @GetMapping("/paged")
    public Page<BoardDTO> getPagedBoardList(
            @RequestParam("userId") Long userId,
            @RequestParam(defaultValue = "0") int page
    ) {
        int size = 4; // 한 페이지에 4개씩 표시
        Pageable pageable = PageRequest.of(page, size);
        return boardService.getBoards(userId, pageable);
    }
}
