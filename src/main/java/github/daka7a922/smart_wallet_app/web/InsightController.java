package github.daka7a922.smart_wallet_app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class InsightController {

    @GetMapping("/reports")
    public ModelAndView getReportsPage(){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("reports");

        return modelAndView;
    }
}
