package com.adityaPandit.zaika.services;

import com.adityaPandit.zaika.entities.FoodOrder;
import com.adityaPandit.zaika.entities.MenuItem;
import com.adityaPandit.zaika.entities.User;
import com.adityaPandit.zaika.repositories.FoodOrderRepository;
import com.adityaPandit.zaika.repositories.MenuItemRepository;
import com.adityaPandit.zaika.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private FoodOrderRepository orderRepository;
    @Mock
    private MenuItemRepository menuItemRepository;
    @Mock
    private UserRepository userRepository;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, menuItemRepository, userRepository);
    }

    @Test
    void placeOrderPersistsUserAndPriceSnapshot() {
        User user = new User();
        when(userRepository.findByUsername("alice")).thenReturn(user);
        when(menuItemRepository.findByNameIgnoreCase("Curry"))
                .thenReturn(new MenuItem("Curry", 12.5, "/images/curry.png", 4.5));

        orderService.placeOrder("alice", Map.of("Curry", 2));

        ArgumentCaptor<FoodOrder> orderCaptor = ArgumentCaptor.forClass(FoodOrder.class);
        verify(orderRepository).save(orderCaptor.capture());
        FoodOrder order = orderCaptor.getValue();
        assertEquals(user, order.getUser());
        assertEquals(new BigDecimal("25.00"), order.getTotalAmount());
        assertEquals(1, order.getItems().size());
        assertEquals("Curry", order.getItems().get(0).getItemName());
        assertEquals(2, order.getItems().get(0).getQuantity());
        assertEquals(new BigDecimal("12.50"), order.getItems().get(0).getUnitPrice());
    }

    @Test
    void emptyCartDoesNotCreateAnOrder() {
        orderService.placeOrder("alice", Map.of());

        verifyNoInteractions(orderRepository, menuItemRepository, userRepository);
    }
}
