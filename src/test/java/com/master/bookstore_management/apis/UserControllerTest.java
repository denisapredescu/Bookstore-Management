package com.master.bookstore_management.apis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.bookstore_management.dtos.UpdateUser;
import com.master.bookstore_management.dtos.UserDetails;
import com.master.bookstore_management.dtos.UserResponse;
import com.master.bookstore_management.models.User;
import com.master.bookstore_management.services.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    private static final String TOKEN_ADMIN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEZW5pc2FQcmVkZXNjdSIsInJvbGUiOiJBRE1JTiJ9.ZA0vxSE7keltGZWcNYlRTor-TBOXOrUxFbCsUleok4Y";
    private static final Integer USER_ID = 0;
    private static final User INPUT_USER = new User(
            0,
            "firstName",
            "lastName",
            "08-02-2001",
            "denisa.predescu@gmail.com",
            "Denisa01!",
            "ADMIN"
    );
    private static final User USER = new User(
            INPUT_USER.getId(),
            INPUT_USER.getFirstName(),
            INPUT_USER.getLastName(),
            INPUT_USER.getBirthday(),
            INPUT_USER.getEmail(),
            INPUT_USER.getPassword(),
            INPUT_USER.getRole()
    );
    private static final UpdateUser UPDATE_USER = new UpdateUser(
            "denisa",
            "predescu",
            "08-02-2001"
    ) ;
    private static final UserResponse USER_RESPONSE = new UserResponse(
            USER.getId(),
            USER.getEmail(),
            TOKEN_ADMIN
    );
    private static final List<UserDetails> USER_DETAILS = List.of(
            new UserDetails(
                    USER.getId(),
                    USER.getFirstName(),
                    USER.getLastName(),
                    USER.getBirthday(),
                    USER.getEmail(),
                    USER.getRole()
            ),
            new UserDetails(
                    1,
                    "user 2",
                    "user 2",
                    "00-00-0000",
                    "user@gmail.com",
                    "CUSTOMER"
            )
    );

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void create() throws Exception {
        when(userService.create(INPUT_USER)).thenReturn(USER);

        String inputUserJson = mapper.writeValueAsString(INPUT_USER);
        String insertedUserJson = mapper.writeValueAsString(USER);

        mockMvc.perform(post("/user/signUp")
                        .content(inputUserJson)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(insertedUserJson));
    }

    @Test
    void update() throws Exception {
        USER.setFirstName(UPDATE_USER.getFirstName());
        USER.setLastName(UPDATE_USER.getLastName());
        USER.setBirthday(UPDATE_USER.getBirthday());

        when(userService.update(eq(TOKEN_ADMIN), eq(USER_ID), any(UpdateUser.class))).thenReturn(USER);

        String updateUserJson = mapper.writeValueAsString(UPDATE_USER);
        String updatedUserJson = mapper.writeValueAsString(USER);

        mockMvc.perform(patch("/user/update/{id}", USER_ID)
                        .header("userToken", TOKEN_ADMIN)
                        .content(updateUserJson)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(updatedUserJson));
    }

    @Test
    void delete() throws Exception {
        userService.delete(TOKEN_ADMIN, USER_ID);

        mockMvc.perform(patch("/user/delete/{id}", USER_ID)
                .header("userToken", TOKEN_ADMIN)
        ).andExpect(status().isNoContent());
    }

    @Test
    void login() throws Exception {
        String email = USER.getEmail();
        String password = USER.getPassword();

        when(userService.login(email, password)).thenReturn(USER_RESPONSE);

        String userResponseJson = mapper.writeValueAsString(USER_RESPONSE);

        mockMvc.perform(get("/user/login/{email}", email)
                        .header("password", password)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(userResponseJson));
    }

    @Test
    void getUser() throws Exception {
        when(userService.getUser(USER_ID)).thenReturn(USER);

        String userJson = mapper.writeValueAsString(USER);

        mockMvc.perform(get("/user/getUser/{id}", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(userJson));
    }

    @Test
    void getUsers() throws Exception {
        when(userService.getUsers(TOKEN_ADMIN)).thenReturn(USER_DETAILS);

        String usersJson = mapper.writeValueAsString(USER_DETAILS);

        mockMvc.perform(get("/user/getUsers")
                        .header("userToken", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(usersJson));
    }
}