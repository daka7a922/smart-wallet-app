package github.daka7a922.smart_wallet_app.security;

import github.daka7a922.smart_wallet_app.user.model.User;
import github.daka7a922.smart_wallet_app.user.model.UserRole;
import github.daka7a922.smart_wallet_app.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;
import java.util.UUID;

@Component
@Slf4j
public class SessionCheckInterceptor implements HandlerInterceptor {

    private final Set<String> UNAUTHENTICATED_ENDPOINTS = Set.of("/","/login","/register");
    private final Set<String> ADMIN_ENDPOINTS = Set.of("/users","/reports");

    private final UserService userService;

    @Autowired
    public SessionCheckInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String endpoint = request.getServletPath();

        if (UNAUTHENTICATED_ENDPOINTS.contains(endpoint)){
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null){
            response.sendRedirect("/login");
            return false;
        }

        UUID userId = (UUID) session.getAttribute("user_id");
        User user = userService.getUserById(userId);

        if (!user.isActive()){
            session.invalidate();
            log.info("Inactive user %s tried logging in".formatted(user.getUsername()));
            response.sendRedirect("/");
            return false;
        }
         // Way one:

//        if (ADMIN_ENDPOINTS.contains(endpoint) && user.getUserRole() != UserRole.ADMIN){
//
//            response.setStatus(HttpStatus.FORBIDDEN.value());
//            response.getWriter().write("You are not allowed to access this resource");
//            return false;
//        }

        // Way two:
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if (handlerMethod.hasMethodAnnotation(RequireAdminRole.class) && user.getUserRole() != UserRole.ADMIN){
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("You are not allowed to access this resource");
            return false;
        }


        return true;
    }
}
