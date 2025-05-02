package kuke.board.articleread.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kuke.board.articleread.entity.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

}
