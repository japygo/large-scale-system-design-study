package kuke.board.comment.api;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import kuke.board.comment.service.response.CommentPageResponse;
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

    @Test
    void readAll() {
        CommentPageResponse response = restClient.get()
                                             .uri("/v1/comments?articleId=1&page=1&pageSize=10")
                                             .retrieve()
                                             .body(CommentPageResponse.class);
        System.out.println("response.getCommentCount() = " + response.getCommentCount());
        for (CommentResponse comment : response.getComments()) {
            if (!comment.getCommentId().equals(comment.getParentCommentId())) {
                System.out.print("\t");
            }
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }

        /**
         * 1번 페이지 수행 결과
         * comment.getCommentId() = 176977596657418240
         * 	comment.getCommentId() = 176977596707749890
         * comment.getCommentId() = 176977596657418241
         * 	comment.getCommentId() = 176977596707749888
         * comment.getCommentId() = 176977596657418242
         * 	comment.getCommentId() = 176977596707749895
         * comment.getCommentId() = 176977596657418243
         * 	comment.getCommentId() = 176977596707749897
         * comment.getCommentId() = 176977596657418244
         * 	comment.getCommentId() = 176977596707749893
         */
    }

    @Test
    void readAllInfiniteScroll() {
        List<CommentResponse> responses = restClient.get()
                                                    .uri("/v1/comments/infinite-scroll?articleId=1&pageSize=5")
                                                    .retrieve()
                                                    .body(new ParameterizedTypeReference<List<CommentResponse>>() {});

        System.out.println("firstPage");
        for (CommentResponse comment : responses) {
            if (!comment.getCommentId().equals(comment.getParentCommentId())) {
                System.out.print("\t");
            }
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }

        Long lastParentCommentId = responses.getLast().getParentCommentId();
        Long lastCommentId = responses.getLast().getCommentId();

        List<CommentResponse> responses2 =
            restClient.get()
                      .uri(
                          "/v1/comments/infinite-scroll?articleId=1&pageSize=5&lastParentCommentId={lastParentCommentId}&lastCommentId={lastCommentId}",
                          lastParentCommentId, lastCommentId)
                      .retrieve()
                      .body(new ParameterizedTypeReference<List<CommentResponse>>() {});

        System.out.println("secondPage");
        for (CommentResponse comment : responses2) {
            if (!comment.getCommentId().equals(comment.getParentCommentId())) {
                System.out.print("\t");
            }
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }
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
