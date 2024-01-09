package com.master.bookstore_management.dtos;

import java.util.List;

public class BasketDetails {
    private int id;
    private String sent;
    private int userId;
    private String email;
    private int cost;
    private List<BookFromBasketDetails> books;

    public BasketDetails(int id, String sent, int userId, String email, int cost, List<BookFromBasketDetails> books) {
        this.id = id;
        this.sent = sent;
        this.userId = userId;
        this.email = email;
        this.cost = cost;
        this.books = books;
    }

    public int getId() {
        return id;
    }

    public String getSent() {
        return sent;
    }

    public String getEmail() {
        return email;
    }

    public int getCost() {
        return cost;
    }

    public List<BookFromBasketDetails> getBooks() {
        return books;
    }

    public int getUserId() {
        return userId;
    }
}
