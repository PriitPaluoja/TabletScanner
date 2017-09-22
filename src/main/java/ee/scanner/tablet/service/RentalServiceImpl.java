package ee.scanner.tablet.service;

import ee.scanner.tablet.db.DeviceRepository;
import ee.scanner.tablet.db.RentalRepository;
import ee.scanner.tablet.db.UserRepository;
import ee.scanner.tablet.domain.DeviceUser;
import ee.scanner.tablet.domain.Rental;
import ee.scanner.tablet.dto.RegisterDTO;
import ee.scanner.tablet.exception.NoActiveRentalsFoundException;
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
    public void takeDevices(RegisterDTO dto) throws NoSuchElementException {
        DeviceUser user = userRepository.findByPin(dto.getPersonInformation()).orElse(null);
        LocalDateTime timestamp = LocalDateTime.now();

        if (user == null) throw new NoSuchElementException();

        // Find all devices from database
        Arrays.stream(dto.getDevices().split(RENTAL_DEVICE_SEPARATOR))
                .map(String::trim)
                .map(deviceRepository::findDeviceByIdent)
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
                .forEach(device -> rentalRepository.save(new Rental(null, user, device, timestamp, null, false, null)));
    }

    @Override
    public void returnDevices(RegisterDTO dto) throws NoSuchElementException, NoActiveRentalsFoundException {
        // Find all devices from database
        List<Rental> rentals = Arrays.stream(dto.getDevices().split(RENTAL_DEVICE_SEPARATOR))
                .map(deviceRepository::findDeviceByIdent)
                .map(e -> rentalRepository.findByDeviceIdentAndIsReturned(e.getIdent(), false))
                .collect(Collectors.toList());

        // Find all rentals regarding user
        if (rentals.isEmpty()) throw new NoActiveRentalsFoundException();

        DeviceUser user = userRepository.findByPin(dto.getPersonInformation().trim()).orElse(null);
        LocalDateTime timestamp = LocalDateTime.now();

        for (Rental rental : rentals) {
            rental.setReturner(user);
            rental.setReturnTime(timestamp);
            rental.setIsReturned(true);
            rentalRepository.save(rental);
        }
    }
}