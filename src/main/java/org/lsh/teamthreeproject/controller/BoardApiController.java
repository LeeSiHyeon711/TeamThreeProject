package org.lsh.teamthreeproject.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.lsh.teamthreeproject.dto.BoardDTO;
import org.lsh.teamthreeproject.dto.UserDTO;
import org.lsh.teamthreeproject.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    @PostMapping
    public Page<BoardDTO> getBoards(@RequestParam int page, @RequestParam int size, HttpSession session) { // 추후 확장 시 long 으로 변환 가능
        Long userId = ((UserDTO) session.getAttribute("user")).getUserId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("regDate").descending());
        return boardService.findAllByOrderByRegDateDesc(pageable, userId);
    }

}
