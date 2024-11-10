package org.lsh.teamthreeproject.service;

import org.lsh.teamthreeproject.dto.ReportDTO;

import java.util.List;

public interface ReportService {
    // 통합 조회 메서드
    List<ReportDTO> getAllReports();

    // 특정 유형의 신고 조회 메서드
    List<ReportDTO> getAllReports(String type);

    // 신고 삭제 메서드
    void deleteReport(long reportId);

    // 신고 저장 메서드
    void saveReport(ReportDTO reportDTO);
}
