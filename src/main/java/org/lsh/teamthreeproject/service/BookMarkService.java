package org.lsh.teamthreeproject.service;

import org.lsh.teamthreeproject.dto.BookmarkDTO;
import org.lsh.teamthreeproject.entity.BookMarkId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookMarkService {
    Optional<BookmarkDTO> readBookMark(BookMarkId bookmarkId);
    List<BookmarkDTO> readUserBookmarks(Long userId);
    void deleteBookMark(BookMarkId bookmarkId);
    Page<BookmarkDTO> getBookmarks(Long userId, Pageable pageable);

}
