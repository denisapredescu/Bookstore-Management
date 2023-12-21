package com.master.bookstore_management.service.author;

import com.master.bookstore_management.model.Author;

import java.util.List;

public interface AuthorService {
    Author addAuthor(String token, Author newAuthor);
    Author updateAuthor(String token, Author newAuthor, int id);
    void deleteAuthor(String token, int id);
    List<Author> getAuthors();

}
