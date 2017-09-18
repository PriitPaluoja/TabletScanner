package ee.scanner.tablet.db;

import ee.scanner.tablet.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
