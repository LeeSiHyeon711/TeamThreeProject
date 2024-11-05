package org.lsh.teamthreeproject.repository;

import org.lsh.teamthreeproject.entity.ReportedBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportedBoardRepository extends JpaRepository<ReportedBoard, Long> {
}
