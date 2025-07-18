package api.server.domain.article.repository;

import api.server.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleCustomRepository {
    boolean existsByLink(String link);
}
