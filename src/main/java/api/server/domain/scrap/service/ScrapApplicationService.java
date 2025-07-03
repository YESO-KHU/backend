package api.server.domain.scrap.service;

import api.server.domain.scrap.dto.request.ScrapArticleRequest;
import api.server.domain.scrap.dto.response.ScrapArticleResponse;
import api.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScrapApplicationService {
    private final ScrapCommandService scrapCommandService;

    public ScrapArticleResponse scrapArticle(Long articleId, ScrapArticleRequest request, User user){
        log.info("[ScrapApplicationService - scrapArticle]");

        return scrapCommandService.scrapArticle(articleId, request, user);
    }
}
