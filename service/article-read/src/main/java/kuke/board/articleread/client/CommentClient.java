package kuke.board.articleread.client;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentClient {
    private RestClient restClient;
    @Value("${endpoints.kuke-board-comment-service.url}")
    private String commentServiceUrl;

    @PostConstruct
    public void initRestClient() {
        restClient = RestClient.create(commentServiceUrl);
    }

    public long count(Long articleId) {
        try {
            return restClient.get()
                             .uri("/v2/comments/articles/{articleId}/count", articleId)
                             .retrieve()
                             .body(Long.class);
        } catch (Exception e) {
            log.error("[CommentClient.count] articleId={}", articleId, e);
            return 0;
        }
    }
}
