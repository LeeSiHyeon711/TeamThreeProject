package org.lsh.teamthreeproject.repository;

import org.lsh.teamthreeproject.entity.BoardLike;
import org.lsh.teamthreeproject.entity.BoardLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, BoardLikeId> {
    Optional<BoardLike> findByBoardBoardIdAndUserUserId(Long boardId, Long userId);
}
