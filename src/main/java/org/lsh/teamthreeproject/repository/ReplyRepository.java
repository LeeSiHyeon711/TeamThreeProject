package org.lsh.teamthreeproject.repository;

import org.lsh.teamthreeproject.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    Optional<Reply> findById(Long id);
    List<Reply> findByUserUserId(Long userId);
}
