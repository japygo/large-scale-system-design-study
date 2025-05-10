package kuke.board.articleread.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import kuke.board.articleread.service.ArticleReadService;
import kuke.board.articleread.service.response.ArticleReadResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ArticleReadController {
    private final ArticleReadService articleReadService;

    @GetMapping("/v1/articles/{articleId}")
    public ArticleReadResponse read(@PathVariable Long articleId) {
        return articleReadService.read(articleId);
    }
}
