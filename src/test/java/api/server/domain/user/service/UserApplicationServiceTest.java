package api.server.domain.user.service;

import api.server.domain.user.dto.request.LoginRequest;
import api.server.domain.user.dto.request.RegisterRequest;
import api.server.domain.user.dto.response.LoginResponse;
import api.server.domain.user.dto.response.RegisterResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserApplicationServiceTest {
    @Mock
    private UserCommandService userCommandService;
    @Mock
    private UserQueryService userQueryService;
    @InjectMocks
    private UserApplicationService userApplicationService;

    @Test
    @DisplayName("회원가입 성공")
    void register_success() {
        // given
        RegisterRequest request = new RegisterRequest();
        RegisterResponse response = RegisterResponse.builder().username("testuser").build();
        given(userCommandService.register(request)).willReturn(response);

        // when
        RegisterResponse result = userApplicationService.register(request);

        // then
        assertThat(result.getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() {
        // given
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        MockHttpServletResponse response = new MockHttpServletResponse();
        given(userQueryService.checkUserInfo(request)).willReturn("testuser");
        LoginResponse loginResponse = LoginResponse.builder().userId(1L).username("testuser").build();
        given(userCommandService.issueToken("testuser", response)).willReturn(loginResponse);

        // when
        LoginResponse result = userApplicationService.login(request, response);

        // then
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void reissue_success() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        userApplicationService.reissue(request, response);

        // then
        verify(userCommandService).reissueToken(request, response);
    }
} 