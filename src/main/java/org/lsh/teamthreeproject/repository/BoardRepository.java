package org.lsh.teamthreeproject.repository;

import org.lsh.teamthreeproject.entity.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
    // 인기도 계산 쿼리문
    @Query(value = """
        select b 
        from Board b 
        left join b.boardLikes bl 
        left join b.bookmarks bm 
        where b.regDate >= :fourWeeksAgo
        group by b
        order by (4 * coalesce(sum(case when bl.isDeleted = false then 1 else 0 end), 0) 
        + 4 * coalesce(count(bm), 0)
        + 2 * b.visitCount) desc
        limit 10
    """)
    List<Board> findTop10ByPopularity(LocalDateTime fourWeeksAgo);
}
