package org.lsh.teamthreeproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.lsh.teamthreeproject.dto.BoardDTO;
import org.lsh.teamthreeproject.dto.ReplyDTO;
import org.lsh.teamthreeproject.dto.UserDTO;
import org.lsh.teamthreeproject.dto.upload.UploadFileDTO;
import org.lsh.teamthreeproject.entity.User;
import org.lsh.teamthreeproject.service.BoardService;
import org.lsh.teamthreeproject.service.ReplyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Log4j2
public class BoardController {
    // 파일 저장 경로 설정
    @Value("${org.lsh.teamthreeproject.path}")
    private String uploadPath;

    private final BoardService boardService;
    private final ReplyService replyService;

    // 내가 쓴 게시물 하나 조회
    @GetMapping("/myBoard/{userId}/{boardId}")
    public String readOneBoard(@PathVariable("boardId") Long boardId,
                               Model model) {
        boardService.increaseVisitCount(boardId);
        Optional<BoardDTO> board = boardService.readBoard(boardId);
        if (board.isPresent()) {
            model.addAttribute("board", board.get());
        }
        return "/my/myBoard";
    }

    // 내가 쓴 게시물 전체 조회
    @GetMapping("/myBoardList/{userId}")
    public String readAllBoards(Model model,
                                @PathVariable("userId") Long userId) {
        List<BoardDTO> boards = boardService.readBoardsByUserId(userId);
        model.addAttribute("boards", boards);
        return "/my/myBoardList";
    }

    // 내 게시물 삭제
    @PostMapping("/delete/{userId}/{boardId}")
    public String deleteBoard(@PathVariable("userId") Long userId,
                              @PathVariable("boardId") Long boardId) {
        boardService.deleteBoard(boardId);
        return "redirect:/myBoardList/" + userId;
    }

    // 게시글 리스트 조회
    @GetMapping("/list")
    public String listGET(Model model) {
        List<BoardDTO> boardList = boardService.findAllByOrderByRegDateDesc(); // 모든 게시글을 가져오는 서비스 메소드
        model.addAttribute("boardList", boardList);
        return "board/list"; // list.html 파일을 반환하도록 설정
    }

    // 게시글 등록 페이지 이동
    @GetMapping("/register")
    public String registerGET(Model model, HttpSession session) {
        // 세션에서 user 정보를 가져옴
        UserDTO loggedInUser = (UserDTO) session.getAttribute("user");
        log.info(loggedInUser);
        // 로그인 상태인지 확인하고, 로그인된 상태라면 userId를 모델에 추가
        if (loggedInUser != null) {
            BoardDTO boardDTO = new BoardDTO();
            boardDTO.setUserId(loggedInUser.getUserId()); // userId를 설정
            model.addAttribute("dto", boardDTO); // 모델에 boardDTO 추가

        } else {
            // 로그인 정보가 없으면 로그인 페이지로 리다이렉트
            return "redirect:/user/login";
        }

        return "board/register"; // register.html 파일로 이동
    }

    @PostMapping("/board/register")
    public String registerPost(@ModelAttribute BoardDTO boardDTO,
                               @RequestParam("files") List<MultipartFile> files,
                               HttpSession session) {
        // 세션에서 로그인한 사용자 정보를 가져오기
        UserDTO loggedInUser = (UserDTO) session.getAttribute("user");
        if (loggedInUser == null) {
            // 유저 정보가 없으면 로그인 페이지로 리다이렉트
            return "redirect:/user/login";
        }

        // 유저 정보가 있으면 boardDTO에 userId를 설정
        Long userId = loggedInUser.getUserId();
        boardDTO.setUserId(userId);

        // 로그로 userId를 확인
        log.info("Registering post by userId=" + userId);

        // MultipartFile에서 파일 이름을 추출하여 DTO의 fileNames에 설정
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            if (fileName != null && !fileName.isEmpty()) {
                fileNames.add(fileName);
            }
        }

        // 파일 이름과 파일 리스트를 DTO에 설정
        boardDTO.setFileNames(fileNames);
        boardDTO.setFiles(files);

        // 서비스 호출하여 게시글 저장
        boardService.saveBoardWithImage(boardDTO);

        return "redirect:/list"; // 게시글 목록으로 리디렉션
    }


    @GetMapping("/read/{boardId}")
    public String read(@PathVariable Long boardId, Model model, HttpSession session) {
        // 게시글 읽기
        BoardDTO boardDTO = boardService.readOne(boardId);
        boardService.visitCount(boardId);
        log.info(boardDTO);

        // 세션에서 로그인된 사용자 정보 가져오기
        UserDTO loggedInUser = (UserDTO) session.getAttribute("user"); // 세션에 저장된 사용자 정보 사용
        model.addAttribute("dto", boardDTO);

        if (loggedInUser != null) {
            model.addAttribute("loggedInUser", loggedInUser);
        }

        // 댓글 리스트 추가
        List<ReplyDTO> replies = replyService.readRepliesByBoardId(boardId);
        model.addAttribute("replies", replies);

        return "board/read";  // 템플릿 이름이 "board/read"라면 이렇게 명시
    }


    private List<String> fileUpload(UploadFileDTO uploadFileDTO){

        List<String> list = new ArrayList<>();
        uploadFileDTO.getFiles().forEach(multipartFile -> {
            String originalName = multipartFile.getOriginalFilename();
            log.info(originalName);

            String uuid = UUID.randomUUID().toString();
            Path savePath = Paths.get(uploadPath, uuid+"_"+ originalName);
            boolean image = false;
            try {
                multipartFile.transferTo(savePath); // 서버에 파일저장
                //이미지 파일의 종류라면
                if(Files.probeContentType(savePath).startsWith("image")){
                    image = true;
                    File thumbFile = new File(uploadPath, "s_" + uuid+"_"+ originalName);
                    Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 250,250);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            list.add(uuid+"_"+originalName);
        });
        return list;
    }

    @GetMapping("/view/{fileName}")
    @ResponseBody
    public ResponseEntity<Resource> viewFileGet(@PathVariable("fileName") String fileName){
        Resource resource = new FileSystemResource(uploadPath+File.separator + fileName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try{
            headers.add("Content-Type", Files.probeContentType( resource.getFile().toPath() ));
        } catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    private void removeFile(List<String> fileNames){
        log.info("AAAAA"+fileNames.size());

        for(String fileName:fileNames){
            log.info("fileRemove method: "+fileName);
            Resource resource = new FileSystemResource(uploadPath+File.separator + fileName);
            String resourceName = resource.getFilename();

            // Map<String, Boolean> resultMap = new HashMap<>();
            boolean removed = false;

            try {
                String contentType = Files.probeContentType(resource.getFile().toPath());
                removed = resource.getFile().delete();

                //섬네일이 존재한다면
                if(contentType.startsWith("image")){
                    String fileName1=fileName.replace("s_","");
                    File originalFile = new File(uploadPath+File.separator + fileName1);
                    originalFile.delete();
                }

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String fileName) {
        Resource resource = boardService.downloadFile(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/delete/{boardId}")
    public String delete(@PathVariable Long boardId, RedirectAttributes redirectAttributes) {
        boardService.remove(boardId);
        redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 삭제되었습니다.");
        return "redirect:/list";
    }

    // 수정 페이지로 이동
    @GetMapping("/modify/{boardId}")
    public String modifyForm(@PathVariable Long boardId, Model model) {
        BoardDTO boardDTO = boardService.readOne(boardId);
        model.addAttribute("dto", boardDTO);
        return "board/modify"; // modify.html로 이동
    }

    // 수정 내용 저장 처리
    @PostMapping("/modify")
    public String modify(BoardDTO boardDTO, HttpServletRequest request) {
        boardService.update(boardDTO, request);
        return "redirect:/list";
    }
}
