package ee.scanner.tablet.service;

import ee.scanner.tablet.db.DeviceRepository;
import ee.scanner.tablet.db.RentalRepository;
import ee.scanner.tablet.db.UserRepository;
import ee.scanner.tablet.domain.Device;
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
        // Find all devices from database
        List<Device> devices = Arrays.stream(dto.getDevices().split(RENTAL_DEVICE_SEPARATOR))
                .map(deviceRepository::findDeviceByIdent)
                .collect(Collectors.toList());

        // Find all rentals regarding user
        rentalRepository.save(new Rental(
                null,
                userRepository.findByPin(dto.getPersonInformation()).get(),
                devices,
                LocalDateTime.now(),
                null,
                false)
        );
    }

    @Override
    public void returnDevices(RegisterDTO dto) throws NoSuchElementException, NoActiveRentalsFoundException {
        // Find all devices from database
        List<Device> devices = Arrays.stream(dto.getDevices().split(RENTAL_DEVICE_SEPARATOR))
                .map(deviceRepository::findDeviceByIdent)
                .collect(Collectors.toList());

        // Find all rentals regarding user
        List<Rental> rentals = rentalRepository.findByUserPinAndIsReturned(dto.getPersonInformation(), false);

        if (rentals.isEmpty()) throw new NoActiveRentalsFoundException();

        for (Rental rental : rentals) {
            for (Device dbDevice : devices) {
                if (rental.getDevices().contains(dbDevice)) {
                    rental.setIsReturned(true);
                    rental.setReturnTime(LocalDateTime.now());
                    break;
                }
            }
        }
        rentalRepository.save(rentals);
    }
}