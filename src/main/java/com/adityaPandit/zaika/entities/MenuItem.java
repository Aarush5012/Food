package com.adityaPandit.zaika.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "menu_items")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;
    private String imagePath;
    private double rating;

    @Transient
    private int quantity;

    public MenuItem() {}

    public MenuItem(String name, double price, String imagePath, double rating) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.rating = rating;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
