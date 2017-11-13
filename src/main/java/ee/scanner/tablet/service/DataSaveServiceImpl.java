package ee.scanner.tablet.service;

import ee.scanner.tablet.db.DeviceRepository;
import ee.scanner.tablet.db.RentalRepository;
import ee.scanner.tablet.db.UserRepository;
import ee.scanner.tablet.domain.Device;
import ee.scanner.tablet.domain.DeviceUser;
import ee.scanner.tablet.domain.Rental;
import ee.scanner.tablet.dto.*;
import ee.scanner.tablet.exception.DeviceDuplicateException;
import ee.scanner.tablet.exception.IdNotPresentException;
import ee.scanner.tablet.exception.PinDuplicateException;
import ee.scanner.tablet.exception.PinNotPresentException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DataSaveServiceImpl implements DataSaveService {
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final RentalRepository rentalRepository;

    @Override
    public void saveDevice(DeviceDTO deviceDTO) throws DeviceDuplicateException {
        if (deviceRepository.findDeviceByIdent(deviceDTO.getDeviceIdentification()) != null)
            throw new DeviceDuplicateException();

        deviceRepository.save(new Device(null, deviceDTO.getDeviceIdentification(), true));
    }

    @Override
    public void saveUser(UserDTO userDTO) throws PinDuplicateException {
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
        return rentalRepository.findByIsReturned(false).stream().map(convertRentalToDTO()).collect(Collectors.toList());
    }

    @Override
    public List<RentalDTO> getAllRentals() {
        return rentalRepository.findAll().stream().map(convertRentalToDTO()).collect(Collectors.toList());
    }

    @Override
    public UserWrapperDTO getAllUsers() {
        return new UserWrapperDTO(userRepository.findAll().stream()
                .map(DeviceUserUserManagementDTO())
                .collect(Collectors.toList()));
    }

    private Function<DeviceUser, UserManagementDTO> DeviceUserUserManagementDTO() {
        return deviceUser -> new UserManagementDTO(deviceUser.getId(),
                deviceUser.getFirstName(),
                deviceUser.getLastName(),
                deviceUser.getPin(),
                deviceUser.getActive());
    }

    @Override
    public DeviceWrapperDTO getAllDevices() {
        return new DeviceWrapperDTO(deviceRepository.findAll().stream().map(deviceDTO()).collect(Collectors.toList()));
    }

    @Override
    public void updateUsers(UserWrapperDTO dto) throws IdNotPresentException, PinNotPresentException, PinDuplicateException {
        List<DeviceUser> fromFrontEnd = dto.getUsers().stream().map(userManagementDeviceUser()).collect(Collectors.toList());

        for (DeviceUser fromFront : fromFrontEnd) {
            DeviceUser fromDb = userRepository.findOne(fromFront.getId());

            if (fromDb == null) throw new IdNotPresentException();
            else {
                if (fromFront.getPin().isEmpty()) throw new PinNotPresentException();
                if (!fromFront.getPin().equals(fromDb.getPin()) && userRepository.findByPin(fromFront.getPin()).isPresent())
                    throw new PinDuplicateException();

                if (!fromFront.getActive()) fromFront.setPin(null);
                userRepository.save(fromFront);
            }
        }
    }

    @Override
    public void updateDevices(DeviceWrapperDTO dto) throws IdNotPresentException, DeviceDuplicateException {
        List<Device> fromFrontEnd = dto.getDevices().stream().map(dtoDevice()).collect(Collectors.toList());

        for (Device fromFront : fromFrontEnd) {
            Device fromDb = deviceRepository.findOne(fromFront.getId());

            if (fromDb == null)
                throw new IdNotPresentException();
            else {
                //TODO: check duplicates

                if (!fromFront.getIdent().equals(fromDb.getIdent()) && deviceRepository.findDeviceByIdent(fromFront.getIdent()) != null)
                    throw new DeviceDuplicateException();

                deviceRepository.save(fromFront);
            }
        }
    }

    @Override
    public Boolean userExists(String pin) {
        return userRepository.findByPin(pin).isPresent();
    }

    private Function<Rental, RentalDTO> convertRentalToDTO() {
        return e -> new RentalDTO(
                e.getId(),
                new UserDTO(e.getUser().getId(),
                        e.getUser().getFirstName(),
                        e.getUser().getLastName(),
                        e.getUser().getPin(),
                        e.getUser().getActive()),
                new DeviceDTO(e.getDevice().getId(),
                        e.getDevice().getIdent(),
                        e.getDevice().getActive()),
                e.getRentalTime(),
                e.getReturnTime(),
                e.getIsReturned(),
                e.getReturner() == null ? null : new UserDTO(e.getReturner().getId(),
                        e.getReturner().getFirstName(),
                        e.getReturner().getLastName(),
                        e.getReturner().getPin(),
                        e.getReturner().getActive())
        );
    }

    private Function<UserManagementDTO, DeviceUser> userManagementDeviceUser() {
        return userManagementDTO -> new DeviceUser(userManagementDTO.getId(), userManagementDTO.getFirstName(), userManagementDTO.getLastName(), userManagementDTO.getPin(), userManagementDTO.getActive());
    }

    private Function<DeviceDTO, Device> dtoDevice() {
        return deviceDTO -> new Device(deviceDTO.getId(), deviceDTO.getDeviceIdentification(), deviceDTO.getActive());
    }

    private Function<Device, DeviceDTO> deviceDTO() {
        return device -> new DeviceDTO(device.getId(), device.getIdent(), device.getActive());
    }
}
