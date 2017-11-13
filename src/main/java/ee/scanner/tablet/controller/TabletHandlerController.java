package ee.scanner.tablet.controller;

import ee.scanner.tablet.dto.RegisterDTO;
import ee.scanner.tablet.exception.NoActiveRentalsFoundException;
import ee.scanner.tablet.exception.NoDeviceFoundException;
import ee.scanner.tablet.exception.NoUserFoundException;
import ee.scanner.tablet.exception.SomeDeviceNotFoundException;
import ee.scanner.tablet.feedback.FeedbackType;
import ee.scanner.tablet.service.RentalService;
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
                try {
                    rentalService.takeDevices(dto);
                    ControllerUtil.setFeedback(model, FeedbackType.SUCCESS, "Seadmed väljastatud");
                } catch (NoUserFoundException e) {
                    ControllerUtil.setFeedback(model, FeedbackType.ERROR, "Sisestatud PIN-iga kasutajat ei leitud!");
                } catch (NoDeviceFoundException e) {
                    ControllerUtil.setFeedback(model, FeedbackType.ERROR, "Sisestatud seadmeid ei leitud!");
                } catch (SomeDeviceNotFoundException e) {
                    ControllerUtil.setFeedback(model, FeedbackType.ERROR, "Sisestatud seadmete arv ei vasta tegelikele laenutatud seadmetele!");
                    ControllerUtil.setFeedback(model, FeedbackType.SUCCESS, "Seadmed väljastatud");
                }
                dto.setDevices("");
                dto.setPersonInformation("");
                break;
            case "return":
                try {
                    rentalService.returnDevices(dto);
                    ControllerUtil.setFeedback(model, FeedbackType.SUCCESS, "Seadmed tagastatud!");
                    dto.setDevices("");
                    dto.setPersonInformation("");
                } catch (NoActiveRentalsFoundException e) {
                    ControllerUtil.setFeedback(model, FeedbackType.ERROR, "Sisestatud seadmetega ei ole seotud ühtegi laenutust!");
                } catch (NoUserFoundException e) {
                    ControllerUtil.setFeedback(model, FeedbackType.ERROR, "Sisestatud PIN ei ole õige!");
                }
                break;
            default:
                ControllerUtil.setFeedback(model, FeedbackType.ERROR, "Viga sisestatud andmetes! Kas kasutasid veebilehel nuppe?");
        }
        return "scan";
    }
}
