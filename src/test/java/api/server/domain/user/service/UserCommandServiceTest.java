package api.server.domain.user.service;

import api.server.common.util.JWTUtil;
import api.server.domain.user.dto.request.RegisterRequest;
import api.server.domain.user.dto.response.LoginResponse;
import api.server.domain.user.dto.response.RegisterResponse;
import api.server.domain.user.entity.User;
import api.server.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.servlet.http.Cookie;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JWTUtil jwtUtil;
    @Mock
    private StringRedisTemplate stringRedisTemplate;
    @Mock
    private ValueOperations<String, String> valueOperations;
    @InjectMocks
    private UserCommandService userCommandService;

    @Test
    @DisplayName("회원가입 성공")
    void register_success() {
        // given
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        given(userRepository.existsByUsername("testuser")).willReturn(false);
        given(passwordEncoder.encode("password")).willReturn("encodedPassword");
        User user = User.builder().username("testuser").password("encodedPassword").role("ROLE_USER").build();
        given(userRepository.save(any(User.class))).willReturn(user);

        // when
        RegisterResponse response = userCommandService.register(request);

        // then
        assertThat(response.getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("토큰 발급 성공")
    void issueToken_success() throws Exception {
        // given
        User user = User.builder().username("testuser").password("encodedPassword").role("ROLE_USER").build();
        java.lang.reflect.Field idField = User.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, 1L);
        given(userRepository.findByUsername("testuser")).willReturn(Optional.of(user));
        given(jwtUtil.createJwt(eq(1L), eq("access"), eq("testuser"), eq("ROLE_USER"), anyLong())).willReturn("accessToken");
        given(jwtUtil.createJwt(eq(1L), eq("refresh"), eq("testuser"), eq("ROLE_USER"), anyLong())).willReturn("refreshToken");
        MockHttpServletResponse response = new MockHttpServletResponse();
        given(stringRedisTemplate.opsForValue()).willReturn(valueOperations);
        doNothing().when(valueOperations).set(anyString(), anyString(), anyLong(), any());

        // when
        LoginResponse loginResponse = userCommandService.issueToken("testuser", response);

        // then
        assertThat(loginResponse.getUserId()).isEqualTo(1L);
        assertThat(loginResponse.getUsername()).isEqualTo("testuser");
        assertThat(response.getHeader("Authorization")).contains("Bearer accessToken");
        assertThat(response.getHeader("Set-Cookie")).contains("refreshToken");
    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void reissueToken_success() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Cookie refreshCookie = new Cookie("refresh", "refreshToken");
        request.setCookies(refreshCookie);
        User user = User.builder().username("testuser").password("encodedPassword").role("USER").build();
        java.lang.reflect.Field idField = User.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, 1L);
        given(jwtUtil.getUsername("refreshToken")).willReturn("testuser");
        given(userRepository.findByUsername("testuser")).willReturn(Optional.of(user));
        given(stringRedisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get("refresh:userId:1")).willReturn("refreshToken");
        given(jwtUtil.isValidRefreshToken("refreshToken", "refreshToken")).willReturn(true);
        given(jwtUtil.getRole("refreshToken")).willReturn("ROLE_USER");
        given(jwtUtil.createJwt(eq(1L), eq("access"), eq("testuser"), eq("ROLE_USER"), anyLong())).willReturn("newAccessToken");
        given(jwtUtil.createJwt(eq(1L), eq("refresh"), eq("testuser"), eq("ROLE_USER"), anyLong())).willReturn("newRefreshToken");
        doNothing().when(valueOperations).set(anyString(), anyString(), anyLong(), any());

        // when
        userCommandService.reissueToken(request, response);

        // then
        assertThat(response.getHeader("Authorization")).contains("Bearer newAccessToken");
        assertThat(response.getHeader("Set-Cookie")).contains("refreshToken");
        verify(stringRedisTemplate).delete("refresh:userId:1");
        verify(valueOperations).set(eq("refresh:userId:1"), eq("newRefreshToken"), eq(1L), eq(TimeUnit.DAYS));
    }
} 