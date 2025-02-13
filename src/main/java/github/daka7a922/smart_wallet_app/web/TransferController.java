package github.daka7a922.smart_wallet_app.web;

import github.daka7a922.smart_wallet_app.user.model.User;
import github.daka7a922.smart_wallet_app.user.service.UserService;
import github.daka7a922.smart_wallet_app.web.dto.TransferRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/transfers")
public class TransferController {

    private final UserService userService;

    @Autowired
    public TransferController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getTransferPage(){

        User user = userService.getUserById(UUID.fromString("4f35f873-f28c-466a-8cd4-5e52482b1b8f"));

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.addObject("transferRequest", TransferRequest.builder().build());
        modelAndView.setViewName("transfer");

        return modelAndView;
    }

    @PostMapping
    public ModelAndView initiateTransfer(@Valid TransferRequest transferRequest, BindingResult bindingResult){



        ModelAndView modelAndView = new ModelAndView();


        return null;
    }
}
