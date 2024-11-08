package org.lsh.teamthreeproject.repository;

import org.lsh.teamthreeproject.entity.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findById(Long id);
    List<Board> findByUserUserId(Long userId);
    @EntityGraph(attributePaths = {"imageSet"})
    @Query("select b from Board b where b.boardId=:boardId")
    Optional<Board> findByIdWithImages(Long boardId);
    // 최신 글을 먼저 가져오는 방식으로 Repository에서 조회
    List<Board> findAllByOrderByRegDateDesc();
    // 게시글 삭제 메서드
    void deleteById(Long boardId);
}
