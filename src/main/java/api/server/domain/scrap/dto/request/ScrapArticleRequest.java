package api.server.domain.scrap.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ScrapArticleRequest {

    @Size(max = 20, message = "메모는 100자를 초과할 수 없습니다.")
    private String memo = "";
}
