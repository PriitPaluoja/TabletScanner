package ee.scanner.tablet.db;

import ee.scanner.tablet.domain.Rental;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RentalRepository extends JpaRepository<Rental, Integer> {
}
