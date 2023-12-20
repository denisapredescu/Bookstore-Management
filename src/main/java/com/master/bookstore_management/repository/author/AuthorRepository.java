package com.master.bookstore_management.repository.author;

import com.master.bookstore_management.model.Author;

import java.util.List;

public interface AuthorRepository {
    List<Author> getAuthors();
}
