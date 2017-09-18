package ee.scanner.tablet.controller;

import ee.scanner.tablet.dto.RegisterDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class IndexController {
    @GetMapping("/")
    public String scanPage(@ModelAttribute("registerDTO") RegisterDTO dto) {
        return "scan";
    }

    @GetMapping(value = "/login")
    public String loginPage() {
        return "login";
    }
}
