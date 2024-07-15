package yiu.aisl.granity.dto.Request;

import lombok.*;
import yiu.aisl.granity.domain.*;

@Getter
@Setter
public class CommentRequestDto {
    private Board board;
    private String contents;
    private User user;
    private Integer checks;
    private User checkUser;
}
