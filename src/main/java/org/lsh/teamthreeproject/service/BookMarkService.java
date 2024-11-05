package org.lsh.teamthreeproject.service;

import org.lsh.teamthreeproject.dto.BookmarkDTO;
import org.lsh.teamthreeproject.entity.BookMarkId;

import java.util.List;
import java.util.Optional;

public interface BookMarkService {
    Optional<BookmarkDTO> readBookMark(BookMarkId bookmarkId);
    List<BookmarkDTO> readUserBookmarks(Long userId);
    void deleteBookMark(BookMarkId bookmarkId);
}
