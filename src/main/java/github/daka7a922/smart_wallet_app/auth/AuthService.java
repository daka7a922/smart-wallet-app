package github.daka7a922.smart_wallet_app.auth;

import github.daka7a922.smart_wallet_app.exception.DomainException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    //Might implement this feature TODO

    public String getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
         throw new DomainException("Invalid user");
    }
}
