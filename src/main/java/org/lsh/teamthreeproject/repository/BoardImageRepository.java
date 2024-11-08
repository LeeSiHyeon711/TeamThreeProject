package org.lsh.teamthreeproject.repository;

import org.lsh.teamthreeproject.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
    List<BoardImage> findByBoard_BoardId(long boardId); // Board 엔티티의 관계를 통해 검색
}
