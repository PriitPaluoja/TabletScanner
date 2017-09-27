package ee.scanner.tablet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceWrapperDTO {

    @Valid
    private List<DeviceDTO> devices = new ArrayList<>();
}
