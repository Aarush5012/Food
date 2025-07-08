package com.adityaPandit.zaika.controllers;

import com.adityaPandit.zaika.entities.MenuItem;
import com.adityaPandit.zaika.services.MenuService;
import com.adityaPandit.zaika.services.UserService;
import com.adityaPandit.zaika.wrapper.MenuItemWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final MenuService menuService;
    private final UserService userService;

    public AdminController(MenuService menuService, UserService userService) {
        this.menuService = menuService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin_dashboard";
    }

    @GetMapping("/menu")
    public String viewMenu(Model model) {
        MenuItemWrapper wrapper = new MenuItemWrapper();
        wrapper.setMenuItems(menuService.getMenu());
        model.addAttribute("menuItems", wrapper);
        return "admin_menu";
    }

    @PostMapping("/menu/update-prices")
    public String updatePrices(@ModelAttribute("menuItems") MenuItemWrapper wrapper) {
        menuService.updateAllPrices(wrapper.getMenuItems());
        return "redirect:/admin/menu";
    }

    @GetMapping("/users")
    public String viewAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin_users";
    }
}
