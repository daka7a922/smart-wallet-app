package github.daka7a922.smart_wallet_app.web;

import github.daka7a922.smart_wallet_app.security.RequireAdminRole;
import github.daka7a922.smart_wallet_app.user.model.User;
import github.daka7a922.smart_wallet_app.user.service.UserService;
import github.daka7a922.smart_wallet_app.web.dto.UserEditRequest;
import github.daka7a922.smart_wallet_app.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/{id}/profile")
    public ModelAndView getUserProfilePage(@PathVariable UUID id){

        User user = userService.getUserById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("profile-menu");
        modelAndView.addObject("user", user);
        modelAndView.addObject("userEditRequest", DtoMapper.mapUserToUserProfileEditRequest(user));

        return modelAndView;
    }

    @PutMapping("/{id}/profile")
    public ModelAndView updateUserProfile(@PathVariable UUID id, @Valid UserEditRequest userEditRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            User user = userService.getUserById(id);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("profile-menu");
            modelAndView.addObject("user", user);
            modelAndView.addObject("userEditRequest", userEditRequest);
            return modelAndView;
        }

        userService.updateUserProfile(id, userEditRequest);

        return new ModelAndView("redirect:/home");
    }

    @GetMapping
    @RequireAdminRole
    public ModelAndView getAllUsers(){

        List<User> users = userService.getAllUsers();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users");
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    @PutMapping("/{id}/status")
    public String updateUserStatus(@PathVariable UUID id) {

        userService.switchStatus(id);

        return "redirect:/users";
    }
 @PutMapping("/{id}/role")
    public String updateUserRole(@PathVariable UUID id) {

        userService.switchRole(id);

        return "redirect:/users";
    }


}
