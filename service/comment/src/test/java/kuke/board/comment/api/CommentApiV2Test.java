package kuke.board.comment.api;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

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

    @Getter
    @AllArgsConstructor
    static class CommentCreateRequestV2 {
        private Long articleId;
        private String content;
        private String parentPath;
        private Long writerId;
    }
}
