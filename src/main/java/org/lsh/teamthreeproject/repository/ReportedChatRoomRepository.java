package org.lsh.teamthreeproject.repository;

import org.lsh.teamthreeproject.entity.ReportedChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportedChatRoomRepository extends JpaRepository<ReportedChatRoom, Long> {
}
