package com.master.bookstore_management.service.author;

import com.master.bookstore_management.model.Author;
import com.master.bookstore_management.repository.author.AuthorRepository;
import com.master.bookstore_management.repository.author.AuthorRepositoryJPA;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService{
    private final AuthorRepositoryJPA authorRepository;

    public AuthorServiceImpl(AuthorRepositoryJPA authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author addAuthor(String token, Author newAuthor) {
        verifyUser(token);
        return authorRepository.save(newAuthor);
    }

    @Override
    public Author updateAuthor(String token, Author newAuthor, int id) {
        verifyUser(token);
        Author author = authorRepository.findById(id).orElseThrow();
        author.setFirstName(newAuthor.getFirstName());
        author.setLastName(newAuthor.getLastName());
        author.setNationality(newAuthor.getNationality());
        return authorRepository.save(author);
    }

    @Override
    public void deleteAuthor(String token, int id) {
        verifyUser(token);
        authorRepository.deleteById(id);
    }

    @Override
    public List<Author> getAuthors() {
        return authorRepository.getAuthors();
    }

    private static void verifyUser(String token) {
        if (token == null || Strings.isBlank(token)){
            throw new RuntimeException("User not logged in");
        }
    }

}
