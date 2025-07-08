package com.adityaPandit.zaika.services;

import com.adityaPandit.zaika.entities.MenuItem;
import com.adityaPandit.zaika.repositories.MenuItemRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    private final MenuItemRepository menuRepo;

    public MenuService(MenuItemRepository menuRepo) {
        this.menuRepo = menuRepo;
    }

    @PostConstruct
    public void init() {
        if (menuRepo.count() == 0) {
            menuRepo.saveAll(List.of(
                    new MenuItem("Paneer Butter Masala", 250.0, "/images/paneer.png", 4.5),
                    new MenuItem("Chicken Biryani", 280.0, "/images/biryani.png", 4.8),
                    new MenuItem("Veg Thali", 150.0, "/images/thali.png", 4.0),
                    new MenuItem("Butter Naan", 20.0, "/images/naan.png", 4.1),
                    new MenuItem("Gulab Jamun", 60.0, "/images/gulabjamun.png", 4.6)
            ));
        }
    }

    public List<MenuItem> getMenu() {
        return menuRepo.findAll();
    }

    public void updateAllPrices(List<MenuItem> updatedItems) {
        for (MenuItem updated : updatedItems) {
            Optional<MenuItem> existing = menuRepo.findById(updated.getId());
            existing.ifPresent(menu -> {
                menu.setPrice(updated.getPrice());
                menuRepo.save(menu);
            });
        }
    }
}
