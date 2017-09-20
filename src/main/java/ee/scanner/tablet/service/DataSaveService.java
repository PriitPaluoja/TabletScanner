package ee.scanner.tablet.service;

import ee.scanner.tablet.dto.DeviceDTO;
import ee.scanner.tablet.dto.UserDTO;
import ee.scanner.tablet.exception.DeviceDuplicateException;
import ee.scanner.tablet.exception.PinDuplicateException;

public interface DataSaveService {
    void saveDevice(DeviceDTO deviceDTO) throws DeviceDuplicateException;

    void saveUser(UserDTO userDTO) throws PinDuplicateException;
}
