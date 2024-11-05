package org.lsh.teamthreeproject.service;

import org.hibernate.Hibernate;
import org.lsh.teamthreeproject.dto.BookmarkDTO;
import org.lsh.teamthreeproject.entity.BookMark;
import org.lsh.teamthreeproject.entity.BookMarkId;
import org.lsh.teamthreeproject.repository.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookMarkServiceImpl implements BookMarkService {
    private final BookmarkRepository bookmarkRepository;
    @Autowired
    public BookMarkServiceImpl(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    @Override
    public Optional<BookmarkDTO> readBookMark(BookMarkId bookMarkId) {
        Optional<BookMark> bookmark = bookmarkRepository.findById(bookMarkId);
        bookmark.ifPresent(b -> {
            Hibernate.initialize(b.getBoard());
            Hibernate.initialize(b.getUser());
        });
        return bookmark.map(this::convertEntityToDTO);
    }

    @Override
    public List<BookmarkDTO> readUserBookmarks(Long userId) {
        return bookmarkRepository.findByUserUserId(userId).stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBookMark(BookMarkId bookmarkId) {
        bookmarkRepository.deleteById(bookmarkId);
    }

    private BookmarkDTO convertEntityToDTO(BookMark bookmark) {
        return BookmarkDTO.builder()
                .boardId(bookmark.getBoard().getBoardId())
                .userId(bookmark.getUser().getUserId())
                .bookmarkedDate(bookmark.getBookmarkedDate())
                .userNickname(bookmark.getUser().getNickname())
                .title(bookmark.getBoard().getTitle())
                .content(bookmark.getBoard().getContent())
                .purchaseLink(bookmark.getBoard().getPurchaseLink())
                .regDate(bookmark.getBoard().getRegDate())
                .build();
    }
}
