package yiu.aisl.granity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.granity.config.CustomUserDetails;
import yiu.aisl.granity.domain.*;
import yiu.aisl.granity.dto.Request.BoardRequestDto;
import yiu.aisl.granity.dto.Request.CommentRequestDto;
import yiu.aisl.granity.exception.CustomException;
import yiu.aisl.granity.exception.ErrorCode;
import yiu.aisl.granity.repository.BoardRepository;
import yiu.aisl.granity.repository.CommentRepository;
import yiu.aisl.granity.repository.MajorGroupCodeRepository;
import yiu.aisl.granity.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final MajorGroupCodeRepository majorGroupCodeRepository;
    private final UserRepository userRepository;
    private final PushService pushService;

    // [API] 게시글 등록
    public boolean registerBoard(CustomUserDetails userDetails, BoardRequestDto request) throws Exception {
        // 데이터 없음 - 400
        if(request.getTitle().isEmpty() || request.getContents().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        // user 없음 - 404
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_MEMBER));

        // id 없음 - 404
        MajorGroupCode majorGroupCode = majorGroupCodeRepository.findById(request.getMajorGroupCode().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        try {
            Board board = Board.builder()
                    .title(request.getTitle())
                    .contents(request.getContents())
                    .file(request.getFile())
                    .user(user)
                    .checks(0)
                    .majorGroupCode(majorGroupCode)
                    .hit(0)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            boardRepository.save(board);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] 게시글 삭제
    public Boolean deleteBoard(CustomUserDetails userDetails, Integer boardId) throws Exception {
        // 해당 유저 없음 - 404
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_MEMBER));

        Board board = boardRepository.findByIdAndUser(boardId, user);
        // 해당 게시글 없음 - 404
        if(board == null) {
            throw new CustomException(ErrorCode.NOT_EXIST_ID);
        }
        boardRepository.delete(board);
        return true;
    }

    // [API] 게시글 수정
    public Boolean updateBoard(CustomUserDetails userDetails, Integer boardId, BoardRequestDto request) throws Exception {
        // 데이터 없음 - 400
        if(request.getTitle().isEmpty() || request.getContents().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        // 해당 유저 없음 - 404
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_MEMBER));

        Board board = boardRepository.findByIdAndUser(boardId, user);
        // 해당 게시글 없음 - 404
        if(board == null) {
            throw new CustomException(ErrorCode.NOT_EXIST_ID);
        }

        try {
            board.setTitle(request.getTitle());
            board.setContents(request.getContents());
            board.setFile(request.getFile());
            board.setUpdatedAt(LocalDateTime.now());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] 댓글 등록
    public Boolean registerComment(CustomUserDetails userDetails, Integer boardId, CommentRequestDto request) throws Exception {
        // 해당 유저 없음 - 404
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_MEMBER));

        int checks = 0;
        User checkUser = null;
        if(user.getRole() != 1) {
            checks = 1;
            checkUser = user;
        }

        // 해당 게시글 없음 - 404
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        // 데이터 없음 - 400
        if(request.getContents().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        try {
            Comment comment = Comment.builder()
                    .board(board)
                    .contents(request.getContents())
                    .user(user)
                    .checks(checks)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .checkUser(checkUser)
                    .build();

            String pushContents = "작성한 게시글에 댓글이 달렸습니다.";
            pushService.registerPush(4, boardId, board.getUser(), pushContents);

            commentRepository.save(comment);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // [API] 댓글 삭제
    public Boolean deleteComment(CustomUserDetails userDetails, Integer commentId) throws Exception {
        // 해당 유저 없음 - 404
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_MEMBER));

        // 해당 댓글 없음 - 404
        Comment comment = commentRepository.findByIdAndUser(commentId, user);
        if(comment == null) {
            throw new CustomException(ErrorCode.NOT_EXIST_ID);
        }

        commentRepository.delete(comment);

        return true;
    }

    // [API] 댓글 수정
    public Boolean updateComment(CustomUserDetails userDetails, Integer commentId, CommentRequestDto request) throws Exception {
        // 해당 유저 없음 - 404
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_MEMBER));

        // 해당 댓글 없음 - 404
        Comment comment = commentRepository.findByIdAndUser(commentId, user);
        if(comment == null) {
            throw new CustomException(ErrorCode.NOT_EXIST_ID);
        }

        // 데이터 미입력  - 400
        if(request.getContents().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        try {
            comment.setContents(request.getContents());
            comment.setUpdatedAt(LocalDateTime.now());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return true;
    }

    // [API] 댓글 승인
    public Boolean approvalComment(CustomUserDetails userDetails, Integer commentId) throws Exception {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_MEMBER));
        // 해당 댓글 없음 - 404
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        comment.setChecks(1);
        comment.setCheckUser(user);

        String pushContents = "작성한 댓글이 승인되었습니다.";
        pushService.registerPush(4, commentId, comment.getUser(), pushContents);

        return true;
    }

    // [API] 댓글 승인 취소
    public Boolean rejectionComment(CustomUserDetails userDetails, Integer commentId) throws Exception {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_MEMBER));
        // 해당 댓글 없음 - 404
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        comment.setChecks(0);
        comment.setCheckUser(user);

        String pushContents = "작성한 댓글이 거절되었습니다.";
        pushService.registerPush(4, commentId, comment.getUser(), pushContents);
        return true;
    }
}
