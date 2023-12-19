package com.master.bookstore_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {
    @Id
    @NotNull
    @GeneratedValue
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "email")
    @NotEmpty(message = "The email cannot be blank!")
    private String email;

    @ManyToOne(targetEntity = Role.class)
    @NotNull
    @PrimaryKeyJoinColumn(name = "role_id")
    private Role role;
}
