package ee.scanner.tablet.service;

import ee.scanner.tablet.dto.*;
import ee.scanner.tablet.exception.DeviceDuplicateException;
import ee.scanner.tablet.exception.IdNotPresentException;
import ee.scanner.tablet.exception.PinDuplicateException;
import ee.scanner.tablet.exception.PinNotPresentException;

import java.util.List;

public interface DataSaveService {
    /**
     * Save new device to database.
     *
     * @throws DeviceDuplicateException if device identification already exists in the database.
     */
    void saveDevice(DeviceDTO deviceDTO) throws DeviceDuplicateException;

    /**
     * Save new user into the database.
     *
     * @throws PinDuplicateException if user PIN already exists.
     */
    void saveUser(UserDTO userDTO) throws PinDuplicateException;

    /**
     * @return find and retrieve all active rentals from the database.
     */
    List<RentalDTO> getActiveRentals();

    /**
     * @return find and retrieve all rentals from the database.
     */
    List<RentalDTO> getAllRentals();

    /**
     * @return find and retrieve all users from the database.
     */
    UserWrapperDTO getAllUsers();

    /**
     * @return find and retrieve all devices from the database.
     */
    DeviceWrapperDTO getAllDevices();

    /**
     * Update existing user.
     *
     * @throws IdNotPresentException  if user ID not present.
     * @throws PinNotPresentException if user PIN not present.
     * @throws PinDuplicateException  if user PIN already in use.
     */
    void updateUsers(UserWrapperDTO dto) throws IdNotPresentException, PinNotPresentException, PinDuplicateException;

    /**
     * Update existing device.
     *
     * @throws IdNotPresentException    if device ID not provided.
     * @throws DeviceDuplicateException if device identification already in use.
     */
    void updateDevices(DeviceWrapperDTO dto) throws IdNotPresentException, DeviceDuplicateException;

    /**
     * @return true if user exists, else false (by user PIN)
     */
    Boolean userExists(String pin);

    /**
     * @return user first name if user present, else return empty string.
     */
    String getUserFirstName(String pin);
}
