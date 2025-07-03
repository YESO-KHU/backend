package api.server.domain.user.service;

import api.server.common.payload.failure.customException.UserException;
import api.server.common.util.CookieUtil;
import api.server.common.util.JWTUtil;
import api.server.domain.user.dto.request.RegisterRequest;
import api.server.domain.user.dto.response.LoginResponse;
import api.server.domain.user.dto.response.RegisterResponse;
import api.server.domain.user.entity.User;
import api.server.domain.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserCommandService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final StringRedisTemplate stringRedisTemplate;

    public RegisterResponse register(RegisterRequest request){
        log.info("[UserCommandService - register]");

        // username 중복확인
        if(userRepository.existsByUsername(request.getUsername())){
            throw new UserException.UsernameAlreadyExistException();
        }

        // 비밀번호 encryption 후 save
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_USER")
                .build();

        userRepository.save(user);

        return RegisterResponse.builder()
                .username(request.getUsername())
                .build();
    }

    public LoginResponse issueToken(String username, HttpServletResponse httpServletResponse){
        log.info("[UserCommandService - issueToken]");
        User user = userRepository.findByUsername(username).orElseThrow(UserException.UsernameNotExistException::new);

        String accessToken = jwtUtil.createJwt(user.getId(),"access", user.getUsername(), user.getRole(), 60 * 60 * 1000L); // 1시간
        String refreshToken = jwtUtil.createJwt(user.getId(),"refresh", user.getUsername(), user.getRole(), 24 * 60 * 60 * 1000L); // 1일

        String redisRefreshKey = "refresh:userId:" + user.getId();
        stringRedisTemplate.opsForValue().set(redisRefreshKey, refreshToken, 1, TimeUnit.DAYS);

        httpServletResponse.setHeader("Authorization", "Bearer " + accessToken);
        httpServletResponse.setHeader("Set-Cookie", CookieUtil.getResponseCookie(refreshToken));


        return LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .build();
    }

    public void reissueToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        log.info("[UserCommandService - reissueToken]");
        // cookie refresh 추출
        String refreshToken = null;
        Cookie[] cookies = httpServletRequest.getCookies();

        for (Cookie cookie : cookies){
            if (cookie.getName().equals("refresh")){
                refreshToken = cookie.getValue();
            }
        }
        // redis refresh 추출
        User user = userRepository.findByUsername(jwtUtil.getUsername(refreshToken)).orElseThrow(UserException.PasswordNotValidException::new);
        String redisRefreshKey = "refresh:userId:" + user.getId();
        String redisRefreshValue = stringRedisTemplate.opsForValue().get(redisRefreshKey);

        // refresh 유효성 검증
        if (!jwtUtil.isValidRefreshToken(refreshToken, redisRefreshValue)){
            throw new UserException.TokenNotValidException();
        }

        // redis refresh 삭제
        stringRedisTemplate.delete(redisRefreshKey);

        // access, refresh 모두 재발급
        String newAccessToken = jwtUtil.createJwt(user.getId(),"access", user.getUsername(), jwtUtil.getRole(refreshToken), 60 * 60 * 1000L);
        String newRefreshToken = jwtUtil.createJwt(user.getId(),"refresh", user.getUsername(), jwtUtil.getRole(refreshToken), 24 * 60 * 60 * 1000L);

        // new refresh 1 day 유지
        stringRedisTemplate.opsForValue().set(redisRefreshKey, newRefreshToken, 1, TimeUnit.DAYS);

        httpServletResponse.setHeader("Authorization", "Bearer " + newAccessToken);
        httpServletResponse.setHeader("Set-Cookie", CookieUtil.getResponseCookie(refreshToken));
    }

}
