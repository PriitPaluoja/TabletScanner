package ee.scanner.tablet.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private DeviceUser user;

    @JoinColumn(name = "device_id")
    @ManyToOne
    private Device device;

    private LocalDateTime rentalTime;

    private LocalDateTime returnTime;

    private Boolean isReturned;

    @JoinColumn(name = "returner")
    @ManyToOne
    private DeviceUser returner;
}
