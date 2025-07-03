package api.server.common.payload.success;

import api.server.common.payload.BaseApiResponse;
import api.server.domain.scrap.dto.response.ScrapArticleResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ScrapSuccessApiResponse<T> extends BaseApiResponse {
    private final T response;

    public ScrapSuccessApiResponse(Boolean isSuccess, String code, String message, T response) {
        super(isSuccess, code, message);
        this.response = response;
    }

    public static ScrapSuccessApiResponse<ScrapArticleResponse> scrapArticle(ScrapArticleResponse response){
        return new ScrapSuccessApiResponse<>(
                true,
                HttpStatus.OK.toString(),
                "단일 기사 스크랩 성공",
                response);
    }
}