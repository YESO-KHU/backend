package api.server.common.payload.success;

import api.server.common.payload.BaseApiResponse;
import api.server.domain.user.dto.response.LoginResponse;
import api.server.domain.user.dto.response.RegisterResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserSuccessApiResponse<T> extends BaseApiResponse {
    private final T response;

    public UserSuccessApiResponse(Boolean isSuccess, String code, String message, T response) {
        super(isSuccess, code, message);
        this.response = response;
    }

    public static UserSuccessApiResponse<RegisterResponse> register(RegisterResponse response){
        return new UserSuccessApiResponse<>(
                true,
                HttpStatus.CREATED.toString(),
                "회원가입 성공",
                response);
    }

    public static UserSuccessApiResponse<LoginResponse> login(LoginResponse response){
        return new UserSuccessApiResponse<>(
                true,
                HttpStatus.OK.toString(),
                "로그인 성공",
                response);
    }

    public static UserSuccessApiResponse<Void> reissue(){
        return new UserSuccessApiResponse<>(
                true,
                HttpStatus.OK.toString(),
                "토큰 재발급 성공",
                null);
    }
}
