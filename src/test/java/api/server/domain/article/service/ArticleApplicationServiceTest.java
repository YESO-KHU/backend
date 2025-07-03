package api.server.domain.article.service;

import api.server.domain.article.dto.response.GetArticleListResponse;
import api.server.domain.article.dto.response.GetArticleResponse;
import api.server.external.grpc.GrpcService;
import article.ArticleServiceOuterClass.ArticleItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeastOnce;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArticleApplicationServiceTest {
    @Mock
    private GrpcService grpcService;
    @Mock
    private ArticleCommandService articleCommandService;
    @Mock
    private ArticleQueryService articleQueryService;
    @InjectMocks
    private ArticleApplicationService articleApplicationService;

    @Test
    @DisplayName("게시글 리스트 조회 성공")
    void getArticleList_success() {
        // given
        GetArticleListResponse response = GetArticleListResponse.builder().page(1).size(10).totalElements(1).articleList(List.of()).build();
        given(articleQueryService.getArticleList(anyInt(), anyInt(), any(), any())).willReturn(response);

        // when
        GetArticleListResponse result = articleApplicationService.getArticleList(1, 10, null, null);

        // then
        assertThat(result.getPage()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("게시글 단건 조회 성공 (요약 없는 경우)")
    void getArticle_success_noSummary() {
        // given
        GetArticleResponse response = GetArticleResponse.builder().id(1L).title("title").summary("").build();
        given(articleQueryService.getArticle(anyLong())).willReturn(response);
        given(articleCommandService.summarizeArticleContent(anyLong())).willReturn("summary");

        // when
        GetArticleResponse result = articleApplicationService.getArticle(1L);

        // then
        assertThat(result.getSummary()).isEqualTo("summary");
    }

    @Test
    @DisplayName("게시글 단건 조회 성공 (요약 있는 경우)")
    void getArticle_success_withSummary() {
        // given
        GetArticleResponse response = GetArticleResponse.builder().id(1L).title("title").summary("summary").build();
        given(articleQueryService.getArticle(anyLong())).willReturn(response);

        // when
        GetArticleResponse result = articleApplicationService.getArticle(1L);

        // then
        assertThat(result.getSummary()).isEqualTo("summary");
    }

    @Test
    @DisplayName("게시글 업로드 스케줄러 정상 동작")
    void uploadArticles_success() {
        // given
        CompletableFuture<List<ArticleItem>> future = CompletableFuture.completedFuture(null);
        given(grpcService.getArticleItemsAsync(anyString(), anyInt(), anyInt())).willReturn(future);

        // when
        articleApplicationService.uploadArticles();

        // then
        verify(grpcService, atLeastOnce()).getArticleItemsAsync(anyString(), anyInt(), anyInt());
    }
} 