package com.master.bookstore_management.repository.author;

import com.master.bookstore_management.model.Author;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    @Override
    public List<Author> getAuthors() {
        return null;
    }
}
