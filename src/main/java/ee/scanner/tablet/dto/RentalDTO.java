package ee.scanner.tablet.dto;


import ee.scanner.tablet.domain.Device;
import ee.scanner.tablet.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RentalDTO {
    private Integer id;
    private User user;
    private List<Device> devices = new ArrayList<>();
    private LocalDateTime rentalTime;
    private LocalDateTime returnTime;
    private Boolean isReturned;
}
