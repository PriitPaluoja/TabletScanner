package ee.scanner.tablet;

import ee.scanner.tablet.dto.RegisterDTO;
import org.springframework.stereotype.Service;

@Service
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
