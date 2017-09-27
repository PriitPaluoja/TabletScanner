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
public class UserDTO {

    private Integer id;

    @Length(max = 50)
    @NotBlank
    private String firstName;

    @Length(max = 50)
    @NotBlank
    private String lastName;

    @Length(max = 10)
    @NotBlank
    private String pin;

    private Boolean active;

}