package com.master.bookstore_management.services.author;

import com.master.bookstore_management.models.Author;
import com.master.bookstore_management.repositories.author.AuthorRepository;
import com.master.bookstore_management.token.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AuthorServiceImpl implements AuthorService{
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author addAuthor(String token, Author newAuthor) {
        JwtUtil.verifyAdmin(token);

        return  save(newAuthor);
    }

    @Override
    public Author save(Author newAuthor) {
        if (newAuthor == null)
            return null;

        Author alreadyIn = getAuthor(newAuthor.getFirstName(), newAuthor.getLastName());

        if (alreadyIn == null)
            return authorRepository.save(newAuthor);

        return  alreadyIn;
    }

    @Override
    public Author updateAuthor(String token, Author newAuthor, int id) {
        JwtUtil.verifyAdmin(token);
        Author author = authorRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Author with this id not found"));

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
        return authorRepository.getAuthors();
    }

    @Override
    public Author getAuthor(String firstName, String lastName) {
        return authorRepository.getAuthor(firstName, lastName).orElse(null);
    }
}
