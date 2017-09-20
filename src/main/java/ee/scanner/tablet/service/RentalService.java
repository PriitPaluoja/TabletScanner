package ee.scanner.tablet.service;

import ee.scanner.tablet.dto.RegisterDTO;
import ee.scanner.tablet.exception.NoActiveRentalsFoundException;

import java.util.NoSuchElementException;

public interface RentalService {
    void takeDevices(RegisterDTO dto);

    void returnDevices(RegisterDTO dto) throws NoSuchElementException, NoActiveRentalsFoundException;
}