package api.server.common.payload.failure.customExceptionStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ArticleExceptionStatus {

    ARTICLE_NOT_EXIST(HttpStatus.BAD_REQUEST, "40010", "해당 기사가 존재하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}