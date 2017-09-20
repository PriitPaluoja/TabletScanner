package ee.scanner.tablet.db;

import ee.scanner.tablet.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Integer> {
    Device findDeviceByIdent(String ident);
}