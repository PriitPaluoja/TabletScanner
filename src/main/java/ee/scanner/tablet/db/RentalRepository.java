package ee.scanner.tablet.db;

import ee.scanner.tablet.domain.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RentalRepository extends JpaRepository<Rental, Integer> {
    List<Rental> findByUserPinAndIsReturned(String userPin, Boolean isReturned);

    Rental findByDeviceIdentAndIsReturned(String ident, Boolean isReturned);

    List<Rental> findByIsReturned(Boolean isReturned);
}
