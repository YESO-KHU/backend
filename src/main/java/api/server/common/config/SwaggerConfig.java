package api.server.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Server")
                ))
                .components(components())
                .info(apiInfo())
                .addSecurityItem(new SecurityRequirement().addList("Authorization"));
    }

    private Info apiInfo() {
        return new Info()
                .title("BlockEstate Backend API Docs")
                .description("BlockEstate API 명세서입니다.");
    }

    private Components components() {
        return new Components()
                .addSecuritySchemes("Authorization", // <-- 이 이름을 위에서 참조
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                );
    }
}