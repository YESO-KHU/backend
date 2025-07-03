package api.server.domain.scrap.service;

import api.server.common.payload.failure.customException.ArticleException;
import api.server.common.payload.failure.customException.ScrapException;
import api.server.domain.scrap.dto.request.ScrapArticleRequest;
import api.server.domain.scrap.dto.response.ScrapArticleResponse;
import api.server.domain.article.entity.Article;
import api.server.domain.article.repository.ArticleRepository;
import api.server.domain.scrap.entity.Scrap;
import api.server.domain.scrap.repository.ScrapRepository;
import api.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ScrapCommandService {
    private final ScrapRepository scrapRepository;
    private final ArticleRepository articleRepository;

    public ScrapArticleResponse scrapArticle(Long articleId, ScrapArticleRequest request, User user){
        log.info("[ScrapCommandService - scrapArticle]");

        Article article = articleRepository.findById(articleId).orElseThrow(ArticleException.ArticleNotExistException::new);

        if (scrapRepository.existsByUserAndArticle(user, article)) {
            throw new ScrapException.ArticleAlreadyScrap();
        }

        Scrap newScrap = Scrap.builder()
                .user(user)
                .article(article)
                .memo(request.getMemo())
                .createdAt(LocalDateTime.now())
                .build();

        scrapRepository.save(newScrap);

        return ScrapArticleResponse.builder()
                .userId(newScrap.getUser().getId())
                .articleId(newScrap.getArticle().getId())
                .memo(newScrap.getMemo())
                .build();


    }
}
