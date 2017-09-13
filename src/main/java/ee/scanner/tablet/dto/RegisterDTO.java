package ee.scanner.tablet.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RegisterDTO {
    private String personInformation;
    private String devices;
}
