package github.daka7a922.smart_wallet_app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/subscriptions")
public class SubscriptionController {


    @GetMapping
    public ModelAndView getSubscriptionsPage(){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("upgrade");

        return modelAndView;
    }

    @GetMapping("/history")
    public ModelAndView subscriptionHistoryPage(){

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("subscription-history");

        return modelAndView;

    }


}
