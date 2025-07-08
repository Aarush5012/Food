package com.adityaPandit.zaika.controllers;

import com.adityaPandit.zaika.services.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final MenuService menuService;

    public HomeController(MenuService menuService) {
        this.menuService = menuService;
    }

    /** Landing page with User / Admin / Guest options */
    @GetMapping("/")
    public String homePage() {
        return "home";
    }

    /** Read‑only menu for guests (no login required) */
    @GetMapping("/guest/menu")
    public String guestMenuPage(Model model) {
        model.addAttribute("menuItems", menuService.getMenu());
        return "guest_menu";
    }
}
