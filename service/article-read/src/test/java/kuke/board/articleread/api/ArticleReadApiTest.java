package kuke.board.articleread.api;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import kuke.board.articleread.service.response.ArticleReadPageResponse;
import kuke.board.articleread.service.response.ArticleReadResponse;

class ArticleReadApiTest {
    RestClient articleReadRestClient = RestClient.create("http://localhost:9005");
    RestClient articleRestClient = RestClient.create("http://localhost:9000");

    @Test
    void readTest() {
        ArticleReadResponse response = articleReadRestClient.get()
                                                            .uri("/v1/articles/{articleId}", 176551543106539520L)
                                                            .retrieve()
                                                            .body(ArticleReadResponse.class);

        System.out.println("response = " + response);
    }

    @Test
    void readAllTest() {
        ArticleReadPageResponse response = articleReadRestClient
            .get()
            .uri("/v1/articles?boardId={boardId}&page={page}&pageSize={pageSize}", 1L, 3000L, 5L)
            .retrieve()
            .body(ArticleReadPageResponse.class);

        System.out.println("response.getArticleCount() = " + response.getArticleCount());
        for (ArticleReadResponse article : response.getArticles()) {
            System.out.println("article.getArticleId() = " + article.getArticleId());
        }

        ArticleReadPageResponse response2 = articleReadRestClient
            .get()
            .uri("/v1/articles?boardId={boardId}&page={page}&pageSize={pageSize}", 1L, 3000L, 5L)
            .retrieve()
            .body(ArticleReadPageResponse.class);

        System.out.println("response2.getArticleCount() = " + response2.getArticleCount());
        for (ArticleReadResponse article : response2.getArticles()) {
            System.out.println("article.getArticleId() = " + article.getArticleId());
        }
    }

    @Test
    void readAllInfiniteScrollTest() {
        List<ArticleReadResponse> responses = articleReadRestClient
            .get()
            .uri("/v1/articles/infinite-scroll?boardId={boardId}&pageSize={pageSize}&lastArticleId={lastArticleId}", 1L, 5L, 179831409887014912L)
            .retrieve()
            .body(new ParameterizedTypeReference<>() {
            });

        for (ArticleReadResponse response : responses) {
            System.out.println("response.getArticleId() = " + response.getArticleId());
        }

        List<ArticleReadResponse> responses2 = articleReadRestClient
            .get()
            .uri("/v1/articles/infinite-scroll?boardId={boardId}&pageSize={pageSize}&lastArticleId={lastArticleId}", 1L, 5L, 179831409887014912L)
            .retrieve()
            .body(new ParameterizedTypeReference<>() {
            });

        for (ArticleReadResponse response : responses2) {
            System.out.println("response.getArticleId() = " + response.getArticleId());
        }
    }
}
