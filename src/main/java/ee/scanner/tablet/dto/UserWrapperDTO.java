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
public class UserWrapperDTO {

    @Valid
    private List<UserDTO> users = new ArrayList<>();
}
