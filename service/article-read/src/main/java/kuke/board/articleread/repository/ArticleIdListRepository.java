package kuke.board.articleread.repository;

import java.util.List;

import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.Limit;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ArticleIdListRepository {
    private final StringRedisTemplate redisTemplate;

    // article-read::board::{boardId}::article-list
    private static final String KEY_FORMAT = "article-read::board::%s::article-list";

    public void add(Long boardId, Long articleId, Long limit) {
        redisTemplate.executePipelined((RedisCallback<?>)action -> {
            StringRedisConnection conn = (StringRedisConnection)action;
            String key = generateKey(boardId);
            conn.zAdd(key, 0, toPaddedString(articleId));
            conn.zRemRangeByScore(key, 0, -limit - 1);
            return null;
        });
    }

    public void delete(Long boardId, Long articleId) {
        redisTemplate.opsForZSet().remove(generateKey(boardId), toPaddedString(articleId));
    }

    public List<Long> readAll(Long boardId, Long offset, Long limit) {
        return redisTemplate.opsForZSet()
                            .reverseRange(generateKey(boardId), offset, offset + limit - 1)
                            .stream().map(Long::valueOf).toList();
    }

    public List<Long> readAllInfiniteScroll(Long boardId, Long lastArticleId, Long limit) {
        return redisTemplate.opsForZSet().reverseRangeByLex(
            generateKey(boardId),
            lastArticleId == null ?
            Range.unbounded() :
            Range.leftUnbounded(Range.Bound.exclusive(toPaddedString(lastArticleId))),
            Limit.limit().count(limit.intValue())
        ).stream().map(Long::valueOf).toList();
    }

    private String toPaddedString(Long articleId) {
        return String.format("%019d", articleId);
        // 1234 -> 00000000000000012234
    }

    private String generateKey(Long boardId) {
        return KEY_FORMAT.formatted(boardId);
    }
}
