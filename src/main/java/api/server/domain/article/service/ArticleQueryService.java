package api.server.domain.article.service;

import api.server.common.payload.failure.customException.ArticleException;
import api.server.domain.article.dto.paging.ArticleBriefDto;
import api.server.domain.article.dto.response.GetArticleListResponse;
import api.server.domain.article.dto.response.GetArticleResponse;
import api.server.domain.article.entity.Article;
import api.server.domain.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleQueryService {

    private final ArticleRepository articleRepository;

    public GetArticleListResponse getArticleList(int page, int size, String search, String category){
        log.info("[ArticleQueryService - getArticleList]");

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "publishDate"));
        Page<Article> pagingArticles;

        if ((search == null || search.trim().isEmpty()) && (category == null || category.trim().isEmpty())) {
            // 1. search, hashtag 둘 다 없을 때 → findAll
            pagingArticles = articleRepository.findAll(pageable);
        } else if (search != null && !search.trim().isEmpty() && (category == null || category.trim().isEmpty())) {
            // 2. search만 있을 때
            pagingArticles = articleRepository.getArticleListBySearch(search, pageable);
        } else if ((search == null || search.trim().isEmpty()) && category != null && !category.trim().isEmpty()) {
            // 3. category만 있을 때
            pagingArticles = articleRepository.getArticleListByCategory(category, pageable);
        } else {
            // 4. search와 category 둘 다 있을 때
            pagingArticles = articleRepository.getArticleList(search, category, pageable);
        }

        List<ArticleBriefDto> pagingBriefArticles = pagingArticles.getContent().stream()
                .map(ArticleBriefDto::new)
                .toList();

        return GetArticleListResponse.builder()
                .page(page)
                .size(size)
                .totalElements((int) pagingArticles.getTotalElements())
                .articleList(pagingBriefArticles)
                .build();
    }

    public GetArticleResponse getArticle(Long articleId){
        log.info("[ArticleQueryService - getArticle]");

        Article article = articleRepository.findById(articleId).orElseThrow(ArticleException.ArticleNotExistException::new);

        return GetArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .summary(article.getSummary())
                .articleCategory(article.getArticleCategory().getDisplayName())
                .link(article.getLink())
                .likeCount(article.getLikeCount())
                .viewCount(article.getViewCount())
                .publishDate(article.getPublishDate())
                .build();
    }
}
