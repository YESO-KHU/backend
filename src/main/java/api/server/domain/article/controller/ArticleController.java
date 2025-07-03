package api.server.domain.article.controller;

import api.server.common.payload.success.ArticleSuccessApiResponse;
import api.server.domain.article.dto.response.GetArticleListResponse;
import api.server.domain.article.dto.response.GetArticleResponse;
import api.server.domain.article.service.ArticleApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class ArticleController {
    private final ArticleApplicationService articleApplicationService;

    @Operation(summary = "기사 리스트 조회", description = "업로드된 기사 리스트를 조회합니다.")
    @GetMapping("/articles")
    public ArticleSuccessApiResponse<GetArticleListResponse> getArticleList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category)
    {
        log.info("[ArticleController - getArticleList]");
        return ArticleSuccessApiResponse.getArticleList(
                articleApplicationService.getArticleList(page, size, search, category)
        );
    }

    @Operation(summary = "단일 기사 세부 조회", description = "특정 기사에 대한 세부 정보를 조회합니다.")
    @GetMapping("/articles/{articleId}")
    public ArticleSuccessApiResponse<GetArticleResponse> getArticle(
            @PathVariable("articleId") Long articleId)
    {
        log.info("[ArticleController - getArticle]");

        return ArticleSuccessApiResponse.getArticle(
                articleApplicationService.getArticle(articleId)
        );
    }

}
