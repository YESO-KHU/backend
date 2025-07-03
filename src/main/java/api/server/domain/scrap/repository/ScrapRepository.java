package api.server.domain.scrap.repository;

import api.server.domain.article.entity.Article;
import api.server.domain.scrap.entity.Scrap;
import api.server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    boolean existsByUserAndArticle(User user, Article article);
}
