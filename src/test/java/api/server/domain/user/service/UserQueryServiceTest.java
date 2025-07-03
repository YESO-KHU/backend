package api.server.domain.user.service;

import api.server.common.payload.failure.customException.UserException;
import api.server.domain.user.dto.request.LoginRequest;
import api.server.domain.user.entity.User;
import api.server.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserQueryService userQueryService;

    @Test
    @DisplayName("정상 로그인 시 username 반환")
    void checkUserInfo_success() {
        // given
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        User user = User.builder().username("testuser").password("encodedPassword").role("USER").build();
        given(userRepository.findByUsername("testuser")).willReturn(Optional.of(user));
        given(passwordEncoder.matches("password", "encodedPassword")).willReturn(true);

        // when
        String result = userQueryService.checkUserInfo(request);

        // then
        assertThat(result).isEqualTo("testuser");
    }

    @Test
    @DisplayName("존재하지 않는 아이디로 로그인 시 예외 발생")
    void checkUserInfo_usernameNotExist() {
        // given
        LoginRequest request = new LoginRequest();
        request.setUsername("notfound");
        request.setPassword("password");
        given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userQueryService.checkUserInfo(request))
                .isInstanceOf(UserException.UsernameNotExistException.class);
    }

    @Test
    @DisplayName("비밀번호 불일치 시 예외 발생")
    void checkUserInfo_passwordNotValid() {
        // given
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("wrongpassword");
        User user = User.builder().username("testuser").password("encodedPassword").role("USER").build();
        given(userRepository.findByUsername("testuser")).willReturn(Optional.of(user));
        given(passwordEncoder.matches("wrongpassword", "encodedPassword")).willReturn(false);

        // when & then
        assertThatThrownBy(() -> userQueryService.checkUserInfo(request))
                .isInstanceOf(UserException.PasswordNotValidException.class);
    }
} 