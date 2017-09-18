package ee.scanner.tablet.service;

import ee.scanner.tablet.dto.RegisterDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RentalServiceImpl implements RentalService {
    @Override
    public void retrieveDevices(RegisterDTO dto) {
        System.err.println("retrieve");
    }

    @Override
    public void returnDevices(RegisterDTO dto) {
        System.err.println("return");
    }
}
