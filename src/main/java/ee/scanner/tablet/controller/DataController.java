package ee.scanner.tablet.controller;

import ee.scanner.tablet.dto.DeviceDTO;
import ee.scanner.tablet.dto.UserDTO;
import ee.scanner.tablet.feedback.FeedbackType;
import ee.scanner.tablet.service.DataSaveService;
import ee.scanner.tablet.util.ControllerUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DataController {
    private final DataSaveService dataSaveService;

    @GetMapping("/insert")
    public String insertPage(@ModelAttribute("userDTO") UserDTO userDTO,
                             @ModelAttribute("deviceDTO") DeviceDTO deviceDTO) {
        return "insert";
    }

    @PostMapping("/insert_device")
    public String insertDevice(@ModelAttribute("userDTO") UserDTO userDTO,
                               @Valid @ModelAttribute("deviceDTO") DeviceDTO deviceDTO,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            ControllerUtil.setFeedback(model, FeedbackType.ERROR, "Seadme identifikaator ei ole korrektne!");
        } else {
            dataSaveService.saveDevice(deviceDTO);
            ControllerUtil.setFeedback(model, FeedbackType.SUCCESS, "Seade salvestatud!");
        }
        return "insert";
    }

    @PostMapping("/insert_user")
    public String insertUser(@ModelAttribute("deviceDTO") DeviceDTO deviceDTO,
                             @Valid @ModelAttribute("userDTO") UserDTO userDTO, BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            ControllerUtil.setFeedback(model, FeedbackType.ERROR, "Kasutajaga seotud andmed ei ole korreksed!");
        } else {
            dataSaveService.saveUser(userDTO);
            ControllerUtil.setFeedback(model, FeedbackType.SUCCESS, "Uus kasutaja salvestatud!");
        }
        return "insert";
    }
}