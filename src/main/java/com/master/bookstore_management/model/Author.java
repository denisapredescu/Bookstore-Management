package com.master.bookstore_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @NotNull
    @GeneratedValue
    private Integer id;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "nationality")
    private String nationality;

//    @OneToMany(targetEntity = Book.class)
//    private List<Book> books;

    public Author() {
    }

//    public Author(Integer id, String firstName, String lastName, String nationality, List<Book> books) {
//        this.id = id;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.nationality = nationality;
//        this.books = books;
//    }

    public Author(Integer id, String firstName, String lastName, String nationality) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
