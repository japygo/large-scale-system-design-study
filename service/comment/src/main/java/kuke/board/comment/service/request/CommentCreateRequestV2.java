package kuke.board.comment.service.request;

import lombok.Getter;

@Getter
public class CommentCreateRequestV2 {
    // 댓글 생성 시 유효성 검증 필요
    // 검증 시 의존성, 단방향 등 고려 필요
    private Long articleId;
    private String content;
    private String parentPath;
    private Long writerId;
}
