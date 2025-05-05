package kuke.board.comment.api;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import kuke.board.comment.service.response.CommentPageResponse;
import kuke.board.comment.service.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

class CommentApiV2Test {
    RestClient restClient = RestClient.create("http://localhost:9001");

    @Test
    void create() {
        CommentResponse response = create(new CommentCreateRequestV2(1L, "my comment 1", null, 1L));
        CommentResponse response2 = create(new CommentCreateRequestV2(1L, "my comment 2", response.getPath(), 1L));
        CommentResponse response3 = create(new CommentCreateRequestV2(1L, "my comment 3", response2.getPath(), 1L));

        System.out.println("response.getPath() = " + response.getPath());
        System.out.println("response.getCommentId()() = " + response.getCommentId());
        System.out.println("\tresponse2.getPath() = " + response2.getPath());
        System.out.println("\tresponse2.getCommentId()() = " + response2.getCommentId());
        System.out.println("\t\tresponse3.getPath() = " + response3.getPath());
        System.out.println("\t\tresponse3.getCommentId()() = " + response3.getCommentId());

        /**
         * response.getPath() = 00003
         * response.getCommentId()() = 177321997239459840
         * 	response2.getPath() = 0000300000
         * 	response2.getCommentId()() = 177321997428203520
         * 		response3.getPath() = 000030000000000
         * 		response3.getCommentId()() = 177321997486923776
         */
    }

    CommentResponse create(CommentCreateRequestV2 request) {
        return restClient.post()
                         .uri("/v2/comments")
                         .body(request)
                         .retrieve()
                         .body(CommentResponse.class);
    }

    @Test
    void read() {
        CommentResponse response = restClient.get()
                                             .uri("/v2/comments/{commentId}", 177321997239459840L)
                                             .retrieve()
                                             .body(CommentResponse.class);
        System.out.println("response = " + response);
    }

    @Test
    void delete() {
        restClient.delete()
                  .uri("/v2/comments/{commentId}", 177321997239459840L)
                  .retrieve()
                  .toBodilessEntity();
    }

    @Test
    void readAll() {
        CommentPageResponse response = restClient.get()
                                                 .uri("/v2/comments?articleId=1&page=1&pageSize=10")
                                                 .retrieve()
                                                 .body(CommentPageResponse.class);

        System.out.println("response.getCommentCount() = " + response.getCommentCount());
        for (CommentResponse comment : response.getComments()) {
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }

        /**
         * response.getCommentCount() = 101
         * comment.getCommentId() = 177323877972721666
         * comment.getCommentId() = 177323878010470408
         * comment.getCommentId() = 177323878014664707
         * comment.getCommentId() = 177323878014664710
         * comment.getCommentId() = 177323878014664715
         * comment.getCommentId() = 177323878014664721
         * comment.getCommentId() = 177323878014664729
         * comment.getCommentId() = 177323878014664732
         * comment.getCommentId() = 177323878014664735
         * comment.getCommentId() = 177323878014664750
         */
    }

    @Test
    void readAllInfiniteScroll() {
        List<CommentResponse> responses = restClient.get()
                                                    .uri("/v2/comments/infinite-scroll?articleId=1&pageSize=5")
                                                    .retrieve()
                                                    .body(new ParameterizedTypeReference<List<CommentResponse>>() {});

        System.out.println("firstPage");
        for (CommentResponse comment : responses) {
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }

        String lastPath = responses.getLast().getPath();
        List<CommentResponse> responses2 = restClient.get()
                                                    .uri("/v2/comments/infinite-scroll?articleId=1&pageSize=5&lastPath={lastPath}", lastPath)
                                                    .retrieve()
                                                    .body(new ParameterizedTypeReference<List<CommentResponse>>() {});

        System.out.println("secondPage");
        for (CommentResponse comment : responses2) {
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }
    }

    @Test
    void countTest() {
        CommentResponse commentResponse = create(new CommentCreateRequestV2(2L, "my comment1", null, 1L));

        Long count = restClient.get()
                               .uri("/v2/comments/articles/{articleId}/count", 2L)
                               .retrieve()
                               .body(Long.class);
        System.out.println("count = " + count); // 1

        restClient.delete()
                  .uri("/v2/comments/{commentId}", commentResponse.getCommentId())
                  .retrieve()
                  .toBodilessEntity();

        Long count2 = restClient.get()
                                .uri("/v2/comments/articles/{articleId}/count", 2L)
                                .retrieve()
                                .body(Long.class);
        System.out.println("count2 = " + count2); // 0
    }

    @Getter
    @AllArgsConstructor
    static class CommentCreateRequestV2 {
        private Long articleId;
        private String content;
        private String parentPath;
        private Long writerId;
    }
}
