package api.server.domain.article.service;

import api.server.domain.article.dto.paging.ArticleBriefDto;
import api.server.domain.article.dto.response.GetArticleListResponse;
import api.server.domain.article.dto.response.GetArticleResponse;
import api.server.domain.article.entity.Article;
import api.server.domain.article.entity.ArticleCategory;
import api.server.domain.article.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ArticleQueryServiceTest {
    @Mock
    private ArticleRepository articleRepository;
    @InjectMocks
    private ArticleQueryService articleQueryService;

    @Test
    @DisplayName("게시글 리스트 조회 성공")
    void getArticleList_success() {
        // given
        Article article = Article.builder()
                .title("title")
                .content("content")
                .summary("")
                .articleCategory(ArticleCategory.POLITICS)
                .link("link")
                .likeCount(0)
                .viewCount(0)
                .publishDate(LocalDateTime.now())
                .build();
        Page<Article> page = new PageImpl<>(List.of(article));
        given(articleRepository.findAll(any(Pageable.class))).willReturn(page);

        // when
        GetArticleListResponse response = articleQueryService.getArticleList(1, 10, null, null);

        // then
        assertThat(response.getArticleList()).hasSize(1);
        assertThat(response.getArticleList().get(0).getTitle()).isEqualTo("title");
    }

    @Test
    @DisplayName("게시글 단건 조회 성공")
    void getArticle_success() {
        // given
        Article article = Article.builder()
                .title("title")
                .content("content")
                .summary("summary")
                .articleCategory(ArticleCategory.POLITICS)
                .link("link")
                .likeCount(0)
                .viewCount(0)
                .publishDate(LocalDateTime.now())
                .build();
        given(articleRepository.findById(anyLong())).willReturn(Optional.of(article));

        // when
        GetArticleResponse response = articleQueryService.getArticle(1L);

        // then
        assertThat(response.getTitle()).isEqualTo("title");
        assertThat(response.getSummary()).isEqualTo("summary");
    }
} 