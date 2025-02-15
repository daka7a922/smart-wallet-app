package github.daka7a922.smart_wallet_app.web;

import github.daka7a922.smart_wallet_app.subscription.model.SubscriptionType;
import github.daka7a922.smart_wallet_app.subscription.service.SubscriptionService;
import github.daka7a922.smart_wallet_app.transaction.model.Transaction;
import github.daka7a922.smart_wallet_app.transaction.service.TransactionService;
import github.daka7a922.smart_wallet_app.user.model.User;
import github.daka7a922.smart_wallet_app.user.service.UserService;
import github.daka7a922.smart_wallet_app.web.dto.UpgradeRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final TransactionService transactionService;

    @Autowired
    public SubscriptionController(UserService userService, SubscriptionService subscriptionService, TransactionService transactionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
        this.transactionService = transactionService;
    }


    @GetMapping
    public ModelAndView getSubscriptionsPage(HttpSession session){

        UUID id = (UUID) session.getAttribute("user_id");
        User user = userService.getUserById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("upgrade");
        modelAndView.addObject("user", user);
        modelAndView.addObject("upgradeRequest", UpgradeRequest.builder().build());

        return modelAndView;
    }

    @PostMapping
    public String upgrade(@RequestParam("subscription-type") SubscriptionType subscriptionType, UpgradeRequest upgradeRequest, HttpSession session) {

        UUID id = (UUID) session.getAttribute("user_id");
        User user = userService.getUserById(id);

        Transaction upgradeResult = subscriptionService.upgrade(user, subscriptionType, upgradeRequest);

        return "redirect:/transactions/" + upgradeResult.getId();
    }

//    @GetMapping("/upgrade-result/{id}")
//    public ModelAndView getUpgradeById(@PathVariable UUID id) {
//
//        Transaction transaction = transactionService.getById(id);
//
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("upgrade-result");
//        modelAndView.addObject("subscription", transaction );
//
//        return modelAndView;
//    }

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
