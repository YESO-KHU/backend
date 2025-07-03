package api.server.common.filter;

import api.server.common.payload.failure.customException.JWTException;
import api.server.common.util.JWTUtil;
import api.server.domain.user.dto.userDetails.RoleUserDetails;
import api.server.domain.user.dto.userDetails.UserAccessDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private static final List<String> whiteList = Arrays.asList(
            "/h2", "/favicon", "/api/health", "/api/register", "/api/login", "/api/reissue", "/swagger-ui", "/swagger-ui.html", "/v3/api-docs", "/webjars/");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if (whiteList.stream().anyMatch(requestURI::startsWith)){
            filterChain.doFilter(request, response);
            return;
        }

        String access = request.getHeader("Authorization").split(" ")[1];

        // validation1 - token null
        if (access == null) {
            System.out.println("token value null");
            throw new JWTException.TokenNullException();
        }

        // validation2 - token expire
        if (jwtUtil.isExpired(access)){
            System.out.println("token expired");
            throw new JWTException.TokenExpiredException();
        }

        // validation3 - token type for access
        String tokenType = jwtUtil.getCategory(access);

        if (!tokenType.equals("access")) {
            System.out.println("token type is not for access");
            throw new JWTException.TokenTypeNotAccessException();
        }

        String username = jwtUtil.getUsername(access);
        String role = jwtUtil.getRole(access);
        Long id = jwtUtil.getId(access);

        UserAccessDto userAccessDto = UserAccessDto.builder()
                .username(username)
                .role(role)
                .id(id)
                .build();

        // USER
        if (role.equals("ROLE_USER")){
            RoleUserDetails roleUserDetails = new RoleUserDetails(userAccessDto);

            Authentication authentication = new UsernamePasswordAuthenticationToken(roleUserDetails, null, roleUserDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }
        // ADMIN
        else{
            filterChain.doFilter(request, response);
        }
    }
}