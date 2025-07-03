package api.server.domain.user.dto.userDetails;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserAccessDto {
    private Long id;
    private String username;
    private String role;

    @Builder
    public UserAccessDto(Long id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }
}
