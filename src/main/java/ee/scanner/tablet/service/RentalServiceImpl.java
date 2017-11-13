package ee.scanner.tablet.service;

import ee.scanner.tablet.db.DeviceRepository;
import ee.scanner.tablet.db.RentalRepository;
import ee.scanner.tablet.db.UserRepository;
import ee.scanner.tablet.domain.Device;
import ee.scanner.tablet.domain.DeviceUser;
import ee.scanner.tablet.domain.Rental;
import ee.scanner.tablet.dto.RegisterDTO;
import ee.scanner.tablet.exception.NoActiveRentalsFoundException;
import ee.scanner.tablet.exception.NoDeviceFoundException;
import ee.scanner.tablet.exception.NoUserFoundException;
import ee.scanner.tablet.exception.SomeDeviceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RentalServiceImpl implements RentalService {
    private static final String RENTAL_DEVICE_SEPARATOR = "-";
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;

    @Override
    public void takeDevices(RegisterDTO dto) throws NoUserFoundException, NoDeviceFoundException, SomeDeviceNotFoundException {

        // Fin user by PIN
        DeviceUser user = userRepository.findByPin(dto.getPersonInformation()).orElse(null);
        // If no such user, throw exception
        if (user == null) throw new NoUserFoundException();


        // Convert device id-s from input to Device Object (via database). Filter out Null Objects
        List<Device> devices = Arrays.stream(dto.getDevices().trim().split(RENTAL_DEVICE_SEPARATOR))
                .map(String::trim)
                .collect(Collectors.toSet())
                .stream()
                .map(deviceRepository::findDeviceByIdent)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());


        // If is empty, then there are no devices from user input
        if (devices.isEmpty()) throw new NoDeviceFoundException();


        // If some of the rentals associated with these devices are marked as not returned, mark them as returned
        List<Rental> rentals = devices.stream()
                .map(e -> rentalRepository.findByDeviceIdentAndIsReturned(e.getIdent(), false))
                .collect(Collectors.toList());

        for (Rental e : rentals) {
            if (e != null) {
                e.setIsReturned(true);
                rentalRepository.save(e);
            }
        }

        // Save new rentals
        LocalDateTime timestamp = LocalDateTime.now();
        devices.forEach(device -> rentalRepository.save(new Rental(null, user, device, timestamp, null, false, null)));

        // If some of the mapping failed, throw exception
        if (devices.size() != dto.getDevices().trim().split(RENTAL_DEVICE_SEPARATOR).length)
            throw new SomeDeviceNotFoundException();
    }

    @Override
    public void returnDevices(RegisterDTO dto) throws NoSuchElementException, NoActiveRentalsFoundException, NoUserFoundException {
        // Find all devices from database
        List<Rental> rentals = Arrays.stream(dto.getDevices().split(RENTAL_DEVICE_SEPARATOR))
                .map(deviceRepository::findDeviceByIdent)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
                .stream()
                .map(e -> rentalRepository.findByDeviceIdentAndIsReturned(e.getIdent(), false))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());


        // Find all rentals regarding user
        if (rentals.isEmpty()) throw new NoActiveRentalsFoundException();


        // Mark rentals as returned
        DeviceUser user = userRepository.findByPin(dto.getPersonInformation().trim()).orElse(null);
        LocalDateTime timestamp = LocalDateTime.now();

        for (Rental rental : rentals) {
            if (user != null) rental.setReturner(user);

            rental.setReturnTime(timestamp);
            rental.setIsReturned(true);
            rentalRepository.save(rental);
        }
    }
}