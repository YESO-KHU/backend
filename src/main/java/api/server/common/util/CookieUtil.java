package api.server.common.util;

import org.springframework.http.ResponseCookie;

import java.time.Duration;

public class CookieUtil {
    public static String getResponseCookie(String refreshToken){
        return ResponseCookie.from("refresh", refreshToken)
                .httpOnly(true)  // JavaScript에서 접근
                .secure(false)    // HTTPS에서만 전송
                .sameSite("None") // CORS 환경에서 허용
                .path("/")
                .maxAge(Duration.ofDays(1))
                .build().toString();
    }
}
