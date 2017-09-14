package ee.scanner.tablet.domain;

import java.time.LocalDateTime;

public class Rental {
    private Integer id;
    private User user;
    private DeviceUser deviceUser;
    private LocalDateTime rentalTime;
}
