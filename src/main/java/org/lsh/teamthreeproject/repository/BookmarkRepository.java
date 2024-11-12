package org.lsh.teamthreeproject.repository;

import org.lsh.teamthreeproject.entity.BookMark;
import org.lsh.teamthreeproject.entity.BookMarkId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<BookMark, BookMarkId> {
    Optional<BookMark> findById(BookMarkId bookmarkId);
    void deleteById(BookMarkId bookmarkId);
    List<BookMark> findByUserUserId(Long userId);
    Optional<BookMark> findByBoardBoardIdAndUserUserId(Long boardId, Long userId);
    Page<BookMark> findByUserUserId(Long userId, Pageable pageable);

}
