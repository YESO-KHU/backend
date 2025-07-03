package api.server.common.payload.failure.handler;

import api.server.common.payload.BaseApiResponse;
import api.server.common.payload.failure.ExceptionApiResponse;
import api.server.common.payload.failure.customException.ArticleException;
import api.server.common.payload.failure.customExceptionStatus.ArticleExceptionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class ArticleExceptionHandler {

    @ExceptionHandler(ArticleException.ArticleNotExistException.class)
    public ResponseEntity<BaseApiResponse> handleException(ArticleException.ArticleNotExistException e){
        log.error("[GlobalExceptionHandler] ArticleException.ArticleNotExistException occurred");
        return ResponseEntity
                .status(
                        ArticleExceptionStatus.ARTICLE_NOT_EXIST.getHttpStatus()
                )
                .body(
                        new ExceptionApiResponse(
                                false, ArticleExceptionStatus.ARTICLE_NOT_EXIST.getCode(), ArticleExceptionStatus.ARTICLE_NOT_EXIST.getMessage()
                        )
                );
    }

}