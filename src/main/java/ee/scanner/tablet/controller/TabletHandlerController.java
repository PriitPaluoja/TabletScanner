package ee.scanner.tablet.controller;

import ee.scanner.tablet.RentalService;
import ee.scanner.tablet.dto.RegisterDTO;
import ee.scanner.tablet.feedback.FeedbackType;
import ee.scanner.tablet.util.ControllerUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TabletHandlerController {
    private final RentalService rentalService;

    @PostMapping("/")
    public String returnPage(@ModelAttribute("registerDTO") RegisterDTO dto,
                             @RequestParam(value = "op", required = false) String operationType,
                             Model model) {
        switch (operationType) {
            case "retrieve":
                rentalService.retrieveDevices(dto);
                break;
            case "return":
                rentalService.returnDevices(dto);
                break;
            default:
                ControllerUtil.setFeedback(model, FeedbackType.ERROR, "Viga sisestatud andmetes! Kas kasutasid veebilehel nuppe?");
        }
        return "scan";
    }
}
