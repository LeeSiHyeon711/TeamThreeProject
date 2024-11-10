package org.lsh.teamthreeproject.controller;

import lombok.extern.log4j.Log4j2;
import org.lsh.teamthreeproject.dto.ReportDTO;
import org.lsh.teamthreeproject.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
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

    @PostMapping("/api/report") // 신고 요청을 처리하는 POST 엔드포인트
    @ResponseBody
    public ResponseEntity<String> createReport(@RequestBody ReportDTO reportDTO) {
        try {
            log.info("Received ReportDTO: {}", reportDTO); // RequestBody가 제대로 매핑되는지 확인

            reportService.saveReport(reportDTO); // 신고 저장 로직을 호출
            return ResponseEntity.ok("신고가 접수되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("해당 채팅방이 존재하지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("신고 처리 중 오류가 발생했습니다.");
        }
    }

}
