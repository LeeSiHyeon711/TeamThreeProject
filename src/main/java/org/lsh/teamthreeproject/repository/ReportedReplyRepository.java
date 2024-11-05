package org.lsh.teamthreeproject.repository;

import org.lsh.teamthreeproject.entity.ReportedReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportedReplyRepository extends JpaRepository<ReportedReply, Long> {
}
