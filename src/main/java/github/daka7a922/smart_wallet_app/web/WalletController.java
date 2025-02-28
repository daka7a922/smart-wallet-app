package github.daka7a922.smart_wallet_app.web;

import github.daka7a922.smart_wallet_app.security.AuthenticationDetails;
import github.daka7a922.smart_wallet_app.user.model.User;
import github.daka7a922.smart_wallet_app.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/wallets")
public class WalletController {

    private final UserService userService;
    @Autowired
    public WalletController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping()
    public ModelAndView getWalletsPage(@AuthenticationPrincipal AuthenticationDetails userDetails) {

        String username = userDetails.getUsername();
        User user = userService.getByUsername(username);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("wallets");


        return modelAndView;
    }
}
