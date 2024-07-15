package yiu.aisl.granity.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.granity.config.CustomUserDetails;
import yiu.aisl.granity.domain.*;
import yiu.aisl.granity.dto.Response.BoardResponseDto;
import yiu.aisl.granity.dto.Response.CommentResponseDto;
import yiu.aisl.granity.dto.Response.PushResponseDto;
import yiu.aisl.granity.exception.CustomException;
import yiu.aisl.granity.exception.ErrorCode;
import yiu.aisl.granity.repository.*;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PushRepository pushRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    // [API] 알림내역 조회(종 모양의 붉은 점 여부 - 쪽지 알림은 제외)
    public List<PushResponseDto> getMyPushList(CustomUserDetails userDetails) throws Exception {
        // 해당 유저 없음 - 404
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_MEMBER));

        List<Push> pushs = pushRepository.findByUser(user);
        List<PushResponseDto> getListDto = new ArrayList<>();
        pushs.forEach(s -> getListDto.add(PushResponseDto.getPushDto(s)));
        return getListDto;
    }

    // [API] 특정 push 알림 확인
    public Boolean pushCheck(CustomUserDetails userDetails, Integer pushId) throws Exception {
        // 권한 없음 - 403
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NO_AUTH));

        Push push = pushRepository.findByIdAndUser(pushId, user).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_ID));

        push.setChecks(0);
        return true;
    }

    // [API] 내가 작성한 게시글 조회
    public List<BoardResponseDto> getMyBoardList(CustomUserDetails userDetails) throws Exception {
        // 해당 유저 없음 - 404
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_MEMBER));

        List<Board> boards = boardRepository.findByUser(user);
        List<BoardResponseDto> getListDto = new ArrayList<>();
        boards.forEach(s -> getListDto.add(BoardResponseDto.GetBoardDto(s)));
        return getListDto;
    }

    // [API] 내가 작성한 댓글 조회
    public List<CommentResponseDto> getMyCommentList(CustomUserDetails userDetails) throws Exception {
        // 해당 유저 없음 - 404
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_EXIST_MEMBER));

        List<Comment> comments = commentRepository.findByUser(user);
        List<CommentResponseDto> getListDto = new ArrayList<>();
        comments.forEach(s -> getListDto.add(CommentResponseDto.GetCommentDto(s)));
        return getListDto;
    }
}
