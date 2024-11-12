package org.lsh.teamthreeproject.controller;

import org.lsh.teamthreeproject.dto.BookmarkDTO;
import org.lsh.teamthreeproject.service.BookMarkService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkRestController {
    private final BookMarkService bookMarkService;

    public BookmarkRestController(BookMarkService bookMarkService) {
        this.bookMarkService = bookMarkService;
    }

    @GetMapping("/paged")
    public Page<BookmarkDTO> getPagedBookmarkList(
            @RequestParam("userId") Long userId,
            @RequestParam(defaultValue = "0") int page
    ) {
        int size = 4; // 한 페이지에 4개씩 표시
        Pageable pageable = PageRequest.of(page, size);
        return bookMarkService.getBookmarks(userId, pageable);
    }

}
