package api.server.domain.scrap.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScrapArticleResponse {
    private Long userId;
    private Long articleId;
    private String memo;
}
