package com.master.bookstore_management.service.author;

import com.master.bookstore_management.model.Author;
import com.master.bookstore_management.repository.author.AuthorRepository;
import com.master.bookstore_management.repository.author.AuthorRepositoryJPA;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService{
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> getAuthors() {
        return authorRepository.getAuthors();
    }
}
