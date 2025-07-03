package api.server.common.payload.failure.customException;

public class JWTException {
    public static class TokenNullException extends RuntimeException{}
    public static class TokenExpiredException extends RuntimeException{}
    public static class TokenTypeNotAccessException extends RuntimeException{}

}
