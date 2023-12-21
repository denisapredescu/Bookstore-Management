package com.master.bookstore_management.repository;

import com.master.bookstore_management.dto.UserDetails;
import com.master.bookstore_management.dto.UserResponse;
import com.master.bookstore_management.model.Book;
import com.master.bookstore_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryJPA extends JpaRepository<User,Integer> {
    @Query("SELECT DISTINCT user FROM User user WHERE user.email = :email AND user.password = :password")
    Optional<User> getUser(String email, String password);

//    @Query("SELECT DISTINCT user.id, user.firstName, user.lastName, user.birthday, user.email, user.role FROM User user")
//    List<UserDetails> getUsers();

    @Query("SELECT NEW com.master.bookstore_management.dto.UserDetails(user.id, user.firstName, user.lastName, user.birthday, user.email, user.role) FROM User user")
    List<UserDetails> getUsers();
}
