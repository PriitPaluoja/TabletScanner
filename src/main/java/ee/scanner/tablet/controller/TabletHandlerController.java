package ee.scanner.tablet.controller;

import ee.scanner.tablet.dto.RegisterDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TabletHandlerController {

    @PostMapping("/")
    public String returnPage(@ModelAttribute("registerDTO") RegisterDTO dto,
                             @RequestParam(value = "op", required = false) String operationType) {
        switch (operationType) {
            case "retrieve":
                System.err.println("retrieve");
                break;
            case "return":
                System.err.println("return");
                break;
            default:
                //TODO: error
        }

        System.err.println(dto);
        return "scan";
    }
}
