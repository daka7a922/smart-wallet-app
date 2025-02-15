package github.daka7a922.smart_wallet_app.web;

import github.daka7a922.smart_wallet_app.user.model.User;
import github.daka7a922.smart_wallet_app.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final UserService userService;

    @Autowired
    public SubscriptionController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ModelAndView getSubscriptionsPage(HttpSession session){

        UUID id = (UUID) session.getAttribute("user_id");
        User user = userService.getUserById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("upgrade");

        return modelAndView;
    }

    @GetMapping("/history")
    public ModelAndView subscriptionHistoryPage(HttpSession session){

        UUID userId = (UUID) session.getAttribute("user_id");
        User user = userService.getUserById(userId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("subscription-history");
        modelAndView.addObject("user", user);
        return modelAndView;

    }
}
