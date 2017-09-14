package ee.scanner.tablet;

import ee.scanner.tablet.dto.RegisterDTO;

public interface RentalService {
    void retrieveDevices(RegisterDTO dto);

    void returnDevices(RegisterDTO dto);
}