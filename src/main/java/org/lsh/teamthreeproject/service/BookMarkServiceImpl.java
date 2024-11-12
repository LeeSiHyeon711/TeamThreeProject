package org.lsh.teamthreeproject.service;

import lombok.extern.log4j.Log4j2;
import org.hibernate.Hibernate;
import org.lsh.teamthreeproject.dto.BookmarkDTO;
import org.lsh.teamthreeproject.entity.BookMark;
import org.lsh.teamthreeproject.entity.BookMarkId;
import org.lsh.teamthreeproject.repository.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
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
    @Transactional
    public void deleteBookMark(BookMarkId bookmarkId) {
        log.info("!!!!!!!!!!!!!!!!!!!!!!!!!Deleting bookmark with ID: " + bookmarkId);

        bookmarkRepository.deleteById(bookmarkId);
    }

    @Override
    public Page<BookmarkDTO> getBookmarks(Long userId, Pageable pageable) {
        // Bookmark 엔티티를 가져와서 DTO로 변환
        Page<BookMark> bookmarkPage = bookmarkRepository.findByUserUserId(userId, pageable);
        if (bookmarkPage == null) {
            System.out.println("ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇbookmarkPage is null right after retrieval in the controller");
        }
        return bookmarkPage.map(this::convertEntityToDTO); // 엔티티 -> DTO 변환
    }

    private BookmarkDTO convertEntityToDTO(BookMark bookmark) {

        // 필드 값 확인 출력
        System.out.println("Board ID: " + (bookmark.getBoard() != null ? bookmark.getBoard().getBoardId() : "null"));
        System.out.println("User ID: " + (bookmark.getUser() != null ? bookmark.getUser().getUserId() : "null"));
        System.out.println("Title: " + (bookmark.getBoard() != null ? bookmark.getBoard().getTitle() : "null"));
        System.out.println("Content: " + (bookmark.getBoard() != null ? bookmark.getBoard().getContent() : "null"));
        System.out.println("Nickname: " + (bookmark.getUser() != null ? bookmark.getUser().getNickname() : "null"));

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
