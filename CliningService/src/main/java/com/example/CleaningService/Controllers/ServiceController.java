package com.example.CleaningService.Controllers;

import com.example.CleaningService.Models.Service;
import com.example.CleaningService.Repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ServiceController {
    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping("/table-services")
    public String showServices(Model model) {
        List<Service> services = serviceRepository.findAll();
        model.addAttribute("services", services);
        return "table-services";
    }

    @GetMapping("/addService")
    public String showAddServiceForm(Model model) {
        model.addAttribute("service", new Service());
        return "add-service";
    }

    @PostMapping("/addService")
    public String addService(@ModelAttribute Service service) {
        serviceRepository.save(service);
        return "redirect:/table-services";
    }

    @GetMapping("/edit-service/{id}")
    public String showEditServiceForm(@PathVariable("id") long id, Model model) {
        Service service = serviceRepository.findById((int) id).orElseThrow(() -> new IllegalArgumentException("Invalid service Id:" + id));
        model.addAttribute("service", service);
        return "edit-service";
    }

    @PostMapping("/edit-service")
    public String updateService(@ModelAttribute Service service) {
        serviceRepository.save(service);
        return "redirect:/table-services";
    }

    @GetMapping("/delete-service/{id}")
    public String deleteService(@PathVariable("id") long id, Model model) {
        Service service = serviceRepository.findById((int) id).orElseThrow(() -> new IllegalArgumentException("Invalid service Id:" + id));
        serviceRepository.delete(service);
        return "redirect:/table-services";
    }
}

