package kuke.board.articleread.repository;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardArticleCountRepository {
    private final StringRedisTemplate redisTemplate;

    // article-read::board-article-count::board::{boardId}
    private static final String KEY_FORMAT = "article-read::board-article-count::board::%s";

    public void createOrUpdate(Long boardId, Long articleCount) {
        redisTemplate.opsForValue().set(generateKey(boardId), String.valueOf(articleCount));
    }

    public Long read(Long boardId) {
        String result = redisTemplate.opsForValue().get(generateKey(boardId));
        return result == null ? 0L : Long.parseLong(result);
    }

    private String generateKey(Long boardId) {
        return KEY_FORMAT.formatted(boardId);
    }
}
