package com.master.bookstore_management.services.author;

import com.master.bookstore_management.models.Author;

import java.util.List;

public interface AuthorService {
    Author addAuthor(String token, Author newAuthor);
    Author save(Author newAuthor);
    Author updateAuthor(String token, Author newAuthor, int id);
    void deleteAuthor(String token, int id);
    List<Author> getAuthors();
    Author getAuthor(String firstName, String lastName);

}
