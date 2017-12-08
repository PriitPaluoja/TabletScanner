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
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.stream.Collectors;

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

    @GetMapping(value = "/users_csv", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public byte[] usersCsv(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "attachment; filename=users.csv");
        return ("User PIN, " +
                "First name," +
                "Last name, " +
                "Active," +
                "Total rented devices," +
                "Total rentals\n" +
                (dataSaveService.getAllUsers().getUsers().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("\n"))))
                .getBytes();
    }

    @GetMapping(value = "/devices_csv", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public byte[] devicesCsv(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "attachment; filename=devices.csv");
        return ("Device," +
                "Rentals," +
                "Active\n" +
                (dataSaveService.getAllDevices().getDevices().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("\n"))))
                .getBytes();
    }

    @GetMapping(value = "/active_csv", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public byte[] activeRentalsCsv(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "attachment; filename=active_rentals.csv");
        return ("PIN, " +
                "First name," +
                "Last name," +
                "Device," +
                "Date of rental\n" +
                (dataSaveService.getActiveRentals().stream()
                        .map(e -> e.getUser().getPin() + "," +
                                e.getUser().getFirstName() + "," +
                                e.getUser().getLastName() + "," +
                                e.getDevices().getDeviceIdentification() + "," +
                                e.getRentalTime())
                        .collect(Collectors.joining("\n"))))
                .getBytes();
    }

    @GetMapping(value = "/history_csv", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public byte[] historyRentalsCsv(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "attachment; filename=all_rentals.csv");
        return ("User PIN, " +
                "Retriever first name," +
                "Retriever last name, " +
                "Is user active?," +
                "Device," +
                "Is user active," +
                "Rental start," +
                "Rental end," +
                "Is returned," +
                "Returner PIN," +
                "Returner first name," +
                "Returner last name, " +
                "Is returner active?\n" +
                (dataSaveService.getAllRentals().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("\n"))))
                .getBytes();
    }
}