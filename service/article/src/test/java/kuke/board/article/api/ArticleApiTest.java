package kuke.board.article.api;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import kuke.board.articleread.service.response.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

class ArticleApiTest {
    RestClient restClient = RestClient.create("http://localhost:9000");

    @Test
    void createTest() {
        ArticleResponse response = create(new ArticleCreateRequest(
            "hi", "my content", 1L, 1L
        ));
        System.out.println("response = " + response);
    }

    ArticleResponse create(ArticleCreateRequest request) {
        return restClient.post()
                         .uri("/v1/articles")
                         .body(request)
                         .retrieve()
                         .body(ArticleResponse.class);
    }

    @Test
    void readTest() {
        ArticleResponse response = read(176543634131353600L);
        System.out.println("response = " + response);
    }

    ArticleResponse read(Long articleId) {
        return restClient.get()
                         .uri("/v1/articles/{articleId}", articleId)
                         .retrieve()
                         .body(ArticleResponse.class);
    }

    @Test
    void updateTest() {
        update(176543634131353600L);
        ArticleResponse response = read(176543634131353600L);
        System.out.println("response = " + response);
    }

    void update(Long articleId) {
        restClient.put()
                  .uri("/v1/articles/{articleId}", articleId)
                  .body(new ArticleUpdateRequest("hi 2", "my content 2"))
                  .retrieve()
                  .toBodilessEntity();
    }

    @Test
    void deleteTest() {
        restClient.delete()
                  .uri("/v1/articles/{articleId}", 176543634131353600L)
                  .retrieve()
                  .toBodilessEntity();
    }

    @Getter
    @AllArgsConstructor
    static class ArticleCreateRequest {
        private String title;
        private String content;
        private Long writerId;
        private Long boardId;
    }

    @Getter
    @AllArgsConstructor
    static class ArticleUpdateRequest {
        private String title;
        private String content;
    }
}
