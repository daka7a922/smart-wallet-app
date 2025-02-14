package github.daka7a922.smart_wallet_app.web;

import github.daka7a922.smart_wallet_app.user.model.User;
import github.daka7a922.smart_wallet_app.user.service.UserService;
import github.daka7a922.smart_wallet_app.web.dto.LoginRequest;
import github.daka7a922.smart_wallet_app.web.dto.RegisterRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class IndexController {

    private final UserService userService;

    @Autowired
    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String getIndexPage() {

        return "index";
    }


    @GetMapping("/login")
    public ModelAndView getLoginPage() {

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("login");
        modelAndView.addObject("loginRequest", new LoginRequest());

        return modelAndView;

    }

    @PostMapping ("/login")
    public String login(@Valid LoginRequest loginRequest, BindingResult result, HttpSession session){

        if (result.hasErrors()){
            return "login";
        }

        User loggedInUser = userService.login(loginRequest);
        session.setAttribute("user_id", loggedInUser.getId());

        return "redirect:home";
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("registerRequest", new RegisterRequest());
        modelAndView.setViewName("register");

        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerNewUser(@Valid RegisterRequest registerRequest, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return new ModelAndView("register");
        }

        userService.register(registerRequest);

        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/home")
    public ModelAndView getHomePage(HttpSession session){

        UUID userId = (UUID) session.getAttribute("user_id");

        User user = userService.getUserById(userId);


        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        modelAndView.addObject("user");
        modelAndView.addObject("user", user);

        return modelAndView;
    }


    @GetMapping("/logout")
    public String getLogoutPage(HttpSession session){

        session.invalidate();


        return "redirect:/";
    }



}
