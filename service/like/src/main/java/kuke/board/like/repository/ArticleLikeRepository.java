package kuke.board.like.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kuke.board.like.entity.ArticleLike;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
    Optional<ArticleLike> findByArticleIdAndUserId(Long articleId, Long userId);

    @Query(
        value = "delete from article_like where article_id = :articleId and user_id = :userId",
        nativeQuery = true
    )
    @Modifying
    int deleteByArticleIdAndUserId(Long articleId, Long userId);
}
