package ee.scanner.tablet.db;

import ee.scanner.tablet.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByPin(String pin);
}
