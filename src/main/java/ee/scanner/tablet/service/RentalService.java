package ee.scanner.tablet.service;

import ee.scanner.tablet.dto.RegisterDTO;
import ee.scanner.tablet.exception.NoActiveRentalsFoundException;
import ee.scanner.tablet.exception.NoDeviceFoundException;
import ee.scanner.tablet.exception.NoUserFoundException;
import ee.scanner.tablet.exception.SomeDeviceNotFoundException;

public interface RentalService {

    /**
     * Register devices as rented.
     *
     * @throws NoUserFoundException        if user by PIN was not found.
     * @throws NoDeviceFoundException      if no devices are provided.
     * @throws SomeDeviceNotFoundException if some of the devices by identification was not found.
     */
    void takeDevices(RegisterDTO dto) throws NoUserFoundException, NoDeviceFoundException, SomeDeviceNotFoundException;

    /**
     * Register devices as returned.
     *
     * @throws NoActiveRentalsFoundException if no active rentals found.
     */
    void returnDevices(RegisterDTO dto) throws NoActiveRentalsFoundException, NoUserFoundException;
}