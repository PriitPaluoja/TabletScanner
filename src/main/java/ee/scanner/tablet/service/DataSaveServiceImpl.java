package ee.scanner.tablet.service;

import ee.scanner.tablet.db.DeviceRepository;
import ee.scanner.tablet.db.RentalRepository;
import ee.scanner.tablet.db.UserRepository;
import ee.scanner.tablet.domain.Device;
import ee.scanner.tablet.domain.DeviceUser;
import ee.scanner.tablet.dto.DeviceDTO;
import ee.scanner.tablet.dto.RentalDTO;
import ee.scanner.tablet.dto.UserDTO;
import ee.scanner.tablet.dto.UserWrapperDTO;
import ee.scanner.tablet.exception.DeviceDuplicateException;
import ee.scanner.tablet.exception.IdNotPresentException;
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
        userRepository.save(new DeviceUser(null,
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getPin(),
                true));
    }

    @Override
    public List<RentalDTO> getActiveRentals() {
        return rentalRepository.findByIsReturned(false).stream()
                .map(e -> new RentalDTO(
                                e.getId(),
                                new UserDTO(e.getId(), e.getUser().getFirstName(), e.getUser().getLastName(), e.getUser().getPin(), e.getUser().getActive()),
                                new DeviceDTO(e.getDevice().getIdent()),
                                e.getRentalTime(),
                                e.getReturnTime(),
                                e.getIsReturned(),
                                new UserDTO(e.getId(), e.getUser().getFirstName(), e.getUser().getLastName(), e.getUser().getPin(), e.getUser().getActive())
                        )
                ).collect(Collectors.toList());
    }

    @Override
    public List<RentalDTO> getAllRentals() {
        return rentalRepository.findAll()
                .stream().map(e -> new RentalDTO(
                                e.getId(),
                                new UserDTO(e.getId(), e.getUser().getFirstName(), e.getUser().getLastName(), e.getUser().getPin(), e.getUser().getActive()),
                                new DeviceDTO(e.getDevice().getIdent()),
                                e.getRentalTime(),
                                e.getReturnTime(),
                                e.getIsReturned(),
                                new UserDTO(e.getId(), e.getUser().getFirstName(), e.getUser().getLastName(), e.getUser().getPin(), e.getUser().getActive())
                        )
                ).collect(Collectors.toList());
    }

    @Override
    public UserWrapperDTO getAllUsers() {
        return new UserWrapperDTO(userRepository.findAll().stream()
                .map(e -> new UserDTO(e.getId(), e.getFirstName(), e.getLastName(), e.getPin(), e.getActive()))
                .collect(Collectors.toList()));
    }

    @Override
    public List<DeviceDTO> getAllDevices() {
        return deviceRepository.findAll().stream()
                .map(e -> new DeviceDTO(e.getIdent())).collect(Collectors.toList());
    }

    @Override
    public void updateUsers(UserWrapperDTO dto) throws IdNotPresentException {
        List<DeviceUser> fromFrontEnd = dto.getUsers().stream()
                .map(e -> new DeviceUser(e.getId(), e.getFirstName(), e.getLastName(), e.getPin(), e.getActive()))
                .collect(Collectors.toList());

        for (DeviceUser fromFront : fromFrontEnd) {
            DeviceUser fromDb = userRepository.findOne(fromFront.getId());
            if (fromDb == null) {
                throw new IdNotPresentException();
            } else {
                if (!fromFront.getActive()) {
                    fromFront.setPin(null);
                }
                userRepository.save(fromFront);
            }
        }
    }
}
