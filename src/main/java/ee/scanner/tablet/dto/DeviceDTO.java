package ee.scanner.tablet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeviceDTO {

    private Integer id;

    @Length(max = 100)
    @NotBlank
    private String deviceIdentification;

    private Boolean active;

    private Integer totalRentals;

    @Override
    public String toString() {
        return deviceIdentification + ',' + totalRentals + ',' + active;
    }
}