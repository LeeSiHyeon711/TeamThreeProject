package org.lsh.teamthreeproject.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.lsh.teamthreeproject.dto.BoardDTO;
import org.lsh.teamthreeproject.dto.BoardImageDTO;
import org.lsh.teamthreeproject.dto.UserDTO;
import org.lsh.teamthreeproject.entity.*;
import org.lsh.teamthreeproject.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final BoardLikeRepository boardLikeRepository;
    private final BookmarkRepository bookmarkRepository;


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
                .purchaseLink(boardDTO.getPurchaseLink())
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

            // 새로운 파일이 첨부되지 않았을 경우 기존 이미지 유지
            if (boardDTO.getFileNames() == null || boardDTO.getFileNames().isEmpty()) {
                // 기존 이미지를 유지하기 위해 현재 이미지들을 DTO에 설정
                List<BoardImageDTO> existingImageDTOs = existingBoard.getBoardImages().stream()
                        .map(image -> BoardImageDTO.builder()
                                .boardId(existingBoard.getBoardId())
                                .imageUrl(image.getImageUrl())
                                .build())
                        .collect(Collectors.toList());
                boardDTO.setImages(existingImageDTOs);
            }
        }

        // 게시글 정보 업데이트
        existingBoard.setTitle(boardDTO.getTitle());
        existingBoard.setContent(boardDTO.getContent());
        existingBoard.setPurchaseLink(boardDTO.getPurchaseLink());

        boardRepository.save(existingBoard);
    }

    @Override
    public List<BoardDTO> getPopularBoards() {
        LocalDateTime fourWeeksAgo = LocalDateTime.now().minusWeeks(4);
        return boardRepository.findTop10ByPopularity(fourWeeksAgo)
                .stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BoardDTO> findById(Long boardId) {
        return boardRepository.findById(boardId).map(this::convertEntityToDTO);
    }

    @Override
    public List<BoardDTO> findBoardsByUserId(long userId) {
        // userId에 해당하는 게시물 목록을 데이터베이스에서 조회
        return boardRepository.findByUser_UserId(userId)
                .stream()
                .map(board -> convertEntityToDTO(board)) // 엔티티 -> DTO 변환
                .collect(Collectors.toList());
    }

    @Override
    public Boolean toggleLike(Long boardId, Long userId) {
        Optional<BoardLike> existingLike = boardLikeRepository.findByBoardBoardIdAndUserUserId(boardId, userId);

        if (existingLike.isPresent()) { // 좋아요가 이미 있다면
            BoardLike boardLike = existingLike.get();
            boardLike.setIsDeleted(!boardLike.getIsDeleted()); // 좋아요 삭제여부 반환
            boardLikeRepository.save(boardLike);
            return !boardLike.getIsDeleted();
        } else {
            // 새로운 좋아요 추가
            BoardLike newLike = BoardLike.builder()
                    .id(new BoardLikeId(boardId, userId))
                    .board(new Board(boardId))
                    .user(new User(userId))
                    .isDeleted(false)
                    .build();
            boardLikeRepository.save(newLike);
            return true;
        }
    }

    @Override
    public Boolean toggleBookmark(Long boardId, Long userId) {
        Optional<BookMark> existingBookmark = bookmarkRepository.findByBoardBoardIdAndUserUserId(boardId, userId);

        if (existingBookmark.isPresent()) { // 존재하면
            // 북마크가 이미 존재하면 삭제하여 해제
            bookmarkRepository.delete(existingBookmark.get());
            return false;
        } else { // 존재하지 않으면
            // 새로운 북마크 추가
            BookMark newBookmark = BookMark.builder()
                    .id(new BookMarkId(boardId, userId))
                    .board(new Board(boardId))
                    .user(new User(userId))
                    .build();
            bookmarkRepository.save(newBookmark);
            return true;
        }
    }

    public Boolean isLikedByUser(Long boardId, Long userId) {
        return boardLikeRepository.findByBoardBoardIdAndUserUserId(boardId, userId)
                .map(boardLike -> !boardLike.getIsDeleted())
                .orElse(false);
    }

    public Boolean isBookmarkedByUser(Long boardId, Long userId) {
        return bookmarkRepository.findByBoardBoardIdAndUserUserId(boardId, userId).isPresent();
    }

    @Override
    public Page<BoardDTO> findAllByOrderByRegDateDesc(Pageable pageable, Long userId) {
        Page<Board> boards = boardRepository.findAllByOrderByRegDateDesc(pageable);
        return boards.map(board -> {
            BoardDTO dto = convertEntityToDTO(board);
            dto.setIsLiked(isLikedByUser(board.getBoardId(), userId));
            dto.setIsBookmarked(isBookmarkedByUser(board.getBoardId(), userId));
            return dto;
        });
    }

    @Override
    public Page<BoardDTO> getBoards(Long userId, Pageable pageable) {
        // Board 엔티티를 페이지네이션하여 가져오기
        Page<Board> boardPage = boardRepository.findByUser_UserId(userId, pageable);

        // Board -> BoardDTO로 변환하여 반환
        return boardPage.map(this::convertEntityToBoardDTO);
    }


    private BoardDTO convertEntityToBoardDTO(Board board) {
        return BoardDTO.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .visitCount(board.getVisitCount())
                .userId(board.getUser().getUserId())
                .userLoginId(board.getUser().getLoginId())
                .nickname(board.getUser().getNickname())
                .build();
    }

    private BoardDTO convertEntityToDTO(Board board) {
        // 파일 이름 리스트 생성
        List<String> fileNames = board.getBoardImages().stream()
                .map(BoardImage::getImageUrl)
                .collect(Collectors.toList());

        // 이미지 DTO 리스트 생성
        List<BoardImageDTO> images = board.getBoardImages().stream()
                .map(image -> BoardImageDTO.builder()
                        .boardId(board.getBoardId())
                        .imageUrl(image.getImageUrl())
                        .build())
                .collect(Collectors.toList());

        return BoardDTO.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .purchaseLink(board.getPurchaseLink())
                .regDate(board.getRegDate())
                .visitCount(board.getVisitCount())
                .userId(board.getUser().getUserId())
                .userLoginId(board.getUser().getLoginId())
                .fileNames(fileNames) // 파일 이름 리스트 추가
                .images(images)       // 이미지 DTO 리스트 추가
                .build();
    }

    // userId를 추가로 받아서 처리하는 새 메서드
    private BoardDTO convertEntityToDTOWithUserId(Board board, Long userId) {
        List<String> fileNames = board.getBoardImages().stream()
                .map(BoardImage::getImageUrl)
                .collect(Collectors.toList());

        List<BoardImageDTO> images = board.getBoardImages().stream()
                .map(image -> BoardImageDTO.builder()
                        .boardId(board.getBoardId())
                        .imageUrl(image.getImageUrl())
                        .build())
                .collect(Collectors.toList());

        Boolean isLiked = boardLikeRepository.findByBoardBoardIdAndUserUserId(board.getBoardId(), userId)
                .map(boardLike -> !boardLike.getIsDeleted())
                .orElse(false);
        Boolean isBookmarked = bookmarkRepository.findByBoardBoardIdAndUserUserId(board.getBoardId(), userId)
                .isPresent();

        return BoardDTO.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .purchaseLink(board.getPurchaseLink())
                .regDate(board.getRegDate())
                .visitCount(board.getVisitCount())
                .userId(board.getUser().getUserId())
                .userLoginId(board.getUser().getLoginId())
                .fileNames(fileNames)
                .images(images)
                .isLiked(isLiked)
                .isBookmarked(isBookmarked)
                .build();
    }


}
