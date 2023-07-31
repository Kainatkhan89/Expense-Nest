package com.example.expensenest.entity;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class Products {

    private static String UPLOAD_DIR = "src/main/resources/static/images/user-uploads";

    private int id;
    private String name;
    private double price;
    private int category;

    private MultipartFile imageFile;
    private String image;

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public void storeAndProcessImage () {
        MultipartFile file = getImageFile();
        if (!file.isEmpty()) {
            try {
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                File uploadedFile = new File(uploadDir.getAbsolutePath(), file.getOriginalFilename());
                if(!uploadedFile.exists()) {
                    file.transferTo(uploadedFile);
                }
                this.setImage("/images/user-uploads/" + file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
