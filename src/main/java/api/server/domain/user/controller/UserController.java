package api.server.domain.user.controller;

import api.server.common.payload.success.UserSuccessApiResponse;
import api.server.domain.user.dto.request.LoginRequest;
import api.server.domain.user.dto.request.RegisterRequest;
import api.server.domain.user.dto.response.LoginResponse;
import api.server.domain.user.dto.response.RegisterResponse;
import api.server.domain.user.service.UserApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class UserController {

    private final UserApplicationService userApplicationService;

    @PostMapping("/register")
    public UserSuccessApiResponse<RegisterResponse> register(
            @RequestBody RegisterRequest request)
    {
        log.info("[POST - /api/register] request = {}", request);

        return UserSuccessApiResponse.register(
                userApplicationService.register(request)
        );
    }

    @PostMapping("/login")
    public UserSuccessApiResponse<LoginResponse> register(
            @RequestBody LoginRequest request,
            HttpServletResponse httpServletResponse)
    {
        log.info("[POST - /api/login] request = {}", request);

        return UserSuccessApiResponse.login(
                userApplicationService.login(request, httpServletResponse)
        );
    }

    @PostMapping("/reissue")
    public UserSuccessApiResponse<Void> reissue(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse)
    {
        log.info("[POST - /api/reissue]");

        userApplicationService.reissue(httpServletRequest, httpServletResponse);
        return UserSuccessApiResponse.reissue();
    }
}
