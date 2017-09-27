package ee.scanner.tablet.service;

import ee.scanner.tablet.dto.*;
import ee.scanner.tablet.exception.DeviceDuplicateException;
import ee.scanner.tablet.exception.IdNotPresentException;
import ee.scanner.tablet.exception.PinDuplicateException;

import java.util.List;

public interface DataSaveService {
    void saveDevice(DeviceDTO deviceDTO) throws DeviceDuplicateException;

    void saveUser(UserDTO userDTO) throws PinDuplicateException;

    List<RentalDTO> getActiveRentals();

    List<RentalDTO> getAllRentals();

    UserWrapperDTO getAllUsers();

    DeviceWrapperDTO getAllDevices();

    void updateUsers(UserWrapperDTO dto) throws IdNotPresentException;

    void updateDevices(DeviceWrapperDTO dto) throws IdNotPresentException;
}
