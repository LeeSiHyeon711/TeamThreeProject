package org.lsh.teamthreeproject.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.lsh.teamthreeproject.dto.BoardDTO;
import org.lsh.teamthreeproject.dto.BoardImageDTO;
import org.lsh.teamthreeproject.dto.UserDTO;
import org.lsh.teamthreeproject.entity.Board;
import org.lsh.teamthreeproject.entity.BoardImage;
import org.lsh.teamthreeproject.entity.User;
import org.lsh.teamthreeproject.repository.BoardImageRepository;
import org.lsh.teamthreeproject.repository.BoardRepository;
import org.lsh.teamthreeproject.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class BoardServiceImpl implements BoardService {

    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final UserRepository userRepository;
    private final BoardImageServiceImpl boardImageServiceImpl;

    @Value("${org.lsh.teamthreeproject.path}")
    private String uploadPath;

    public class CustomNotFoundException extends RuntimeException {
        public CustomNotFoundException(String message) {
            super(message);
        }
    }

    // 내가 쓴 게시물 하나 조회
    public Optional<BoardDTO> readBoard(Long id) {
        return boardRepository.findById(id).map(this::convertEntityToDTO);
    }

    // 삭제
    @Override
    public void deleteBoard(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    // 내가 쓴 게시물 전체 조회
    @Override
    public List<BoardDTO> readBoardsByUserId(Long userId) {
        return boardRepository.findByUserUserId(userId).stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    // 조회수 증가
    @Transactional
    public void increaseVisitCount(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));
        board.UpdateVisitCount();
        boardRepository.save(board);
    }

    @Override
    public List<BoardDTO> getAllBoards() {
        List<Board> boardList = boardRepository.findAll();
        return boardList.stream()
                .map(board -> modelMapper.map(board, BoardDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Long register(BoardDTO boardDTO, UserDTO userDTO) {
        // UserDTO를 이용해 Board를 생성
        Board board = dtoToEntity(boardDTO, userDTO);
        Long boardId = boardRepository.save(board).getBoardId();
        return boardId;
    }


    @Override
    public BoardDTO readOne(Long boardId) {
        Optional<Board> result = boardRepository.findByIdWithImages(boardId);
        Board board = result.orElseThrow();
        BoardDTO boardDTO= entityToDTO(board);
        return boardDTO;
    }

//    @Override
//    public void modify(BoardDTO boardDTO) {
//        Optional<Board> result = boardRepository.findById(boardDTO.getBoardId());
//        Board board = result.orElseThrow();
//
//        // 게시물 제목과 내용 수정 (작성자 변경 불가)
//        board.change(boardDTO.getTitle(), boardDTO.getContent());
//
//        // 기존 이미지들 삭제
//        board.clearImages();
//
//        // 새로운 이미지를 추가
//        if (boardDTO.getFileNames() != null) {
//            for (String fileName : boardDTO.getFileNames()) {
//                String[] arr = fileName.split("_");
//                // 새로운 BoardImage 엔티티 생성
//                BoardImage newImage = BoardImage.builder()
//                        .board(board)
//                        .imageUrl(arr[1])
//                        .build();
//                board.getBoardImages().add(newImage); // Board 엔티티에 이미지 추가
//            }
//        }
//        boardRepository.save(board); // 변경 사항 저장
//    }


    @Override
    public void remove(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    @Override
    public void visitCount(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board ID: " + boardId));
        board.UpdateVisitCount();
        boardRepository.save(board);
    }

    @Override
    public Resource downloadFile(String fileName) {
        try {
            Path filePath = Paths.get(uploadPath).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("파일을 찾을 수 없습니다: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("파일을 찾을 수 없습니다: " + fileName, e);
        }
    }

    @Override
    public List<BoardDTO> findAllByOrderByRegDateDesc() {
        List<Board> boardList = boardRepository.findAllByOrderByRegDateDesc(); // 최신순 데이터 가져오기
        return boardList.stream()
                .map(this::entityToDTO) // Board -> BoardDTO로 변환
                .collect(Collectors.toList());
    }

    @Override
    public void saveBoardWithImage(BoardDTO boardDTO) {
        // Board 엔티티 생성 및 저장
        Board board = Board.builder()
                .user(userRepository.findById(boardDTO.getUserId()).orElseThrow())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .regDate(LocalDateTime.now())
                .visitCount(0L)
                .build();
        boardRepository.save(board);

        // 파일이 존재할 경우 경로를 설정하여 저장
        List<BoardImageDTO> images = new ArrayList<>();
        if (boardDTO.getFiles() != null && !boardDTO.getFiles().isEmpty()) {
            for (MultipartFile file : boardDTO.getFiles()) {
                if (!file.isEmpty()) {
                    try {
                        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename(); // UUID를 통해 파일명 유니크하게 생성
                        String filePath = uploadPath + "/" + fileName; // 파일 경로 생성
                        file.transferTo(new File(filePath));

                        // `BoardImage` 엔티티 저장 및 DTO 생성
                        BoardImage boardImage = BoardImage.builder()
                                .board(board)
                                .imageUrl(fileName) // 경로 대신 파일명만 저장
                                .build();
                        boardImageRepository.save(boardImage);

                        // BoardImageDTO 생성 후 추가
                        BoardImageDTO imageDTO = BoardImageDTO.builder()
                                .boardId(board.getBoardId())
                                .imageUrl(fileName) // 경로 대신 파일명만 추가
                                .build();
                        images.add(imageDTO);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            boardDTO.setImages(images); // BoardDTO에 이미지 정보 설정
        }
    }

    @Override
    public void update(BoardDTO boardDTO, HttpServletRequest request) {
        // 게시글 엔티티 가져오기
        Board existingBoard = boardRepository.findById(boardDTO.getBoardId())
                .orElseThrow(() -> new RuntimeException("Board not found: " + boardDTO.getBoardId()));

        // 새로운 파일이 첨부되지 않았을 경우 기존 이미지 유지
        if (boardDTO.getFiles() == null || boardDTO.getFiles().isEmpty()) {
            // 기존 이미지를 유지하기 위해 현재 이미지들을 DTO에 설정
            List<BoardImageDTO> existingImageDTOs = existingBoard.getBoardImages().stream()
                    .map(image -> BoardImageDTO.builder()
                            .boardId(existingBoard.getBoardId())
                            .imageUrl(image.getImageUrl())
                            .build())
                    .collect(Collectors.toList());
            boardDTO.setImages(existingImageDTOs);
        } else {
            // 기존 이미지 삭제 로직
            List<BoardImage> existingImages = boardImageRepository.findByBoard_BoardId(boardDTO.getBoardId());
            if (existingImages != null && !existingImages.isEmpty()) {
                for (BoardImage image : existingImages) {
                    // 파일 삭제 로직 추가 (파일 시스템에서 파일 삭제)
                    String filePath = uploadPath + "/" + image.getImageUrl();
                    File file = new File(filePath);
                    if (file.exists()) {
                        file.delete();  // 실제 파일 삭제
                    }
                }
                boardImageRepository.deleteAll(existingImages); // 데이터베이스에서 기존 이미지 삭제
            }

            // 새로운 파일이 존재할 경우 경로를 설정하여 저장
            List<BoardImageDTO> newImages = new ArrayList<>();
            if (boardDTO.getFiles() != null && !boardDTO.getFiles().isEmpty()) {
                for (MultipartFile file : boardDTO.getFiles()) {
                    if (!file.isEmpty()) {
                        try {
                            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                            String filePath = uploadPath + "/" + fileName;
                            file.transferTo(new File(filePath));

                            // 새로운 `BoardImage` 엔티티 생성 후 저장
                            BoardImage newBoardImage = BoardImage.builder()
                                    .board(existingBoard)
                                    .imageUrl(fileName)
                                    .build();
                            boardImageRepository.save(newBoardImage);

                            // 새로운 BoardImageDTO 추가
                            BoardImageDTO imageDTO = BoardImageDTO.builder()
                                    .boardId(existingBoard.getBoardId())
                                    .imageUrl(fileName)
                                    .build();
                            newImages.add(imageDTO);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                boardDTO.setImages(newImages); // BoardDTO에 새 이미지 정보 설정
            }
        }

        // 게시글 정보 업데이트
        existingBoard.setTitle(boardDTO.getTitle());
        existingBoard.setContent(boardDTO.getContent());
        existingBoard.setPurchaseLink(boardDTO.getPurchaseLink());

        boardRepository.save(existingBoard);
    }

    private BoardDTO convertEntityToDTO(Board board) {
        return BoardDTO.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .purchaseLink(board.getPurchaseLink())
                .regDate(board.getRegDate())
                .userId(board.getUser().getUserId())
                .userLoginId(board.getUser().getLoginId())
                .build();
    }

}
