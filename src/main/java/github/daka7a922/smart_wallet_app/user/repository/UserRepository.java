package github.daka7a922.smart_wallet_app.user.repository;

import github.daka7a922.smart_wallet_app.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>{

    Optional<User> findByUsername (String username);


}
