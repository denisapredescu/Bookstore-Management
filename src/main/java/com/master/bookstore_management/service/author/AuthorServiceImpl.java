package com.master.bookstore_management.service.author;

import com.master.bookstore_management.model.Author;
import com.master.bookstore_management.repository.author.AuthorRepositoryJPA;
import com.master.bookstore_management.token.JwtUtil;
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
        JwtUtil.verifyAdmin(token);
        return authorRepository.save(newAuthor);
    }

    @Override
    public Author updateAuthor(String token, Author newAuthor, int id) {
        JwtUtil.verifyAdmin(token);
        Author author = authorRepository.findById(id).orElseThrow();
        author.setFirstName(newAuthor.getFirstName());
        author.setLastName(newAuthor.getLastName());
        author.setNationality(newAuthor.getNationality());
        return authorRepository.save(author);
    }

    @Override
    public void deleteAuthor(String token, int id) {
        JwtUtil.verifyAdmin(token);
        authorRepository.deleteById(id);
    }

    @Override
    public List<Author> getAuthors(String token) {
        JwtUtil.verifyAdmin(token);
        return authorRepository.getAuthors();
    }
}
