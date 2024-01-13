package com.master.bookstore_management.apis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.bookstore_management.dtos.BasketDetails;
import com.master.bookstore_management.dtos.BookFromBasketDetails;
import com.master.bookstore_management.models.Basket;
import com.master.bookstore_management.models.User;
import com.master.bookstore_management.services.author.AuthorService;
import com.master.bookstore_management.services.basket.BasketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BasketControllerTest {
    private static final String TOKEN_CUSTOMER = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VydXNlciIsInJvbGUiOiJVU0VSIn0.lkIf0276-9gS9nk9M_VEgoabl39m9qYIZuPa5zP4vpg";
    private static final Integer USER_ID = 0;
    private static final Integer BOOK_ID = 1;
    private static final Integer BASKET_ID = 2;
    private static final Basket BASKET = new Basket(
            BASKET_ID,
           false,
           100,
            new User(
                    USER_ID,
                    "firstName",
                    "lastName",
                    "08-02-2001",
                    "denisa.predescu@gmail.com",
                    "Denisa01!",
                    "CUSTOMER"
            )
    );
    private static final BasketDetails GET_BASKET = new BasketDetails(
            0,
            "false",
            USER_ID,
            BASKET.getUser().getEmail(),
            100,
            List.of(
                    new BookFromBasketDetails("book 1", 50, 1),
                    new BookFromBasketDetails("book 2", 25, 2)
            )
    );

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BasketService basketService;

    @Test
    void createBasket() throws Exception {
        when(basketService.createBasket(TOKEN_CUSTOMER, USER_ID)).thenReturn(BASKET);

        String insertedBasketJson = mapper.writeValueAsString(BASKET);

        mockMvc.perform(post("/basket/createBasket/{userId}", USER_ID)
                        .header("userToken", TOKEN_CUSTOMER)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(insertedBasketJson));
    }

    @Test
    void sentOrder() throws Exception {
        BASKET.setSent(true);
        when(basketService.sentOrder(TOKEN_CUSTOMER, USER_ID)).thenReturn(BASKET);

        String sentBasketJson = mapper.writeValueAsString(BASKET);

        mockMvc.perform(patch("/basket/sentOrder/{userId}", USER_ID)
                        .header("userToken", TOKEN_CUSTOMER)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(sentBasketJson));
    }

    @Test
    void getBasket() throws Exception {
        when(basketService.getBasket(TOKEN_CUSTOMER, USER_ID)).thenReturn(GET_BASKET);

        String basketJson = mapper.writeValueAsString(GET_BASKET);

        mockMvc.perform(get("/basket/getBasket/{userId}", USER_ID)
                        .header("userToken", TOKEN_CUSTOMER)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(basketJson));
    }

    @Test
    void addBookToBasket() throws Exception {
        when(basketService.addBookToBasket(TOKEN_CUSTOMER, BOOK_ID, BASKET_ID)).thenReturn(BASKET);

        String basketJson = mapper.writeValueAsString(BASKET);

        mockMvc.perform(post("/basket/addBookToBasket/{bookId}/{basketId}", BOOK_ID, BASKET_ID)
                        .header("userToken", TOKEN_CUSTOMER)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(basketJson));
    }

    @Test
    void removeBookFromBasket() throws Exception {
        when(basketService.removeBookFromBasket(TOKEN_CUSTOMER, BOOK_ID, BASKET_ID)).thenReturn(BASKET);

        String basketJson = mapper.writeValueAsString(BASKET);

        mockMvc.perform(delete("/basket/removeBookFromBasket/{bookId}/{basketId}", BOOK_ID, BASKET_ID)
                        .header("userToken", TOKEN_CUSTOMER)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(basketJson));
    }

    @Test
    void decrementBookFromBasket() throws Exception {
        when(basketService.decrementBookFromBasket(TOKEN_CUSTOMER, BOOK_ID, BASKET_ID)).thenReturn(BASKET);

        String basketJson = mapper.writeValueAsString(BASKET);

        mockMvc.perform(patch("/basket/decrementBookFromBasket/{bookId}/{basketId}", BOOK_ID, BASKET_ID)
                        .header("userToken", TOKEN_CUSTOMER)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(basketJson));
    }
}