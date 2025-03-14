package github.daka7a922.smart_wallet_app.user.service;

import github.daka7a922.smart_wallet_app.user.model.Country;
import github.daka7a922.smart_wallet_app.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserInit implements CommandLineRunner {

    private final UserService userService;


    @Autowired
    public UserInit(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {


        if (!userService.getAllUsers().isEmpty()) {
            return;
        }

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("Test2")
                .password("123123")
                .country(Country.GERMANY)
                .build();
        userService.register(registerRequest);
    }
}
