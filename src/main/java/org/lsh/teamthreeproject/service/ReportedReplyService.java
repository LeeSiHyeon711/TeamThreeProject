package org.lsh.teamthreeproject.service;

import org.lsh.teamthreeproject.dto.ReportedBoardDTO;
import org.lsh.teamthreeproject.dto.ReportedReplyDTO;

public interface ReportedReplyService {
    void saveReport(ReportedReplyDTO reportedReplyDTO);  // 신고 저장 메서드

}
