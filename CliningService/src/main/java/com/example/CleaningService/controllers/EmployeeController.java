package com.example.CleaningService.controllers;

import com.example.CleaningService.models.Employee;
import com.example.CleaningService.repositories.EmployeeRepository;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EmployeeController {

  private final EmployeeRepository employeeRepository;

  public EmployeeController(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  @GetMapping("/table-employees")
  public String showEmployees(Model model) {
    List<Employee> employees = employeeRepository.findAll();
    model.addAttribute("employees", employees);
    return "table-employees";
  }

  @GetMapping("/addEmployee")
  public String showAddEmployeeForm(Model model) {
    model.addAttribute("employee", new Employee());
    return "add-employee";
  }

  @PostMapping("/addEmployee")
  public String addEmployee(@ModelAttribute Employee employee) {
    employeeRepository.save(employee);
    return "redirect:/table-employees";
  }

  @GetMapping("/edit-employee/{id}")
  public String showEditEmployeeForm(@PathVariable("id") long id, Model model) {
    Employee employee = employeeRepository
      .findById(id)
      .orElseThrow(() ->
        new IllegalArgumentException("Invalid employee Id:" + id)
      );
    model.addAttribute("employee", employee);
    return "edit-employee";
  }

  @PostMapping("/edit-employee")
  public String updateEmployee(@ModelAttribute Employee employee) {
    employeeRepository.save(employee);
    return "redirect:/table-employees";
  }

  @GetMapping("/delete-employee/{id}")
  public String deleteEmployee(@PathVariable("id") long id) {
    Employee employee = employeeRepository
      .findById(id)
      .orElseThrow(() ->
        new IllegalArgumentException("Invalid service Id:" + id)
      );
    employeeRepository.delete(employee);
    return "redirect:/table-employees";
  }
}
