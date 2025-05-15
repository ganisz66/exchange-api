package pl.szlify.exchangeapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.szlify.exchangeapi.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
