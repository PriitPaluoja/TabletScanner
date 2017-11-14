package ee.scanner.tablet.controller;

import ee.scanner.tablet.dto.DeviceDTO;
import ee.scanner.tablet.dto.DeviceWrapperDTO;
import ee.scanner.tablet.dto.UserDTO;
import ee.scanner.tablet.dto.UserWrapperDTO;
import ee.scanner.tablet.exception.DeviceDuplicateException;
import ee.scanner.tablet.exception.IdNotPresentException;
import ee.scanner.tablet.exception.PinDuplicateException;
import ee.scanner.tablet.exception.PinNotPresentException;
import ee.scanner.tablet.feedback.FeedbackType;
import ee.scanner.tablet.service.DataSaveService;
import ee.scanner.tablet.util.ControllerUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * Controller for inserting data into the database. Accessible only to admin.
 */
@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
@PreAuthorize("hasRole('ADMIN')")
public class DataController {
    private final DataSaveService dataSaveService;

    @GetMapping("/insert")
    public String insertPage(@ModelAttribute("userDTO") UserDTO usr, @ModelAttribute("deviceDTO") DeviceDTO dev) {
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
            try {
                dataSaveService.saveDevice(deviceDTO);
                ControllerUtil.setFeedback(model, FeedbackType.SUCCESS, "Seade salvestatud!");
            } catch (DeviceDuplicateException e) {
                ControllerUtil.setFeedback(model, FeedbackType.ERROR, "Seade juba eksisteerib!");
            }
        }
        return "insert";
    }

    @PostMapping("/insert_user")
    public String insertUser(@ModelAttribute("deviceDTO") DeviceDTO deviceDTO,
                             @Valid @ModelAttribute("userDTO") UserDTO userDTO,
                             BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            ControllerUtil.setFeedback(model, FeedbackType.ERROR, "Kasutajaga seotud andmed ei ole korreksed!");
        } else {
            try {
                dataSaveService.saveUser(userDTO);
                ControllerUtil.setFeedback(model, FeedbackType.SUCCESS, "Uus kasutaja salvestatud!");
            } catch (PinDuplicateException e) {
                ControllerUtil.setFeedback(model, FeedbackType.ERROR, "Antud PIN on juba kasutusel! Vali uus PIN!");
            }
        }
        return "insert";
    }

    @PostMapping("/update_user")
    public String updateUsers(@Valid @ModelAttribute("users") UserWrapperDTO dto,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            ControllerUtil.setFeedback(model, FeedbackType.ERROR, "Sisestatud andmed ei ole korreksed!");
        } else {
            try {
                dataSaveService.updateUsers(dto);
                ControllerUtil.setFeedback(model, FeedbackType.SUCCESS, "Kasutaja uuendatud!");
            } catch (IdNotPresentException e) {
                ControllerUtil.setFeedback(model, FeedbackType.ERROR, "Kasutajat ei leitud andmebaasist!");
            } catch (PinNotPresentException e) {
                ControllerUtil.setFeedback(model, FeedbackType.ERROR, "Aktiivsel kasutajal on PIN-kood kohustuslik!");
            } catch (PinDuplicateException e) {
                ControllerUtil.setFeedback(model, FeedbackType.ERROR, "Sisestatud PIN-kood ei ole unikaalne!");
            }
        }

        model.addAttribute("users", dataSaveService.getAllUsers());
        return "users";
    }

    @PostMapping("/update_devices")
    public String updateDevices(@Valid @ModelAttribute("devices") DeviceWrapperDTO dto,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            ControllerUtil.setFeedback(model, FeedbackType.ERROR, "Sisestatud seadmed ei ole korreksed!");
        } else {
            try {
                dataSaveService.updateDevices(dto);
                ControllerUtil.setFeedback(model, FeedbackType.SUCCESS, "Seade uuendatud!");
            } catch (IdNotPresentException e) {
                ControllerUtil.setFeedback(model, FeedbackType.ERROR, "Seadet ei leitud andmebaasist!");
            } catch (DeviceDuplicateException e) {
                ControllerUtil.setFeedback(model, FeedbackType.ERROR, "Sisestatud identifikaator on juba andmebaasis!");
            }
        }
        model.addAttribute("devices", dataSaveService.getAllDevices());
        return "devices";
    }
}