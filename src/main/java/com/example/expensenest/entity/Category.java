package com.example.expensenest.entity;

import com.example.expensenest.enums.CategoryType;

public class Category {
    private int id;
    private String name;
    private String image;
    private int totalProducts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public String formatImageData(String typeImage) {
        CategoryType result = null;
        for (CategoryType category : CategoryType.values()) {
            if (category.value.equalsIgnoreCase(typeImage)) {
                result = category;
                break;
            }
        }

        switch(result) {
            case ELECTRONICS:
                return "/images/electronics.png";
            case ELECTRONIC_ACCESSORIES:
                return "/images/electronic-accessories.png";
            case FRESH_FOOD_AND_GROCERY:
                return "/images/food-grocery.png";
            case FRUITS_AND_VEGETABLES:
                return "/images/fruits.png";
            case MEAT_AND_DAIRY:
                return "/images/dairy.png";
            default:
                return null;
        }
    }
}
