package vn.edu.stu.doangk_qlsach.model;

import java.io.Serializable;

public class Book implements Serializable {
    private int id;
    private String title;
    private int categoryId;
    private String imagePath;
    private String author;
    private int publishYear;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public Book(int id, String title, int categoryId, String imagePath, String author, int publishYear) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
        this.imagePath = imagePath;
        this.author = author;
        this.publishYear = publishYear;
    }

    public Book(String title, int categoryId, String imagePath, String author, int publishYear) {
        this.title = title;
        this.categoryId = categoryId;
        this.imagePath = imagePath;
        this.author = author;
        this.publishYear = publishYear;
    }

    public Book() {
    }

    @Override
    public String toString() {
        return "Ma: " + id + "\nTen: " + title + "\nThe loai: " +
                categoryId + "\nTac gia: " + author + "\nNam xb: " + publishYear;
    }
}
