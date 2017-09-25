package ee.scanner.tablet.controller;

import ee.scanner.tablet.service.DataSaveService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StatisticsController {
    private final DataSaveService dataSaveService;

    @GetMapping("/stat")
    public String statisticsPage(Model model) {
        model.addAttribute("rentals", dataSaveService.getActiveRentals());
        return "stat";
    }

    @GetMapping("/history")
    public String historyPage(Model model) {
        model.addAttribute("rentalsHistory", dataSaveService.getAllRentals());
        return "history";
    }
}
