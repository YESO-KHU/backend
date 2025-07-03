package api.server.domain.scrap.service;

import api.server.domain.article.entity.Article;
import api.server.domain.article.entity.ArticleCategory;
import api.server.domain.article.repository.ArticleRepository;
import api.server.domain.scrap.dto.request.ScrapArticleRequest;
import api.server.domain.scrap.dto.response.ScrapArticleResponse;
import api.server.domain.scrap.entity.Scrap;
import api.server.domain.scrap.repository.ScrapRepository;
import api.server.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ScrapCommandServiceTest {
    @Mock
    private ScrapRepository scrapRepository;
    @Mock
    private ArticleRepository articleRepository;
    @InjectMocks
    private ScrapCommandService scrapCommandService;

    @Test
    @DisplayName("스크랩 성공")
    void scrapArticle_success() {
        // given
        User user = User.builder().username("user").password("pw").role("USER").build();
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
        ScrapArticleRequest request = new ScrapArticleRequest();
        request.setMemo("memo");
        given(articleRepository.findById(anyLong())).willReturn(Optional.of(article));
        given(scrapRepository.existsByUserAndArticle(user, article)).willReturn(false);
        Scrap scrap = Scrap.builder().user(user).article(article).memo("memo").createdAt(LocalDateTime.now()).build();
        given(scrapRepository.save(any(Scrap.class))).willReturn(scrap);

        // when
        ScrapArticleResponse response = scrapCommandService.scrapArticle(1L, request, user);

        // then
        assertThat(response.getUserId()).isEqualTo(user.getId());
        assertThat(response.getArticleId()).isEqualTo(article.getId());
        assertThat(response.getMemo()).isEqualTo("memo");
    }
} 