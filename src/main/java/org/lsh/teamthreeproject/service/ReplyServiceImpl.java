package org.lsh.teamthreeproject.service;

import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.lsh.teamthreeproject.dto.BoardDTO;
import org.lsh.teamthreeproject.dto.ReplyDTO;
import org.lsh.teamthreeproject.dto.UserDTO;
import org.lsh.teamthreeproject.entity.Board;
import org.lsh.teamthreeproject.entity.Reply;
import org.lsh.teamthreeproject.entity.User;
import org.lsh.teamthreeproject.repository.BoardRepository;
import org.lsh.teamthreeproject.repository.ReplyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
@Transactional
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardServiceImpl boardServiceImpl;
    private final UserService userService;
    private final BoardRepository boardRepository;

    @Override
    public Optional<ReplyDTO> readReply(Long replyId) {
        return replyRepository.findById(replyId).map(this::convertEntityToDTO);
    }

    @Override
    public List<ReplyDTO> readAllReplies() {
        return replyRepository.findAll().stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReplyDTO> readRepliesByUserId(Long userId) {
        return replyRepository.findByUserUserId(userId).stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReplyDTO updateReply(ReplyDTO replyDTO, String content) {
        Reply reply = replyRepository.findById(replyDTO.getReplyId())
                .orElseThrow(() -> new IllegalArgumentException("Reply not found"));
        reply.setContent(content);
        replyRepository.save(reply);
        return convertEntityToDTO(reply);
    }

    @Override
    public List<ReplyDTO> readRepliesByBoardId(Long boardId) {
        // boardId로 Board 객체를 찾아서 가져옴
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        List<Reply> replies = replyRepository.findByBoard(board);

        return replies.stream().map(reply -> {
            System.out.println("ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇReply ID: " + reply.getReplyId());
            System.out.println("ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇUser ID: " + reply.getUser().getUserId());

            ReplyDTO replyDTO = ReplyDTO.builder()
                    .replyId(reply.getReplyId())
                    .content(reply.getContent())
                    .createdDate(reply.getCreatedDate())
                    .isDeleted(reply.getIsDeleted())
                    .boardId(reply.getBoard().getBoardId())
                    .userId(reply.getUser().getUserId())
                    .build();

            // 작성자 정보를 UserService로부터 가져와 설정
            UserDTO userDTO = userService.readUser(reply.getUser().getUserId())
                    .orElse(UserDTO.builder().nickname("Unknown").build());
            replyDTO.setReplyer(userDTO.getNickname());

            return replyDTO;
        }).collect(Collectors.toList());

    }


    @Override
    public ReplyDTO createReply(BoardDTO boardDTO, UserDTO userDTO, String content) {
        Board board = convertBoardDTOToEntity(boardDTO);
        User user = convertUserDTOToEntity(userDTO);
        Reply reply = Reply.builder()
                .board(board)
                .user(user)
                .content(content)
                .isDeleted(false)
                .build();
        replyRepository.save(reply);
        return convertEntityToDTO(reply);
    }

    @Override
    public void saveReply(ReplyDTO replyDTO) {
        Reply reply = convertDTOToEntity(replyDTO);
        replyRepository.save(reply);
    }

    public boolean deleteReply(Long replyId) {
        Optional<Reply> replyOptional = replyRepository.findById(replyId);

        if (replyOptional.isPresent()) {
            replyRepository.deleteById(replyId);
            return true;
        }
        return false;
    }

    @Override
    public List<ReplyDTO> getRepliesByBoardId(Long boardId, int page, int size) {
        // pageRequest 객체 생성, 페이징 정보와 정렬 방식 설정
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        // boardId에 해당하는 댓글 페이징 방식으로 데이터베이스에서 조회
        return replyRepository.findByBoardBoardId(boardId, pageRequest)
                .stream()
                .map(this::convertEntityToDTO)
                .toList();
    }

    @Override
    public Page<ReplyDTO> findRepliesByBoardId(Long boardId, Pageable pageable) {
        // repository에서 Page<Reply>를 가져온 다음, DTO로 변환하여 반환
        Page<Reply> replyPage = replyRepository.findByBoardBoardId(boardId, pageable);
        return replyPage.map(this::convertEntityToDTO); // Page 내부 요소들을 ReplyDTO로 변환
    }


    private Reply convertDTOToEntity(ReplyDTO replyDTO) {
        Board board = new Board();
        board.setBoardId(replyDTO.getBoardId());

        User user = new User();
        user.setUserId(replyDTO.getUserId());

        return Reply.builder()
                .replyId(replyDTO.getReplyId())
                .board(board)
                .user(user)
                .content(replyDTO.getContent())
                .createdDate(replyDTO.getCreatedDate())
                .isDeleted(replyDTO.getIsDeleted())
                .build();
    }

    private Board convertBoardDTOToEntity(BoardDTO boardDTO) {
        return Board.builder()
                .boardId(boardDTO.getBoardId())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .purchaseLink(boardDTO.getPurchaseLink())
                .regDate(boardDTO.getRegDate())
                .build();
    }

    private User convertUserDTOToEntity(UserDTO userDTO) {
        return User.builder()
                .userId(userDTO.getUserId())
                .nickname(userDTO.getNickname())
                .loginId(userDTO.getLoginId())
                .email(userDTO.getEmail())
                .build();
    }

    private ReplyDTO convertEntityToDTO(Reply reply) {
        return ReplyDTO.builder()
                .replyId(reply.getReplyId())
                .content(reply.getContent())
                .createdDate(reply.getCreatedDate())
                .isDeleted(reply.getIsDeleted())
                .boardId(reply.getBoard().getBoardId())
                .userId(reply.getUser().getUserId())
                .replyer(reply.getUser().getNickname()) // 작성자 정보 추가
                .build();
    }
}
