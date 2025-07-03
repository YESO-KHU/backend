package api.server.domain.article.repository;

import api.server.domain.article.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface  ArticleCustomRepository {

    Page<Article> getArticleListBySearch(String search, Pageable pageable);
    Page<Article> getArticleListByCategory(String category, Pageable pageable);
    Page<Article> getArticleList(String search, String category, Pageable pageable);
}
