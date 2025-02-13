package github.daka7a922.smart_wallet_app.web.dto;

import github.daka7a922.smart_wallet_app.user.model.Country;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @Size(min = 6 ,  message = "Username must be at least 6 symbols")
    private String username;

    private String name;

    @Size(min = 6 ,  message = "Password must be at least 6 symbols")
    private String password;

    @NotNull
    private Country country;


}
