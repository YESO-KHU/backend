package api.server.common.payload.success;

import api.server.common.payload.BaseApiResponse;
import api.server.domain.article.dto.response.GetArticleListResponse;
import api.server.domain.article.dto.response.GetArticleResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ArticleSuccessApiResponse<T> extends BaseApiResponse {
    private final T response;

    public ArticleSuccessApiResponse(Boolean isSuccess, String code, String message, T response) {
        super(isSuccess, code, message);
        this.response = response;
    }

    public static ArticleSuccessApiResponse<GetArticleListResponse> getArticleList(GetArticleListResponse response){
        return new ArticleSuccessApiResponse<>(
                true,
                HttpStatus.CREATED.toString(),
                "기사 리스트 조회 성공",
                response);
    }

    public static ArticleSuccessApiResponse<GetArticleResponse> getArticle(GetArticleResponse response){
        return new ArticleSuccessApiResponse<>(
                true,
                HttpStatus.OK.toString(),
                "단일 기사 세부 조회 성공",
                response);
    }

}
