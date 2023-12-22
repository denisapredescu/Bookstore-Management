package com.master.bookstore_management.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Locale;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @NotNull
    @GeneratedValue
    private Integer id;

    @Column(name = "first_name")
    private String firstName = "";

    @Column(name = "last_name")
    private String lastName = "";

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "email")
    @NotEmpty(message = "The email cannot be blank!")
    private String email;

    @Column(name = "password")
    @NotEmpty(message = "The password cannot be blank!")
    private String password;

//    @ManyToOne(targetEntity = Role.class)
//    @NotNull
//    @PrimaryKeyJoinColumn(name = "role_id")
//    private Role role;

    @Column(name = "role")
    @NotEmpty(message = "The role name cannot be blank!")
    private String role;


    public User() {
    }

    public User(Integer id, String firstName, String lastName, String birthday, String email, String password, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.email = email;
        this.password = password;
        this.role = role.toUpperCase(Locale.ENGLISH);
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role.toUpperCase(Locale.ENGLISH);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(birthday, user.birthday) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, birthday, email, password, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday='" + birthday + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
