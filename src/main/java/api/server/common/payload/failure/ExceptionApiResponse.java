package api.server.common.payload.failure;

import api.server.common.payload.BaseApiResponse;
import lombok.Getter;

@Getter
public class ExceptionApiResponse extends BaseApiResponse {

    public ExceptionApiResponse(Boolean isSuccess, String code, String message) {
        super(isSuccess, code, message);
    }

}
