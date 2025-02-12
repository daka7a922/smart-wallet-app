package github.daka7a922.smart_wallet_app.web;

import github.daka7a922.smart_wallet_app.user.model.User;
import github.daka7a922.smart_wallet_app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/wallets")
public class WalletController {

    private final UserService userService;
    @Autowired
    public WalletController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping()
    public ModelAndView getWalletsPage(){

        //User user = userService.getUserById()

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("wallets");


        return modelAndView;
    }
}
