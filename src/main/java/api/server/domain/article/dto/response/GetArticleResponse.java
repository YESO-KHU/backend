package api.server.domain.article.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class GetArticleResponse {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private String articleCategory;
    private String link;
    private int likeCount;
    private int viewCount;
    private LocalDateTime publishDate;
}
