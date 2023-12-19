package com.master.bookstore_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue
    private int id;

    @Column(name="name")
    @NotNull
    @NotEmpty(message = "The name cannot be blank!")
    private String name;

    @Column(name = "price")
    @Min(value = 0)
    @NotEmpty(message = "The price cannot be blank!")
    @NotNull
    private double price;

    @Column(name = "year")
    @Min(value = 0)
    private int year;

    @Column(name = "copies")
    @Min(value = 0)
    private int copies;

    @Column(name = "volume")
    @Min(value = 0)
    private int volume = 0;

    @Column(name = "series_name")
    private String series_name = null;

    @ManyToOne(targetEntity = Author.class)
    @PrimaryKeyJoinColumn(name = "author_id")
//    @JoinTable(name = "authors")
    private Author author;

    @ManyToMany(targetEntity = Category.class)
    private List<Category> bookCategories = null;

//    @OneToMany(targetEntity = BookBasket.class)
//    @PrimaryKeyJoinColumn(name = "book_basket_id")
////    @JoinTable(name = "authors")
//    private List<BookBasket> bookBaskets = null;

    public Book() {
    }

//    public Book(int id, String name, double price, int year, int copies, int volume, String series_name, Author author, List<Category> bookCategories, List<BookBasket> bookBaskets) {
//        this.id = id;
//        this.name = name;
//        this.price = price;
//        this.year = year;
//        this.copies = copies;
//        this.volume = volume;
//        this.series_name = series_name;
//        this.author = author;
//        this.bookCategories = bookCategories;
//        this.bookBaskets = bookBaskets;
//    }

    public Book(int id,
                String name,
                double price,
                int year,
                int copies,
                int volume,
                String series_name,
                Author author) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.year = year;
        this.copies = copies;
        this.volume = volume;
        this.series_name = series_name;
        this.author = author;
    }

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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getSeries_name() {
        return series_name;
    }

    public void setSeries_name(String series_name) {
        this.series_name = series_name;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Category> getBookCategories() {
        return bookCategories;
    }

    public void setBookCategories(List<Category> bookCategories) {
        this.bookCategories = bookCategories;
    }

//    public List<BookBasket> getBookBaskets() {
//        return bookBaskets;
//    }
//
//    public void setBookBaskets(List<BookBasket> bookBaskets) {
//        this.bookBaskets = bookBaskets;
//    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Book book = (Book) o;
//        return id == book.id && Double.compare(price, book.price) == 0 && year == book.year && copies == book.copies && volume == book.volume && Objects.equals(name, book.name) && Objects.equals(series_name, book.series_name) && Objects.equals(author, book.author) && Objects.equals(bookCategories, book.bookCategories) && Objects.equals(bookBaskets, book.bookBaskets);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, name, price, year, copies, volume, series_name, author, bookCategories, bookBaskets);
//    }

//    @Override
//    public String toString() {
//        return "Book{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", price=" + price +
//                ", year=" + year +
//                ", copies=" + copies +
//                ", volume=" + volume +
//                ", series_name='" + series_name + '\'' +
//                ", author=" + author +
//                ", bookCategories=" + bookCategories +
//                ", bookBaskets=" + bookBaskets +
//                '}';
//    }
}
