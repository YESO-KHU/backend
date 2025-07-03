package api.server.common.payload.failure.handler;

import api.server.common.payload.BaseApiResponse;
import api.server.common.payload.failure.ExceptionApiResponse;
import api.server.common.payload.failure.customException.ScrapException;
import api.server.common.payload.failure.customExceptionStatus.ScrapExceptionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ScrapExceptionHandler {

    @ExceptionHandler(ScrapException.ArticleAlreadyScrap.class)
    public ResponseEntity<BaseApiResponse> handleException(ScrapException.ArticleAlreadyScrap e){
        log.error("[GlobalExceptionHandler] ScrapException.ArticleAlreadyScrap occurred");
        return ResponseEntity
                .status(
                        ScrapExceptionStatus.ARTICLE_ALREADY_SCRAP.getHttpStatus()
                )
                .body(
                        new ExceptionApiResponse(
                                false, ScrapExceptionStatus.ARTICLE_ALREADY_SCRAP.getCode(), ScrapExceptionStatus.ARTICLE_ALREADY_SCRAP.getMessage()
                        )
                );
    }

}