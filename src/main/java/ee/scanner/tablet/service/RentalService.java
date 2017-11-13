package ee.scanner.tablet.service;

import ee.scanner.tablet.dto.RegisterDTO;
import ee.scanner.tablet.exception.NoActiveRentalsFoundException;
import ee.scanner.tablet.exception.NoDeviceFoundException;
import ee.scanner.tablet.exception.NoUserFoundException;
import ee.scanner.tablet.exception.SomeDeviceNotFoundException;

import java.util.NoSuchElementException;

public interface RentalService {
    void takeDevices(RegisterDTO dto) throws NoUserFoundException, NoDeviceFoundException, SomeDeviceNotFoundException;

    void returnDevices(RegisterDTO dto) throws NoActiveRentalsFoundException, NoUserFoundException;
}