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
public class UserManagementDTO {
    private Integer id;

    @Length(max = 50)
    @NotBlank
    private String firstName;

    @Length(max = 50)
    @NotBlank
    private String lastName;

    private Integer totalRentals;

    @Length(max = 10)
    private String pin;

    private Boolean active;

    @Override
    public String toString() {
        return pin + "," + firstName + "," + lastName + "," + active + "," + totalRentals;
    }
}
