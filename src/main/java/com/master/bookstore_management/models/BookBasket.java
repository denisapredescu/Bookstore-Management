package com.master.bookstore_management.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "book_baskets")
public class BookBasket {
    @Id
    @GeneratedValue
    private int id;

    @Column(name = "copies")
    @Min(value = 1)
    private int copies;

    @Column(name = "price")
    private int price = 0;

    @ManyToOne(targetEntity = Book.class)
    @PrimaryKeyJoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(targetEntity = Basket.class)
    @PrimaryKeyJoinColumn(name = "basket_id")
    private Basket basket;

    public BookBasket() {
    }

    public BookBasket(int id, int copies, int price, Book book, Basket basket) {
        this.id = id;
        this.copies = copies;
        this.price = price;
        this.book = book;
        this.basket = basket;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }
}
