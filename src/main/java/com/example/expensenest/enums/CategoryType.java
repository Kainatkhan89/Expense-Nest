package com.example.expensenest.enums;

public enum CategoryType {
    ELECTRONIC_ACCESSORIES("Electronic Accessories"),
    ELECTRONICS("Electronics"),
    FRESH_FOOD_AND_GROCERY("Fresh Food & Grocery"),
    PHARMACEUTICAL("Pharmaceuticals"),
    FRUITS_AND_VEGETABLES("Fruits & Vegetables"),
    MEAT_AND_DAIRY("Meat & Dairy Products");

    public final String value;

    CategoryType(String value) {
        this.value = value;
    }

    public CategoryType getMapping(String value) {
        CategoryType result = null;
        for (CategoryType category : CategoryType.values()) {
            if (category.value.equalsIgnoreCase(value)) {
                result = category;
                break;
            }
        }
        return result;
    }
}
