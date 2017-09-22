package ee.scanner.tablet.db;

import ee.scanner.tablet.domain.DeviceUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<DeviceUser, Integer> {
    Optional<DeviceUser> findByPin(String pin);
}
