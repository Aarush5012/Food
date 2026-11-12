package com.adityaPandit.zaika.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "food_orders")
public class FoodOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "foodOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodOrderItem> items = new ArrayList<>();

    protected FoodOrder() {
    }

    public FoodOrder(User user, LocalDateTime createdAt) {
        this.user = user;
        this.createdAt = createdAt;
        this.totalAmount = BigDecimal.ZERO;
    }

    public void addItem(FoodOrderItem item) {
        items.add(item);
        item.setFoodOrder(this);
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public User getUser() {
        return user;
    }

    public List<FoodOrderItem> getItems() {
        return items;
    }
}
