package kuke.board.articleread.service.response;

import java.util.List;

import lombok.Getter;

@Getter
public class ArticleReadPageResponse {
    private List<ArticleReadResponse> articles;
    private Long articleCount;

    public static ArticleReadPageResponse of(List<ArticleReadResponse> articles, Long articleCount) {
        ArticleReadPageResponse articleReadPageResponse = new ArticleReadPageResponse();
        articleReadPageResponse.articles = articles;
        articleReadPageResponse.articleCount = articleCount;
        return articleReadPageResponse;
    }
}
