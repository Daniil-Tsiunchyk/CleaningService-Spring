package com.example.CleaningService.Controllers;

import com.example.CleaningService.Models.Inventory;
import com.example.CleaningService.Repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class InventoryController {

    @Autowired
    private InventoryRepository inventoryRepository;

    @GetMapping("/table-inventory")
    public String showInventories(Model model) {
        List<Inventory> inventories = inventoryRepository.findAll();
        model.addAttribute("inventories", inventories);
        return "table-inventory";
    }

    @GetMapping("/addInventory")
    public String showAddInventoryForm(Model model) {
        model.addAttribute("inventory", new Inventory());
        return "add-inventory";
    }

    @PostMapping("/addInventory")
    public String addInventory(@ModelAttribute Inventory inventory) {
        inventoryRepository.save(inventory);
        return "redirect:/table-inventory";
    }

    @GetMapping("/edit-inventory/{id}")
    public String showEditInventoryForm(@PathVariable("id") long id, Model model) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));
        model.addAttribute("inventory", inventory);
        return "edit-inventory";
    }

    @PostMapping("/edit-inventory/{id}")
    public String updateInventory(@PathVariable("id") long id, @ModelAttribute Inventory inventory) {
        Inventory existingInventory = inventoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid service Id:" + id));
        existingInventory.setName(inventory.getName());
        existingInventory.setQuantity(inventory.getQuantity());
        inventoryRepository.save(existingInventory);
        return "redirect:/table-inventory";
    }


    @GetMapping("/delete-inventory/{id}")
    public String deleteInventory(@PathVariable("id") long id) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid service Id:" + id));
        inventoryRepository.delete(inventory);
        return "redirect:/table-inventory";
    }
}

