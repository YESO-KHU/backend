package api.server.domain.scrap.service;

import api.server.domain.scrap.dto.request.ScrapArticleRequest;
import api.server.domain.scrap.dto.response.ScrapArticleResponse;
import api.server.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ScrapApplicationServiceTest {
    @Mock
    private ScrapCommandService scrapCommandService;
    @InjectMocks
    private ScrapApplicationService scrapApplicationService;

    @Test
    @DisplayName("스크랩 성공")
    void scrapArticle_success() {
        // given
        ScrapArticleRequest request = new ScrapArticleRequest();
        User user = User.builder().username("user").password("pw").role("USER").build();
        ScrapArticleResponse response = ScrapArticleResponse.builder().userId(1L).articleId(2L).memo("memo").build();
        given(scrapCommandService.scrapArticle(2L, request, user)).willReturn(response);

        // when
        ScrapArticleResponse result = scrapApplicationService.scrapArticle(2L, request, user);

        // then
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getArticleId()).isEqualTo(2L);
        assertThat(result.getMemo()).isEqualTo("memo");
    }
} 