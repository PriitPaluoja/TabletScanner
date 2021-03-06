package ee.scanner.tablet.db;

import ee.scanner.tablet.domain.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RentalRepository extends JpaRepository<Rental, Integer> {
    Rental findByDeviceIdentAndIsReturned(String ident, Boolean isReturned);

    List<Rental> findByIsReturned(Boolean isReturned);

    Integer countAllByDeviceIdent(String ident);

    Integer countAllByUserPin(String pin);

    List<Rental> findByUserPin(String pin);
}
