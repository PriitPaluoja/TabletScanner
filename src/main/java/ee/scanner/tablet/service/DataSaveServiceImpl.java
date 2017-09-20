package ee.scanner.tablet.service;

import ee.scanner.tablet.db.DeviceRepository;
import ee.scanner.tablet.db.UserRepository;
import ee.scanner.tablet.domain.Device;
import ee.scanner.tablet.domain.User;
import ee.scanner.tablet.dto.DeviceDTO;
import ee.scanner.tablet.dto.UserDTO;
import ee.scanner.tablet.exception.DeviceDuplicateException;
import ee.scanner.tablet.exception.PinDuplicateException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DataSaveServiceImpl implements DataSaveService {
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;

    @Override
    public void saveDevice(DeviceDTO deviceDTO) throws DeviceDuplicateException {
        if(deviceRepository.findDeviceByIdent(deviceDTO.getDeviceIdentification()) != null){
            throw new DeviceDuplicateException();
        }
        deviceRepository.save(new Device(null, deviceDTO.getDeviceIdentification()));
    }

    @Override
    public void saveUser(UserDTO userDTO) throws PinDuplicateException {
        // No duplicate check
        if(userRepository.findByPin(userDTO.getPin()).isPresent()){
            throw new PinDuplicateException();
        }
        userRepository.save(new User(null, userDTO.getFirstName(), userDTO.getLastName(), userDTO.getPin()));
    }
}