package kuke.board.hotarticle.api;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import kuke.board.hotarticle.service.response.HotArticleResponse;

class HotArticleApiTest {
    RestClient restClient = RestClient.create("http://localhost:9004");

    @Test
    void readAllTest() {
        List<HotArticleResponse> responses = restClient.get()
                                                       .uri("/v1/hot-articles/articles/date/{dataStr}", "20250509")
                                                       .retrieve()
                                                       .body(new ParameterizedTypeReference<>() {});

        for (HotArticleResponse response : responses) {
            System.out.println("response = " + response);
        }
    }
}
