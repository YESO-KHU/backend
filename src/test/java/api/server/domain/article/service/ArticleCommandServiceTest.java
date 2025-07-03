package api.server.domain.article.service;

import api.server.domain.article.entity.Article;
import api.server.domain.article.entity.ArticleCategory;
import api.server.domain.article.repository.ArticleRepository;
import api.server.external.gemini.GeminiService;
import article.ArticleServiceOuterClass.ArticleItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArticleCommandServiceTest {
    @Mock
    private GeminiService geminiService;
    @Mock
    private ArticleRepository articleRepository;
    @InjectMocks
    private ArticleCommandService articleCommandService;

    @Test
    @DisplayName("여러 게시글 업로드 성공")
    void uploadArticles_success() {
        // given
        ArticleItem item = ArticleItem.newBuilder()
                .setTitle("title")
                .setContent("content")
                .setCategory("POLITICS")
                .setLink("link")
                .setPublishDate(LocalDateTime.now().toString())
                .build();
        given(articleRepository.existsByLink(anyString())).willReturn(false);

        // when
        articleCommandService.uploadArticles(List.of(item));

        // then
        verify(articleRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("게시글 요약 성공")
    void summarizeArticleContent_success() {
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
        given(articleRepository.findById(anyLong())).willReturn(Optional.of(article));
        given(geminiService.summarizeArticleContent("content")).willReturn("summary");

        // when
        String summary = articleCommandService.summarizeArticleContent(1L);

        // then
        assertThat(summary).isEqualTo("summary");
        assertThat(article.getSummary()).isEqualTo("summary");
    }
} 