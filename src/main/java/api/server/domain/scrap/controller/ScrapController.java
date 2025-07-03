package api.server.domain.scrap.controller;

import api.server.common.annotation.CurrentUser;
import api.server.common.payload.success.ScrapSuccessApiResponse;
import api.server.domain.scrap.dto.request.ScrapArticleRequest;
import api.server.domain.scrap.dto.response.ScrapArticleResponse;
import api.server.domain.scrap.service.ScrapApplicationService;
import api.server.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class ScrapController {
    private final ScrapApplicationService scrapApplicationService;

    @Operation(summary = "단일 기사 스크랩", description = "특정 기사를 스크랩합니다.")
    @PostMapping("/articles/{articleId}/scrap")
    public ScrapSuccessApiResponse<ScrapArticleResponse> scrapArticle(
            @PathVariable("articleId") Long articleId,
            @RequestBody ScrapArticleRequest request,
            @CurrentUser User user)
    {
        log.info("[ArticleController - scrapArticle]");

        return ScrapSuccessApiResponse.scrapArticle(
                scrapApplicationService.scrapArticle(articleId, request, user)
        );
    }
}
