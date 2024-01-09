package com.master.bookstore_management.dtos;

public class UpdateUser {
    String firstName;
    String lastName;
    String birthday;

    public UpdateUser(String firstName, String lastName, String birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthday() {
        return birthday;
    }
}
