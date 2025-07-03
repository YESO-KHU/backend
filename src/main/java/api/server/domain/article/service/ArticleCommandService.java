package api.server.domain.article.service;
import api.server.common.payload.failure.customException.ArticleException;
import api.server.domain.article.entity.Article;
import api.server.domain.article.entity.ArticleCategory;
import api.server.domain.article.repository.ArticleRepository;
import api.server.external.gemini.GeminiService;
import article.ArticleServiceOuterClass.ArticleItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ArticleCommandService {
    private final GeminiService geminiService;
    private final ArticleRepository articleRepository;

    public void uploadArticles(List<ArticleItem> articleItemList){
        log.info("[ArticleCommandService - uploadArticles]");

        List<Article> articleList = new ArrayList<>();

        for(ArticleItem articleItem: articleItemList){
            if (articleRepository.existsByLink(articleItem.getLink())) continue;

            Article article = Article.builder()
                    .title(articleItem.getTitle())
                    .content(articleItem.getContent())
                    .summary("")
                    .articleCategory(ArticleCategory.getArticleCategory(articleItem.getCategory()))
                    .link(articleItem.getLink())
                    .likeCount(0)
                    .viewCount(0)
                    .publishDate(LocalDateTime.parse(articleItem.getPublishDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .build();
            articleList.add(article);
        }

        articleRepository.saveAll(articleList);
    }

    public String summarizeArticleContent(Long articleId){
        log.info("[ArticleCommandService - summarizeArticleContent]");

        Article article = articleRepository.findById(articleId).orElseThrow(ArticleException.ArticleNotExistException::new);
        String summarizedArticleContent = geminiService.summarizeArticleContent(article.getContent());

        article.updateSummary(summarizedArticleContent);

        return summarizedArticleContent;

    }
}
