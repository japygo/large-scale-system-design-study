package kuke.board.articleread.client.api;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import kuke.board.articleread.service.response.ArticleReadResponse;

class ArticleReadApiTest {
    RestClient restClient = RestClient.create("http://localhost:9005");

    @Test
    void readTest() {
        ArticleReadResponse response = restClient.get()
                                                 .uri("/v1/articles/{articleId}", 176551543106539520L)
                                                 .retrieve()
                                                 .body(ArticleReadResponse.class);

        System.out.println("response = " + response);
    }
}
