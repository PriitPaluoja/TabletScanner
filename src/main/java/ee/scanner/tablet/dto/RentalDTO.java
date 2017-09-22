package ee.scanner.tablet.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RentalDTO {
    private Integer id;
    private UserDTO user;
    private DeviceDTO devices;
    private LocalDateTime rentalTime;
    private LocalDateTime returnTime;
    private Boolean isReturned;
    private UserDTO returner;
}
