package api.server.domain.article.repository;

import api.server.domain.article.entity.Article;
import api.server.domain.article.entity.ArticleCategory;
import api.server.domain.article.entity.QArticle;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
public class ArticleCustomRepositoryImpl implements ArticleCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Article> getArticleListBySearch(String search, Pageable pageable) {
        QArticle article = QArticle.article;

        BooleanExpression likesSearch = article.title.likeIgnoreCase("%" + search + "%")
                .or(article.content.likeIgnoreCase("%" + search + "%"));

        List<Article> articleList = jpaQueryFactory.selectFrom(article)
                .where(likesSearch)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(
                        article.publishDate.desc()
                )
                .fetch();

        long total = Optional.ofNullable(jpaQueryFactory.select(article.count())
                .from(article)
                .where(likesSearch)
                .fetchOne()).orElse(0L);

        return new PageImpl<>(articleList, pageable, total);
    }

    @Override
    public Page<Article> getArticleListByCategory(String category, Pageable pageable) {
        QArticle article = QArticle.article;

        ArticleCategory articleCategory = ArticleCategory.getArticleCategory(category);

        List<Article> articleList = jpaQueryFactory.selectFrom(article)
                .where(article.articleCategory.eq(articleCategory))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(article.publishDate.desc())
                .fetch();

        long total = Optional.ofNullable(jpaQueryFactory.select(article.count())
                .from(article)
                .where(article.articleCategory.eq(articleCategory))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(articleList, pageable, total);
    }


    @Override
    public Page<Article> getArticleList(String search, String category, Pageable pageable) {
        QArticle article = QArticle.article;

        ArticleCategory articleCategory = ArticleCategory.getArticleCategory(category);

        BooleanExpression likesSearch = article.title.likeIgnoreCase("%" + search + "%")
                .or(article.content.likeIgnoreCase("%" + search + "%"));

        List<Article> articleList = jpaQueryFactory.selectFrom(article)
                .where(likesSearch.and(
                        article.articleCategory.eq(articleCategory)
                ))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(
                        article.publishDate.desc()
                )
                .fetch();

        long total = Optional.ofNullable(jpaQueryFactory.select(article.count())
                .from(article)
                .where(likesSearch.and(
                        article.articleCategory.eq(articleCategory)
                ))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(articleList, pageable, total);
    }

}
