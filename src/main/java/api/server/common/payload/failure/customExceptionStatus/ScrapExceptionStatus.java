package api.server.common.payload.failure.customExceptionStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ScrapExceptionStatus {
    ARTICLE_ALREADY_SCRAP(HttpStatus.BAD_REQUEST, "40000", "이미 스크랩된 기사입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
