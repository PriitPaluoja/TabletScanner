package ee.scanner.tablet.service;

import ee.scanner.tablet.dto.DeviceDTO;
import ee.scanner.tablet.dto.RentalDTO;
import ee.scanner.tablet.dto.UserDTO;
import ee.scanner.tablet.exception.DeviceDuplicateException;
import ee.scanner.tablet.exception.PinDuplicateException;

import java.util.List;

public interface DataSaveService {
    void saveDevice(DeviceDTO deviceDTO) throws DeviceDuplicateException;

    void saveUser(UserDTO userDTO) throws PinDuplicateException;

    List<RentalDTO> getActiveRentals();

    List<RentalDTO> getAllRentals();

    List<UserDTO> getAllUsers();

    List<DeviceDTO> getAllDevices();
}
