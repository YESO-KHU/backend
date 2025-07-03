package api.server.domain.article.dto.response;

import api.server.domain.article.dto.paging.ArticleBriefDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetArticleListResponse {
    public int page;
    public int size;
    public int totalElements;
    List<ArticleBriefDto> articleList;
}
