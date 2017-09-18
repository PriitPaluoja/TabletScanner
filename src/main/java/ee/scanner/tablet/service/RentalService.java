package ee.scanner.tablet.service;

import ee.scanner.tablet.dto.RegisterDTO;

public interface RentalService {
    void retrieveDevices(RegisterDTO dto);

    void returnDevices(RegisterDTO dto);
}