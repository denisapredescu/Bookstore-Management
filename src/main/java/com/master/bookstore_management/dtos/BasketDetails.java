package com.master.bookstore_management.dtos;

import java.util.List;

public class BasketDetails {
    private String sent;
    private String email;
    private int cost;
    private List<BookFromBasketDetails> books;

    public BasketDetails(String sent, String email, int cost, List<BookFromBasketDetails> books) {
        this.sent = sent;
        this.email = email;
        this.cost = cost;
        this.books = books;
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
}
