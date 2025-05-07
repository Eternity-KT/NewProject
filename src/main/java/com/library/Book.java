package com.library;

class Book {
    private String id;
    private String title;
    private String author;
    private String category;
    private int availableQuantity;

    public Book(String id, String title, String author, String category, int availableQuantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.availableQuantity = availableQuantity;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
}
