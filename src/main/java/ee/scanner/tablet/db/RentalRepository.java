package ee.scanner.tablet.db;

import ee.scanner.tablet.domain.Rental;
import ee.scanner.tablet.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RentalRepository extends JpaRepository<Rental, Integer> {
    List<Rental> findByUserPinAndIsReturned(String userPin, Boolean isReturned);
}
