package ee.scanner.tablet.controller;

import ee.scanner.tablet.service.DataSaveService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@RestController
public class InfoController {
    private final DataSaveService dataSaveService;

    @GetMapping("/exists")
    public Boolean userExists(@RequestParam("pin") String pin) {
        return dataSaveService.userExists(pin);
    }
}
