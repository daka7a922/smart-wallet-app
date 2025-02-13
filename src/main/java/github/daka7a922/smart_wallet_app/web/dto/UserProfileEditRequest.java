package github.daka7a922.smart_wallet_app.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileEditRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String profilePicture;
}
