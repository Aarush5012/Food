package com.adityaPandit.zaika.services;

import com.adityaPandit.zaika.entities.MenuItem;
import com.adityaPandit.zaika.repositories.MenuItemRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MenuService {

    private static final Map<String, String> DEFAULT_IMAGES = Map.of(
            "Paneer Butter Masala", "/images/paneer.jpg",
            "Chicken Biryani", "/images/chicken-karahi-kadai.jpg",
            "Veg Thali", "/images/thali.jpg",
            "Butter Naan", "/images/naan.jpg",
            "Gulab Jamun", "/images/jamun.jpg"
    );

    private final MenuItemRepository menuRepo;

    public MenuService(MenuItemRepository menuRepo) {
        this.menuRepo = menuRepo;
    }

    @PostConstruct
    public void init() {
        List<MenuItem> defaultMenu = List.of(
                new MenuItem("Paneer Butter Masala", 250.0, DEFAULT_IMAGES.get("Paneer Butter Masala"), 4.5),
                new MenuItem("Chicken Biryani", 280.0, DEFAULT_IMAGES.get("Chicken Biryani"), 4.8),
                new MenuItem("Veg Thali", 150.0, DEFAULT_IMAGES.get("Veg Thali"), 4.0),
                new MenuItem("Butter Naan", 20.0, DEFAULT_IMAGES.get("Butter Naan"), 4.1),
                new MenuItem("Gulab Jamun", 60.0, DEFAULT_IMAGES.get("Gulab Jamun"), 4.6)
        );

        if (menuRepo.count() == 0) {
            menuRepo.saveAll(defaultMenu);
            return;
        }

        for (MenuItem existing : menuRepo.findAll()) {
            String replacement = DEFAULT_IMAGES.get(existing.getName());
            if (replacement != null && !replacement.equals(existing.getImagePath())) {
                existing.setImagePath(replacement);
                menuRepo.save(existing);
            }
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
