package org.lsh.teamthreeproject.controller;

import org.lsh.teamthreeproject.dto.ReportDTO;
import org.lsh.teamthreeproject.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/all") // /reports/all 경로로 들어오는 GET 요청을 처리
    public String getAllReports(Model model, @RequestParam(required = false) String type) {
        List<ReportDTO> reports = reportService.getAllReports(type); // 유형 필터링
        model.addAttribute("reports", reports); // 모델에 보고서 리스트 추가
        return "report/report"; // 보고서 HTML 페이지 이름 반환
    }

    @GetMapping("/api/all") // /reports/api/all 경로로 들어오는 GET 요청을 처리
    @ResponseBody // JSON 응답으로 반환
    public List<ReportDTO> getAllReportsApi(@RequestParam(required = false) String type) {
        return reportService.getAllReports(type); // JSON으로 유형에 따른 보고서 반환
    }

    @DeleteMapping("/{id}") // /reports/{id} 경로로 DELETE 요청을 처리
    public ResponseEntity<Void> deleteReport(@PathVariable("id") long reportId) {
        reportService.deleteReport(reportId);
        return ResponseEntity.noContent().build(); // 204 No Content 응답 반환
    }
}
