package com.adityaPandit.zaika;

import com.adityaPandit.zaika.entities.FoodOrder;
import com.adityaPandit.zaika.entities.MenuItem;
import com.adityaPandit.zaika.entities.User;
import com.adityaPandit.zaika.repositories.FoodOrderRepository;
import com.adityaPandit.zaika.repositories.MenuItemRepository;
import com.adityaPandit.zaika.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FreeDbWorkflowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private FoodOrderRepository orderRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void registrationLoginMenuCartAndCheckoutPersistInMySql() throws Exception {
        String username = "zaika_test_" + UUID.randomUUID().toString().replace("-", "");
        String password = UUID.randomUUID().toString();

        try {
            mockMvc.perform(post("/register")
                            .param("username", username)
                            .param("password", password))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/login"));

            User registeredUser = userRepository.findByUsername(username);
            assertNotNull(registeredUser);
            assertTrue(passwordEncoder.matches(password, registeredUser.getPassword()));

            MvcResult login = mockMvc.perform(post("/doLogin")
                            .param("username", username)
                            .param("password", password))
                    .andExpect(status().is3xxRedirection())
                    .andReturn();
            MockHttpSession session = (MockHttpSession) login.getRequest().getSession(false);
            assertNotNull(session);

            mockMvc.perform(get("/user/dashboard").session(session))
                    .andExpect(status().isOk())
                    .andExpect(view().name("user_dashboard"));

            MenuItem menuItem = menuItemRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new AssertionError("The menu has no items"));
            mockMvc.perform(post("/user/update-cart")
                            .session(session)
                            .param("itemNames", menuItem.getName())
                            .param("quantities", "2"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/user/cart"));

            mockMvc.perform(get("/user/cart").session(session))
                    .andExpect(status().isOk())
                    .andExpect(content().string(org.hamcrest.Matchers.containsString(menuItem.getName())));

            mockMvc.perform(post("/user/place-order").session(session))
                    .andExpect(status().isOk())
                    .andExpect(view().name("user_order_success"));

            List<FoodOrder> savedOrders = orderRepository.findByUser_Username(username);
            assertEquals(1, savedOrders.size());
            FoodOrder savedOrder = savedOrders.get(0);
            BigDecimal expectedUnitPrice = BigDecimal.valueOf(menuItem.getPrice())
                    .setScale(2, RoundingMode.HALF_UP);
            assertEquals(expectedUnitPrice.multiply(BigDecimal.valueOf(2)), savedOrder.getTotalAmount());
            assertEquals(1, savedOrder.getItems().size());
            assertEquals(2, savedOrder.getItems().get(0).getQuantity());

            mockMvc.perform(get("/user/cart").session(session))
                    .andExpect(status().isOk())
                    .andExpect(content().string(org.hamcrest.Matchers.containsString("currently empty")));
        } finally {
            User testUser = userRepository.findByUsername(username);
            if (testUser != null) {
                orderRepository.deleteAll(orderRepository.findByUser_Username(username));
                userRepository.delete(testUser);
            }
        }
    }
}
