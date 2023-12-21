package com.master.bookstore_management.repository;

import com.master.bookstore_management.dto.UserResponse;
import com.master.bookstore_management.model.Book;
import com.master.bookstore_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryJPA extends JpaRepository<User,Integer> {
    @Query("SELECT DISTINCT user.email, role.role FROM User user JOIN user.role role WHERE user.email = :email AND user.password = :password")
    Optional<UserResponse> getUser(String email, String password);

}
