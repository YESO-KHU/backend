package api.server.domain.user.service;

import api.server.domain.user.dto.request.LoginRequest;
import api.server.domain.user.dto.request.RegisterRequest;
import api.server.domain.user.dto.response.LoginResponse;
import api.server.domain.user.dto.response.RegisterResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserApplicationService {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public RegisterResponse register(RegisterRequest request){
        return userCommandService.register(request);
    }

    public LoginResponse login(LoginRequest request, HttpServletResponse httpServletResponse){
        String username = userQueryService.checkUserInfo(request);
        return userCommandService.issueToken(username, httpServletResponse);
    }

    public void reissue(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        userCommandService.reissueToken(httpServletRequest, httpServletResponse);
    }
}