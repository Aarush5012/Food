package com.adityaPandit.zaika.services;

import com.adityaPandit.zaika.entities.FoodOrder;
import com.adityaPandit.zaika.entities.FoodOrderItem;
import com.adityaPandit.zaika.entities.MenuItem;
import com.adityaPandit.zaika.entities.User;
import com.adityaPandit.zaika.repositories.FoodOrderRepository;
import com.adityaPandit.zaika.repositories.MenuItemRepository;
import com.adityaPandit.zaika.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class OrderService {

    private final FoodOrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

    public OrderService(FoodOrderRepository orderRepository,
                       MenuItemRepository menuItemRepository,
                       UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void placeOrder(String username, Map<String, Integer> cart) {
        if (cart.isEmpty()) {
            return;
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalStateException("Cannot place an order for an unknown user");
        }

        FoodOrder order = new FoodOrder(user, LocalDateTime.now());
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            MenuItem menuItem = menuItemRepository.findByNameIgnoreCase(entry.getKey());
            if (menuItem == null || entry.getValue() <= 0) {
                continue;
            }

            BigDecimal unitPrice = BigDecimal.valueOf(menuItem.getPrice())
                    .setScale(2, RoundingMode.HALF_UP);
            order.addItem(new FoodOrderItem(menuItem.getName(), entry.getValue(), unitPrice));
            total = total.add(unitPrice.multiply(BigDecimal.valueOf(entry.getValue())));
        }

        if (total.compareTo(BigDecimal.ZERO) > 0) {
            order.setTotalAmount(total);
            orderRepository.save(order);
        }
    }
}
