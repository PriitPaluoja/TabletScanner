package ee.scanner.tablet.controller;

import ee.scanner.tablet.service.DataSaveService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for handling various statistical queries. Accessible only to admin.
 */
@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
@PreAuthorize("hasRole('ADMIN')")
public class StatisticsController {
    private final DataSaveService dataSaveService;

    @GetMapping("/stat")
    public String statisticsPage(Model model) {
        model.addAttribute("rentals", dataSaveService.getActiveRentals());
        return "stat";
    }

    @GetMapping("/chart")
    public String chartsPage() {
        return "graphs";
    }

    @GetMapping("/history")
    public String historyPage(Model model) {
        model.addAttribute("rentalsHistory", dataSaveService.getAllRentals());
        return "history";
    }

    @GetMapping("/users")
    public String usersPage(Model model) {
        model.addAttribute("users", dataSaveService.getAllUsers());
        return "users";
    }

    @GetMapping("/devices")
    public String devicePage(Model model) {
        model.addAttribute("devices", dataSaveService.getAllDevices());
        return "devices";
    }

    @GetMapping("/chart_chart_device")
    @ResponseBody
    public List<List<String>> deviceChart() {
        return dataSaveService.getDeviceUsageStat();
    }

    @GetMapping("/chart_chart_user")
    @ResponseBody
    public List<List<String>> userChart() {
        return dataSaveService.getUserUsageStat();
    }

    @GetMapping("/chart_chart_month")
    @ResponseBody
    public List<List<Integer>> monthChart() {
        return dataSaveService.getMonthlyUsageStat();
    }

    @GetMapping("/chart_chart_day")
    @ResponseBody
    public List<Integer> dayHistogramChart() {
        return dataSaveService.getDeviceUsageCountHistPerDay();
    }
}