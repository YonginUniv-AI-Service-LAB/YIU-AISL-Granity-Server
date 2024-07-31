package yiu.aisl.granity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.granity.config.CustomUserDetails;
import yiu.aisl.granity.controller.FileController;
import yiu.aisl.granity.domain.*;
import yiu.aisl.granity.dto.Request.BoardRequestDto;
import yiu.aisl.granity.dto.Request.CommentRequestDto;
import yiu.aisl.granity.dto.Request.FileRequestDto;
import yiu.aisl.granity.dto.Response.BoardResponseDto;
import yiu.aisl.granity.dto.Response.FileResponseDto;
import yiu.aisl.granity.exception.CustomException;
import yiu.aisl.granity.exception.ErrorCode;
import yiu.aisl.granity.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final MajorGroupCodeRepository majorGroupCodeRepository;
    private final UserRepository userRepository;
    private final PushService pushService;
    private final FileService fileService;
    private final FileRepository fileRepository;
    private final FileController fileController;

    // [API] 게시글 전체 조회
    public List<BoardResponseDto> getBoards(MajorGroupCode majorGroupCode) throws Exception {
        List<Board> boards = boardRepository.findAllByMajorGroupCode(majorGroupCode);
        List<BoardResponseDto> getListDto = new ArrayList<>();
        for(Board board : boards) {
            List<File> files = fileRepository.findAllByTypeAndTypeId(3, board.getId());
            List<Comment> comments = commentRepository.findByBoard(board);
            getListDto.add(BoardResponseDto.GetBoardDto(board, files, comments));
        }
        return getListDto;
    }

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
        if(majorGroupCode.getHidden() == 1) {
            throw new CustomException(ErrorCode.NOT_EXIST_ID);
        }
        List<FileRequestDto> files = fileController.uploadFiles(request.getFiles());
        System.out.println(request.getFiles());
        System.out.println("파일: " +files);
        try {
            Board board = Board.builder()
                    .title(request.getTitle())
                    .contents(request.getContents())
                    .user(user)
                    .checks(0)
                    .majorGroupCode(majorGroupCode)
                    .hit(0)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Board mkBoard = boardRepository.save(board);
            fileService.saveFiles(3, mkBoard.getId(), files);
        } catch (Exception e) {
            System.out.println("게시글 작성 오류: " +e);
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
        List<FileResponseDto> deleteFiles = fileService.findAllFileByTypeAndTypeId(3, boardId);

        boardRepository.delete(board);
        fileController.deleteFiles(deleteFiles);
        fileService.deleteAllFileByTypeAndTypeId(3, boardId); // DB 삭제
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
            board.setUpdatedAt(LocalDateTime.now());

            // 기존 파일 목록 조회
            List<FileResponseDto> existingFiles = fileService.findAllFileByTypeAndTypeId(3, boardId);

            // 삭제할 파일 정보 조회
            List<FileResponseDto> deleteFiles = fileService.findAllFileByTypeAndTypeId(3, boardId);

            // 파일 삭제
            fileController.deleteFiles(deleteFiles);

            // 파일 삭제
            fileController.deleteFiles(deleteFiles);
            fileService.deleteAllFileByTypeAndTypeId(3, boardId); // DB 삭제

            // 파일 업로드
            List<FileRequestDto> uploadFiles = fileController.uploadFiles(request.getFiles());

            // 파일 정보 저장
            fileService.saveFiles(3, boardId, uploadFiles);
        } catch (Exception e) {
            System.out.println(e);
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
            Comment mkComment = commentRepository.save(comment);
            if(user.getRole() == 1) {
                List<User> assistant = userRepository.findByRoleAndMajor(2, request.getMajorGroupCode());
                List<User> professor = userRepository.findByRoleAndMajor(3, request.getMajorGroupCode());
                for(User users : assistant) {
                    String pushContents = "승인 대기 중인 댓글이 있습니다. 확인해주세요.";
                    pushService.registerPushs(4, mkComment.getId(), assistant, pushContents);
                }
                for(User users : professor) {
                    String pushContents = "승인 대기 중인 댓글이 있습니다. 확인해주세요.";
                    pushService.registerPushs(4, mkComment.getId(), professor, pushContents);
                }
            }
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

        int checks = 0;
        User checkUser = null;
        if(user.getRole() != 1) {
            checks = 1;
            checkUser = user;
        }

        // 데이터 미입력  - 400
        if(request.getContents().isEmpty()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }

        try {
            comment.setContents(request.getContents());
            comment.setUpdatedAt(LocalDateTime.now());
            comment.setChecks(checks);
            comment.setCheckUser(checkUser);

            if(user.getRole() == 1) {
                List<User> assistant = userRepository.findByRoleAndMajor(2, request.getMajorGroupCode());
                List<User> professor = userRepository.findByRoleAndMajor(3, request.getMajorGroupCode());
                for(User users : assistant) {
                    String pushContents = "승인 대기 중인 댓글이 있습니다. 확인해주세요.";
                    pushService.registerPushs(4, commentId, assistant, pushContents);
                }
                for(User users : professor) {
                    String pushContents = "승인 대기 중인 댓글이 있습니다. 확인해주세요.";
                    pushService.registerPushs(4, commentId, professor, pushContents);
                }
            }
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

    // [API] 조회수 증가
    public Boolean makeHits(Integer boardId) throws Exception {
        // 해당 게시글 없음 - 404
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        board.setHit(board.getHit() + 1);

        return true;
    }
}
