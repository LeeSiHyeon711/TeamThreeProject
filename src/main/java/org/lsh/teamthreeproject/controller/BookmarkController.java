package org.lsh.teamthreeproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.lsh.teamthreeproject.dto.BookmarkDTO;
import org.lsh.teamthreeproject.entity.BookMarkId;
import org.lsh.teamthreeproject.service.BookMarkService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log4j2
public class BookmarkController {
    private final BookMarkService bookMarkService;
    // 북마크 게시물 하나
    @GetMapping("/bookmark/{userId}/{boardId}")
    public String readOneBookmark(@PathVariable("userId") Long userId,
                                  @PathVariable("boardId") Long boardId,
                                  Model model) {
        BookMarkId bookmarkId = new BookMarkId(userId, boardId);
        Optional<BookmarkDTO> bookmark = bookMarkService.readBookMark(bookmarkId);

        if (bookmark.isPresent()) {
            model.addAttribute("bookmark", bookmark.get());
        } else {
            // board가 없거나 북마크가 없는 경우 처리
            model.addAttribute("errorMessage", "해당 게시물을 찾을 수 없습니다.");
            log.error("Bookmark or Board not found for userId: {}, boardId: {}", userId, boardId);
        }
        return "/my/myBookmark";
    }


    // 사용자의 게시물 목록
    @GetMapping("/myBookmarkList/{userId}")
    public String readBookmarkList(@PathVariable("userId") Long userId, Model model) {
        List<BookmarkDTO> bookMarks = bookMarkService.readUserBookmarks(userId);
        model.addAttribute("bookmarks", bookMarks);
        return "/my/myBookmarkList";
    }

    @PostMapping("/bookmarkDelete/{userId}/{boardId}")
    @Transactional
    public String deleteBookmark(@PathVariable("userId") Long userId,
                                 @PathVariable("boardId") Long boardId) {
        BookMarkId bookmarkId = new BookMarkId(boardId, userId);
        bookMarkService.deleteBookMark(bookmarkId);
        return "redirect:/mypage";
    }
}
