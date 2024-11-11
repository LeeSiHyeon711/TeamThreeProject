package org.lsh.teamthreeproject.repository;

import org.lsh.teamthreeproject.entity.Board;
import org.lsh.teamthreeproject.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    Optional<Reply> findById(Long id);
    List<Reply> findByUserUserId(Long userId);
    List<Reply> findByBoardBoardId(Long boardId);
    List<Reply> findByBoard(Board board);
    // boardId로 댓글을 찾아오며 pageable 객체를 통해 페이징 정보를 가져온다.
    Page<Reply> findByBoardBoardId(Long boardId, Pageable pageable);
}
