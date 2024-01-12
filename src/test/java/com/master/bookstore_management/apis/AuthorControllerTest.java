package com.master.bookstore_management.apis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.bookstore_management.models.Author;
import com.master.bookstore_management.services.author.AuthorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorControllerTest {
    private static final String TOKEN_ADMIN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEZW5pc2FQcmVkZXNjdSIsInJvbGUiOiJBRE1JTiJ9.ZA0vxSE7keltGZWcNYlRTor-TBOXOrUxFbCsUleok4Y";

    private static final Author INPUT_AUTHOR = new Author(
            null,
            "firstName",
            "lastName",
            "nationality"
    );

    private static final Author AUTHOR = new Author(
            INPUT_AUTHOR.getId(),
            INPUT_AUTHOR.getFirstName(),
            INPUT_AUTHOR.getLastName(),
            INPUT_AUTHOR.getNationality()
    );
    private static final Integer AUTHOR_ID = 0;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;
    
    @Test
    void addAuthor() throws Exception {
        when(authorService.addAuthor(TOKEN_ADMIN, INPUT_AUTHOR)).thenReturn(AUTHOR);

        String inputAuthorJson = mapper.writeValueAsString(INPUT_AUTHOR);
        String insertedAuthorJson = mapper.writeValueAsString(AUTHOR);

        mockMvc.perform(post("/author/add")
                        .header("userToken", TOKEN_ADMIN)
                        .content(inputAuthorJson)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(insertedAuthorJson));
    }

    @Test
    void updateAuthor() throws Exception {
        when(authorService.updateAuthor(TOKEN_ADMIN, INPUT_AUTHOR, AUTHOR_ID)).thenReturn(AUTHOR);

        String inputAuthorJson = mapper.writeValueAsString(INPUT_AUTHOR);
        String updatedAuthorJson = mapper.writeValueAsString(AUTHOR);

        mockMvc.perform(patch("/author/update/{id}", AUTHOR_ID)
                        .header("userToken", TOKEN_ADMIN)
                        .content(inputAuthorJson)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(updatedAuthorJson));
    }

    @Test
    void deleteAuthor() throws Exception {
        authorService.deleteAuthor(TOKEN_ADMIN, AUTHOR_ID);

        mockMvc.perform(delete("/author/delete/{id}", AUTHOR_ID)
                        .header("userToken", TOKEN_ADMIN)
                ).andExpect(status().isNoContent());
    }

    @Test
    void getAuthors() throws Exception {
        when(authorService.getAuthors()).thenReturn(List.of(AUTHOR));

        String authorsJson = mapper.writeValueAsString(List.of(AUTHOR));

        mockMvc.perform(get("/author/getAuthors")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(content().json(authorsJson));
    }

    @Test
    void getAuthor() throws Exception {
        String firstName = AUTHOR.getFirstName();
        String lastName = AUTHOR.getLastName();
        when(authorService.getAuthor(firstName, lastName)).thenReturn(AUTHOR);

        String authorJson = mapper.writeValueAsString(AUTHOR);

        mockMvc.perform(get("/author/getAuthor/{firstName}/{lastName}", firstName, lastName)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(authorJson));
    }
}