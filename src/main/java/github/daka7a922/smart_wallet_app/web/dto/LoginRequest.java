package github.daka7a922.smart_wallet_app.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {

    private String username;

    private String password;

}
