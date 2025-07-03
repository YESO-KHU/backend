package api.server.domain.article.dto.paging;

import api.server.domain.article.entity.Article;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleBriefDto {
    private Long id;
    private String title;
    private LocalDateTime publishDate;

    public ArticleBriefDto(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.publishDate = article.getPublishDate();
    }
}
