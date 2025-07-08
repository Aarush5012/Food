package com.adityaPandit.zaika.controllers;

import com.adityaPandit.zaika.entities.MenuItem;
import com.adityaPandit.zaika.services.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.util.*;

/**
 * Handles user actions like viewing menu, adding to cart, viewing cart, and placing orders.
 */
@Controller
@RequestMapping("/user")
@SessionScope
public class UserDashboardController {

    @Autowired
    private MenuService menuService;

    // Cart stores itemName → quantity (You may change to itemId later)
    private final Map<String, Integer> cart = new HashMap<>();

    /**
     * Show menu to user.
     */
    @GetMapping("/dashboard")
    public String userDashboard(Model model) {
        List<MenuItem> menu = menuService.getMenu();

        // Update quantities from cart (for display only)
        for (MenuItem item : menu) {
            item.setQuantity(cart.getOrDefault(item.getName(), 0));
        }

        model.addAttribute("menuItems", menu);
        return "user_dashboard";
    }

    /**
     * Add item to cart or update quantity.
     */
    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam String itemName,
                            @RequestParam int quantity,
                            HttpServletRequest request) {
        if (quantity <= 0) return "redirect:/user/dashboard";
        cart.put(itemName, cart.getOrDefault(itemName, 0) + quantity);

        // Update cart count in session
        request.getSession().setAttribute("cartSize", getCartSize());

        return "redirect:/user/dashboard";
    }

    /**
     * View all items in cart with totals.
     */
    @GetMapping("/cart")
    public String viewCart(Model model) {
        List<MenuItem> menu = menuService.getMenu();
        List<MenuItem> cartItems = new ArrayList<>();
        double total = 0.0;

        for (MenuItem item : menu) {
            String name = item.getName();
            if (cart.containsKey(name)) {
                int qty = cart.get(name);
                MenuItem itemCopy = new MenuItem(
                        item.getName(),
                        item.getPrice(),
                        item.getImagePath(),
                        item.getRating()
                );
                itemCopy.setQuantity(qty);
                total += qty * item.getPrice();
                cartItems.add(itemCopy);
            }
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "user_cart";
    }

    /**
     * Remove a single item from cart.
     */
    @PostMapping("/remove-from-cart")
    public String removeFromCart(@RequestParam String itemName,
                                 HttpServletRequest request) {
        cart.remove(itemName);

        // Update cart count in session
        request.getSession().setAttribute("cartSize", getCartSize());

        return "redirect:/user/cart";
    }

    /**
     * Place the order and clear the cart.
     */
    @PostMapping("/place-order")
    public String placeOrder(Model model,
                             HttpServletRequest request) {
        List<MenuItem> menu = menuService.getMenu();
        List<MenuItem> orderItems = new ArrayList<>();
        double total = 0.0;

        for (MenuItem item : menu) {
            String name = item.getName();
            if (cart.containsKey(name)) {
                int qty = cart.get(name);
                MenuItem itemCopy = new MenuItem(
                        item.getName(),
                        item.getPrice(),
                        item.getImagePath(),
                        item.getRating()
                );
                itemCopy.setQuantity(qty);
                total += qty * item.getPrice();
                orderItems.add(itemCopy);
            }
        }

        cart.clear(); // Empty cart after placing order
        request.getSession().setAttribute("cartSize", 0); // Reset cartSize in session

        model.addAttribute("orderItems", orderItems);
        model.addAttribute("total", total);
        return "user_order_success";
    }

    /**
     * Update quantity of items in cart (from cart page).
     */
    @PostMapping("/update-cart")
    public String updateCart(@RequestParam("itemNames") List<String> itemNames,
                             @RequestParam("quantities") List<Integer> quantities,
                             HttpServletRequest request) {
        for (int i = 0; i < itemNames.size(); i++) {
            String name = itemNames.get(i);
            int qty = quantities.get(i);

            if (qty <= 0) {
                cart.remove(name); // Remove if qty is invalid or zero
            } else {
                cart.put(name, qty); // Update with new quantity
            }
        }

        // Update cart count in session
        request.getSession().setAttribute("cartSize", getCartSize());

        return "redirect:/user/cart";
    }

    /**
     * Utility to get total quantity of all items in the cart.
     */
    public int getCartSize() {
        return cart.values().stream().mapToInt(Integer::intValue).sum();
    }
}
