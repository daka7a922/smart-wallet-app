package github.daka7a922.smart_wallet_app.web.dto;

import github.daka7a922.smart_wallet_app.user.model.Country;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class RegisterRequest {


    @Size(min = 6 ,  message = "Username must be at least 6 symbols")
    private String username;

    private String name;

    @Size(min = 6 ,  message = "Password must be at least 6 symbols")
    private String password;

    @NotNull
    private Country country;

    public RegisterRequest() {
    }

    public RegisterRequest(String username, String name, String password, Country country) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.country = country;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
