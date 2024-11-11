package org.lsh.teamthreeproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.lsh.teamthreeproject.dto.BoardDTO;
import org.lsh.teamthreeproject.dto.ReplyDTO;
import org.lsh.teamthreeproject.dto.ReplyRequestDTO;
import org.lsh.teamthreeproject.dto.UserDTO;
import org.lsh.teamthreeproject.service.BoardService;
import org.lsh.teamthreeproject.service.ReplyService;
import org.lsh.teamthreeproject.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ReplyController {
    private final ReplyService replyService;
    private final BoardService boardService;
    private final UserService userService;

    // 특정 사용자의 댓글
    @GetMapping("/myReplyList/{userId}")
    public String readUserReplies(@PathVariable Long userId, Model model) {
        List<ReplyDTO> replies = replyService.readRepliesByUserId(userId);
        model.addAttribute("replies", replies);
        return "/my/myReplyList";
    }

    // 특정 게시물의 댓글 조회
    @GetMapping("/board/{boardId}")
    @ResponseBody // json 형식으로 반환
    public List<ReplyDTO> getRepliesByBoard(@PathVariable Long boardId, Model model) {
        return replyService.readRepliesByBoardId(boardId);
    }

    // 댓글 생성
    @PostMapping("/replies")
    public ResponseEntity<?> createReply(@RequestBody ReplyDTO replyDTO) {
        // User 존재 여부 먼저 확인
        Optional<UserDTO> userOptional = userService.readUser(replyDTO.getUserId());
        log.info("유저 서비스 readUser 결과 : "+userOptional);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        BoardDTO board = boardService.findById(replyDTO.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        UserDTO user = userOptional.get();
        ReplyDTO createdReply = replyService.createReply(board, user, replyDTO.getContent());
        return ResponseEntity.ok(createdReply);
    }

    // 댓글 조회 기능 추가
    @GetMapping("/replies/list")
    public ResponseEntity<Page<ReplyDTO>> getReplies(
            @RequestParam Long boardId,
            @RequestParam int page,
            @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReplyDTO> replies = replyService.findRepliesByBoardId(boardId, pageable);
        return ResponseEntity.ok(replies);
    }


    // 댓글 삭제
    @DeleteMapping("/replies/{replyId}")
    public ResponseEntity<String> deleteReply(@PathVariable Long replyId) {
        try {
            // 댓글이 존재하는지 확인 후 삭제
            boolean deleted = replyService.deleteReply(replyId);

            if (deleted) {
                return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("댓글을 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            log.error("댓글 삭제 중 오류 발생:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 삭제에 실패했습니다.");
        }
    }
}
