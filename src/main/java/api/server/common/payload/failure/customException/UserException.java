package api.server.common.payload.failure.customException;

public class UserException {
    public static class UsernameAlreadyExistException extends RuntimeException{}
    public static class PasswordNotValidException extends RuntimeException{}
    public static class TokenNotValidException extends RuntimeException{}
    public static class UsernameNotExistException extends RuntimeException{}
}
