package org.lsh.teamthreeproject.service;

import jakarta.servlet.http.HttpServletRequest;
import org.lsh.teamthreeproject.dto.BoardDTO;
import org.lsh.teamthreeproject.dto.UserDTO;
import org.lsh.teamthreeproject.entity.Board;
import org.lsh.teamthreeproject.entity.BoardImage;
import org.lsh.teamthreeproject.entity.User;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface BoardService {
    public Optional<BoardDTO> readBoard(Long id);
    void deleteBoard(Long boardId);
    List<BoardDTO> readBoardsByUserId(Long userId);
    void increaseVisitCount(Long boardId);
    List<BoardDTO> getAllBoards();
    Long register(BoardDTO boardDTO, UserDTO userDTO);
    BoardDTO readOne(Long boardId);
    void remove(Long boardId);
    void visitCount(Long boardId);
    Resource downloadFile(String fileName);
    List<BoardDTO> findAllByOrderByRegDateDesc();
    void saveBoardWithImage(BoardDTO boardDTO);
    void update(BoardDTO boardDTO, HttpServletRequest request);
    List<BoardDTO> getPopularBoards();
    Optional<BoardDTO> findById(Long boardId);
    List<BoardDTO> findBoardsByUserId(long userId);
    Boolean toggleLike(Long boardId, Long userId);
    Boolean toggleBookmark(Long boardId, Long userId);
    Boolean isLikedByUser(Long boardId, Long userId);
    Boolean isBookmarkedByUser(Long boardId, Long userId);
    Page<BoardDTO> findAllByOrderByRegDateDesc(Pageable pageable, Long userId);


    default Board dtoToEntity(BoardDTO boardDTO, UserDTO userDTO) {
        // User 객체를 생성하여 필요한 정보를 설정
        User user = User.builder()
                .userId(userDTO.getUserId()) // UserDTO에서 userId 가져오기
                .email(userDTO.getEmail()) // 추가된 이메일 정보 설정
                .nickname(userDTO.getNickname()) // 닉네임 등 필요한 정보 설정
                .build();

        // Board 객체 생성
        return Board.builder()
                .user(user)
                .boardId(boardDTO.getBoardId())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .regDate(boardDTO.getRegDate())
                .purchaseLink(boardDTO.getPurchaseLink())
                .build();
    }
    default BoardDTO entityToDTO(Board board) {
        // 이미지 URL 리스트 추출
        List<String> imageUrls = new ArrayList<>();
        for (BoardImage boardImage : board.getBoardImages()) {
            imageUrls.add(boardImage.getImageUrl());
        }

        // UserDTO 생성
        UserDTO userDTO = UserDTO.builder()
                .userId(board.getUser().getUserId())
                .email(board.getUser().getEmail()) // 이메일 추가
                .nickname(board.getUser().getNickname()) // 닉네임 추가
                .loginId(board.getUser().getLoginId()) // 로그인 ID 추가
                .build();

        // BoardDTO 생성
        return BoardDTO.builder()
                .userId(userDTO.getUserId()) // userId 추가
                .userLoginId(userDTO.getLoginId()) // userLoginId 추가
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .visitCount(board.getVisitCount())
                .purchaseLink(board.getPurchaseLink()) // purchaseLink도 추가!
                .fileNames(imageUrls) // 이미지 URL 리스트 설정 추가
                .build();
    }

}
