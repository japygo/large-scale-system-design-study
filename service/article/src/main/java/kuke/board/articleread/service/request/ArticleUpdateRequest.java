package kuke.board.articleread.service.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ArticleUpdateRequest {
    private String title;
    private String content;
}
