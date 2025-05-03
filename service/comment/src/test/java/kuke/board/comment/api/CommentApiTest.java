package kuke.board.comment.api;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import kuke.board.comment.service.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class CommentApiTest {
    RestClient restClient = RestClient.create("http://localhost:9001");

    @Test
    void create() {
        CommentResponse response = createComment(new CommentCreateRequest(1L, "my comment 1", null, 1L));
        CommentResponse response2 = createComment(new CommentCreateRequest(1L, "my comment 2", response.getCommentId(), 1L));
        CommentResponse response3 = createComment(new CommentCreateRequest(1L, "my comment 3", response.getCommentId(), 1L));

        System.out.println("commentId = " + response.getCommentId());
        System.out.println("\tcommentId = " + response2.getCommentId());
        System.out.println("\tcommentId = " + response3.getCommentId());

        // commentId = 176974353057792000
        //   commentId = 176974353548525568
        //   commentId = 176974353603051520
    }

    CommentResponse createComment(CommentCreateRequest request) {
        return restClient.post()
                         .uri("/v1/comments")
                         .body(request)
                         .retrieve()
                         .body(CommentResponse.class);
    }

    @Test
    void read() {
        CommentResponse response = restClient.get()
                                             .uri("/v1/comments/{commentId}", 176974353057792000L)
                                             .retrieve()
                                             .body(CommentResponse.class);

        System.out.println("response = " + response);
    }

    @Test
    void delete() {
        // commentId = 176974353057792000
        //   commentId = 176974353548525568
        //   commentId = 176974353603051520

        restClient.delete()
                  .uri("/v1/comments/{commentId}", 176974353603051520L)
                  .retrieve()
                  .toBodilessEntity();
    }

    @Getter
    @AllArgsConstructor
    static class CommentCreateRequest {
        private Long articleId;
        private String content;
        private Long parentCommentId;
        private Long writerId;
    }
}
