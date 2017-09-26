package ee.scanner.tablet.service;

import ee.scanner.tablet.db.DeviceRepository;
import ee.scanner.tablet.db.RentalRepository;
import ee.scanner.tablet.db.UserRepository;
import ee.scanner.tablet.domain.Device;
import ee.scanner.tablet.domain.DeviceUser;
import ee.scanner.tablet.dto.DeviceDTO;
import ee.scanner.tablet.dto.RentalDTO;
import ee.scanner.tablet.dto.UserDTO;
import ee.scanner.tablet.exception.DeviceDuplicateException;
import ee.scanner.tablet.exception.PinDuplicateException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DataSaveServiceImpl implements DataSaveService {
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final RentalRepository rentalRepository;

    @Override
    public void saveDevice(DeviceDTO deviceDTO) throws DeviceDuplicateException {
        if (deviceRepository.findDeviceByIdent(deviceDTO.getDeviceIdentification()) != null) {
            throw new DeviceDuplicateException();
        }
        deviceRepository.save(new Device(null, deviceDTO.getDeviceIdentification()));
    }

    @Override
    public void saveUser(UserDTO userDTO) throws PinDuplicateException {
        // No duplicate check
        if (userRepository.findByPin(userDTO.getPin()).isPresent()) {
            throw new PinDuplicateException();
        }
        userRepository.save(new DeviceUser(null, userDTO.getFirstName(), userDTO.getLastName(), userDTO.getPin()));
    }

    @Override
    public List<RentalDTO> getActiveRentals() {
        return rentalRepository.findByIsReturned(false).stream()
                .map(e -> new RentalDTO(
                                e.getId(),
                                new UserDTO(e.getUser().getFirstName(), e.getUser().getLastName(), e.getUser().getPin()),
                                new DeviceDTO(e.getDevice().getIdent()),
                                e.getRentalTime(),
                                e.getReturnTime(),
                                e.getIsReturned(),
                                new UserDTO(e.getUser().getFirstName(), e.getUser().getLastName(), e.getUser().getPin())
                        )
                ).collect(Collectors.toList());
    }

    @Override
    public List<RentalDTO> getAllRentals() {
        return rentalRepository.findAll()
                .stream().map(e -> new RentalDTO(
                                e.getId(),
                                new UserDTO(e.getUser().getFirstName(), e.getUser().getLastName(), e.getUser().getPin()),
                                new DeviceDTO(e.getDevice().getIdent()),
                                e.getRentalTime(),
                                e.getReturnTime(),
                                e.getIsReturned(),
                                new UserDTO(e.getUser().getFirstName(), e.getUser().getLastName(), e.getUser().getPin())
                        )
                ).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(e -> new UserDTO(e.getFirstName(), e.getLastName(), e.getPin()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceDTO> getAllDevices() {
        return deviceRepository.findAll().stream()
                .map(e -> new DeviceDTO(e.getIdent())).collect(Collectors.toList());
    }
}
