package ee.scanner.tablet.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DeviceDTO {

    private Integer id;

    @Length(max = 100)
    @NotBlank
    private String deviceIdentification;

    private Boolean active;
}