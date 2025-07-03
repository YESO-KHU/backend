package api.server.common.payload.failure.handler;

import api.server.common.payload.failure.ExceptionApiResponse;
import api.server.common.payload.failure.customException.JWTException;
import api.server.common.payload.failure.customExceptionStatus.JWTExceptionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class JWTExceptionHandler {
    // [JWT]
    @ExceptionHandler(JWTException.TokenNullException.class)
    public ResponseEntity<ExceptionApiResponse> handleException(JWTException.TokenNullException e){
        log.error("[GlobalExceptionHandler] JWTException.TokenNullException occurred");
        return ResponseEntity
                .status(
                        JWTExceptionStatus.TOKEN_NULL.getHttpStatus()
                )
                .body(
                        new ExceptionApiResponse(
                                false, JWTExceptionStatus.TOKEN_NULL.getCode(), JWTExceptionStatus.TOKEN_NULL.getMessage()
                        )
                );
    }

    @ExceptionHandler(JWTException.TokenExpiredException.class)
    public ResponseEntity<ExceptionApiResponse> handleException(JWTException.TokenExpiredException e){
        log.error("[GlobalExceptionHandler] JWTException.TokenExpiredException occurred");
        return ResponseEntity
                .status(
                        JWTExceptionStatus.TOKEN_EXPIRED.getHttpStatus()
                )
                .body(
                        new ExceptionApiResponse(
                                false, JWTExceptionStatus.TOKEN_EXPIRED.getCode(), JWTExceptionStatus.TOKEN_EXPIRED.getMessage()
                        )
                );
    }

    @ExceptionHandler(JWTException.TokenTypeNotAccessException.class)
    public ResponseEntity<ExceptionApiResponse> handleException(JWTException.TokenTypeNotAccessException e){
        log.error("[GlobalExceptionHandler] JWTException.TokenTypeNotAccessException occurred");
        return ResponseEntity
                .status(
                        JWTExceptionStatus.TOKEN_TYPE_NOT_ACCESS.getHttpStatus()
                )
                .body(
                        new ExceptionApiResponse(
                                false, JWTExceptionStatus.TOKEN_TYPE_NOT_ACCESS.getCode(), JWTExceptionStatus.TOKEN_TYPE_NOT_ACCESS.getMessage()
                        )
                );
    }
}
