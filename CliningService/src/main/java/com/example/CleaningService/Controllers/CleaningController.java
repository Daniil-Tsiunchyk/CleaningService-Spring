package com.example.CleaningService.Controllers;

import com.example.CleaningService.Models.CleaningRequest;
import com.example.CleaningService.Models.Customer;
import com.example.CleaningService.Models.Service;
import com.example.CleaningService.Models.User;
import com.example.CleaningService.Repositories.CleaningRequestRepository;
import com.example.CleaningService.Repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@SessionAttributes("user")
public class CleaningController {

    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private CleaningRequestRepository cleaningRequestRepository;

    @GetMapping("/requestCleaning")
    public String showRequestCleaningForm(@RequestParam(value = "serviceId", required = false) Integer serviceId, Model model) {
        List<Service> services = serviceRepository.findAll();
        model.addAttribute("services", services);
        if (serviceId != null) {
            Service selectedService = serviceRepository.findById(serviceId).orElse(null);
            model.addAttribute("selectedService", selectedService);
        }
        return "requestCleaning";
    }

    @PostMapping("/submitRequest")
    public String submitCleaningRequest(@RequestParam("serviceId") int serviceId,
                                        @RequestParam("dateTime") String dateTime,
                                        @ModelAttribute("user") User user) {

        CleaningRequest request = new CleaningRequest();
        Service service = serviceRepository.findById(serviceId).orElse(null);
        if (service == null) {
            return "redirect:/requestCleaning";
        }

        request.setService(service);
        request.setDateTime(dateTime);
        request.setCustomer((Customer) user);
        cleaningRequestRepository.save(request);

        return "redirect:/requestCleaning";
    }

}

