package api.server.common.payload.failure.handler;

import api.server.common.payload.BaseApiResponse;
import api.server.common.payload.failure.ExceptionApiResponse;
import api.server.common.payload.failure.customException.UserException;
import api.server.common.payload.failure.customExceptionStatus.UserExceptionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserException.UsernameAlreadyExistException.class)
    public ResponseEntity<BaseApiResponse> handleException(UserException.UsernameAlreadyExistException e){
        log.error("[GlobalExceptionHandler] UserException.UsernameAlreadyExistException occurred");
        return ResponseEntity
                .status(
                        UserExceptionStatus.USERNAME_ALREADY_EXIST.getHttpStatus()
                )
                .body(
                        new ExceptionApiResponse(
                                false, UserExceptionStatus.USERNAME_ALREADY_EXIST.getCode(), UserExceptionStatus.USERNAME_ALREADY_EXIST.getMessage()
                        )
                );
    }

    @ExceptionHandler(UserException.PasswordNotValidException.class)
    public ResponseEntity<BaseApiResponse> handleException(UserException.PasswordNotValidException e){
        log.error("[GlobalExceptionHandler] UserException.PasswordNotValidException occurred");
        return ResponseEntity
                .status(
                        UserExceptionStatus.PASSWORD_NOT_VALID.getHttpStatus()
                )
                .body(
                        new ExceptionApiResponse(
                                false, UserExceptionStatus.PASSWORD_NOT_VALID.getCode(), UserExceptionStatus.PASSWORD_NOT_VALID.getMessage()
                        )
                );
    }

    @ExceptionHandler(UserException.TokenNotValidException.class)
    public ResponseEntity<BaseApiResponse> handleException(UserException.TokenNotValidException e){
        log.error("[GlobalExceptionHandler] UserException.TokenNotValidException occurred");
        return ResponseEntity
                .status(
                        UserExceptionStatus.TOKEN_NOT_VALID.getHttpStatus()
                )
                .body(
                        new ExceptionApiResponse(
                                false, UserExceptionStatus.TOKEN_NOT_VALID.getCode(), UserExceptionStatus.TOKEN_NOT_VALID.getMessage()
                        )
                );
    }

    @ExceptionHandler(UserException.UsernameNotExistException.class)
    public ResponseEntity<BaseApiResponse> handleException(UserException.UsernameNotExistException e){
        log.error("[GlobalExceptionHandler] UserException.UsernameNotExistException occurred");
        return ResponseEntity
                .status(
                        UserExceptionStatus.USERNAME_NOT_EXIST.getHttpStatus()
                )
                .body(
                        new ExceptionApiResponse(
                                false, UserExceptionStatus.USERNAME_NOT_EXIST.getCode(), UserExceptionStatus.USERNAME_NOT_EXIST.getMessage()
                        )
                );
    }



}