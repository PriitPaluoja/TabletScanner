package ee.scanner.tablet.service;

import ee.scanner.tablet.dto.DeviceDTO;
import ee.scanner.tablet.dto.UserDTO;

public interface DataSaveService {
    void saveDevice(DeviceDTO deviceDTO);

    void saveUser(UserDTO userDTO);
}
