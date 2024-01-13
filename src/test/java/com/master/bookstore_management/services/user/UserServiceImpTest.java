package com.master.bookstore_management.services.user;

import com.master.bookstore_management.dtos.UpdateUser;
import com.master.bookstore_management.dtos.UserDetails;
import com.master.bookstore_management.dtos.UserResponse;
import com.master.bookstore_management.exceptions.exceptions.DatabaseError;
import com.master.bookstore_management.exceptions.exceptions.EmailAlreadyUsedException;
import com.master.bookstore_management.exceptions.exceptions.InvalidTokenException;
import com.master.bookstore_management.exceptions.exceptions.UnauthorizedUserException;
import com.master.bookstore_management.exceptions.exceptions.UserNotLoggedInException;
import com.master.bookstore_management.models.Author;
import com.master.bookstore_management.models.User;
import com.master.bookstore_management.repositories.author.AuthorRepository;
import com.master.bookstore_management.repositories.user.UserRepository;
import com.master.bookstore_management.services.author.AuthorServiceImpl;
import com.master.bookstore_management.token.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImpTest {
    private static final String TOKEN_ADMIN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEZW5pc2FQcmVkZXNjdSIsInJvbGUiOiJBRE1JTiJ9.ZA0vxSE7keltGZWcNYlRTor-TBOXOrUxFbCsUleok4Y";
    private static final String TOKEN_CUSTOMER = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VydXNlciIsInJvbGUiOiJVU0VSIn0.lkIf0276-9gS9nk9M_VEgoabl39m9qYIZuPa5zP4vpg";
    private static final String TOKEN_NOT_LOGGED_IN = "";
    private static final String TOKEN_INVALID = "invalid";
    private static final Integer USER_ID = 0;
    private static final User USER = new User(
            USER_ID,
            "firstName",
            "lastName",
            "08-02-2001",
            "denisa.predescu@gmail.com",
            "Denisa01!",
            "CUSTOMER"
    );
    private static final UpdateUser UPDATE_USER = new UpdateUser(
            "denisa",
            "predescu",
            "08-02-2001"
    ) ;

    @InjectMocks
    private UserServiceImp userServiceUnderTest;
    @Mock
    private UserRepository userRepository;

    @Test
    void create() {
        when(userRepository.getUserByEmail(USER.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(USER)).thenReturn(USER);

        var result = userServiceUnderTest.create(USER);
        verify(userRepository, times(1)).getUserByEmail(USER.getEmail());
        verify(userRepository, times(1)).save(USER);

        assertEquals(USER, result);
    }

    @Test
    void create_already_in() {
        when(userRepository.getUserByEmail(USER.getEmail())).thenReturn(Optional.of(USER));

        assertThrows(EmailAlreadyUsedException.class, () -> userServiceUnderTest.create(USER));
        verify(userRepository, times(1)).getUserByEmail(USER.getEmail());
        verify(userRepository, never()).save(USER);
    }

    @Test
    void create_DatabaseError_at_getUserByEmail() {
        when(userRepository.getUserByEmail(USER.getEmail())).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> userServiceUnderTest.create(USER));
        verify(userRepository, times(1)).getUserByEmail(USER.getEmail());
        verify(userRepository, never()).save(USER);
    }

    @Test
    void create_DatabaseError_at_save() {
        when(userRepository.getUserByEmail(USER.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(USER)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> userServiceUnderTest.create(USER));
        verify(userRepository, times(1)).getUserByEmail(USER.getEmail());
        verify(userRepository, times(1)).save(USER);
    }

    @Test
    void update() {
        when(userRepository.findById(eq(USER_ID))).thenReturn(Optional.of(USER));

        USER.setBirthday(UPDATE_USER.getBirthday());
        USER.setLastName(UPDATE_USER.getLastName());
        USER.setFirstName(UPDATE_USER.getFirstName());

        when(userRepository.save(any())).thenReturn(USER);

        JwtUtil.verifyIsLoggedIn(TOKEN_CUSTOMER);
        var result = userServiceUnderTest.update(TOKEN_CUSTOMER, USER_ID, UPDATE_USER);
        verify(userRepository, times(1)).findById(USER_ID);
        verify(userRepository).save(USER);

        assertEquals(USER, result);
    }

    @Test
    void update_NoSuchElementException() {
        when(userRepository.findById(eq(USER_ID))).thenThrow(NoSuchElementException.class);

        USER.setBirthday(UPDATE_USER.getBirthday());
        USER.setLastName(UPDATE_USER.getLastName());
        USER.setFirstName(UPDATE_USER.getFirstName());

        JwtUtil.verifyIsLoggedIn(TOKEN_CUSTOMER);
        assertThrows(NoSuchElementException.class, () -> userServiceUnderTest.update(TOKEN_CUSTOMER, USER_ID, UPDATE_USER));
        verify(userRepository, times(1)).findById(USER_ID);
        verify(userRepository, never()).save(USER);
    }

    @Test
    void update_UserNotLoggedInException() {
        assertThrows(UserNotLoggedInException.class, () -> userServiceUnderTest.update(TOKEN_NOT_LOGGED_IN, USER_ID, UPDATE_USER));
        verify(userRepository, never()).findById(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void update_InvalidTokenException() {
        assertThrows(InvalidTokenException.class, () -> userServiceUnderTest.update(TOKEN_INVALID, USER_ID, UPDATE_USER));
        verify(userRepository, never()).findById(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void update_DatabaseError_at_findById() {
        when(userRepository.findById(eq(USER_ID))).thenThrow(DatabaseError.class);

        JwtUtil.verifyIsLoggedIn(TOKEN_CUSTOMER);

        assertThrows(DatabaseError.class, () -> userServiceUnderTest.update(TOKEN_CUSTOMER, USER_ID, UPDATE_USER));
        verify(userRepository, times(1)).findById(USER_ID);
        verify(userRepository, never()).save(USER);
    }

    @Test
    void update_DatabaseError_at_save() {
        when(userRepository.findById(eq(USER_ID))).thenReturn(Optional.of(USER));
        when(userRepository.save(any())).thenThrow(DatabaseError.class);

        JwtUtil.verifyIsLoggedIn(TOKEN_CUSTOMER);

        assertThrows(DatabaseError.class, () -> userServiceUnderTest.update(TOKEN_CUSTOMER, USER_ID, UPDATE_USER));
        verify(userRepository, times(1)).findById(USER_ID);
        verify(userRepository, times(1)).save(USER);
    }

    @Test
    void login() {
        String email = USER.getEmail();
        String password = USER.getPassword();

        when(userRepository.getUser(email, password)).thenReturn(Optional.of(USER));

        UserResponse userResponse = userServiceUnderTest.login(email, password);
        verify(userRepository, times(1)).getUser(email, password);
        assertEquals(new UserResponse(
                USER.getId(),
                USER.getEmail(),
                JwtUtil.generateToken(USER.getFirstName() + USER.getLastName(), USER.getRole())
        ), userResponse);
    }

    @Test
    void login_NoSuchElementException() {
        String email = USER.getEmail();
        String password = USER.getPassword();

        when(userRepository.getUser(email, password)).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> userServiceUnderTest.login(email, password));
        verify(userRepository, times(1)).getUser(email, password);
    }

    @Test
    void login_DatabaseError() {
        String email = USER.getEmail();
        String password = USER.getPassword();

        when(userRepository.getUser(email, password)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> userServiceUnderTest.login(email, password));
        verify(userRepository, times(1)).getUser(email, password);
    }

    @Test
    void getUsers() {
        List<UserDetails> users = List.of(new UserDetails(), new UserDetails());
        when(userRepository.getUsers()).thenReturn(users);

        var result = userServiceUnderTest.getUsers(TOKEN_ADMIN);
        assertEquals(users, result);
        assertEquals(users.size(), result.size());
    }

    @Test
    void getUsers_UnauthorizedUserException()  {
        assertThrows(UnauthorizedUserException.class, () -> userServiceUnderTest.getUsers(TOKEN_CUSTOMER));
        verify(userRepository, never()).getUsers();
    }

    @Test
    void getUsers_UserNotLoggedInException()  {
        assertThrows(UserNotLoggedInException.class, () -> userServiceUnderTest.getUsers(TOKEN_NOT_LOGGED_IN));
        verify(userRepository, never()).getUsers();
    }

    @Test
    void getUsers_InvalidTokenException() {
        assertThrows(InvalidTokenException.class, () -> userServiceUnderTest.getUsers(TOKEN_INVALID));
        verify(userRepository, never()).getUsers();
    }

    @Test
    void getUsers_DatabaseError() {
        when(userRepository.getUsers()).thenThrow(DatabaseError.class);
        assertThrows(DatabaseError.class, () -> userServiceUnderTest.getUsers(TOKEN_ADMIN));
    }

    @Test
    void delete() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(USER));

        userServiceUnderTest.delete(TOKEN_CUSTOMER, USER_ID);

        verify(userRepository).findById(USER_ID);
        verify(userRepository).delete(USER);
    }

    @Test
    void delete_NoSuchElementException() {
        when(userRepository.findById(USER_ID)).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> userServiceUnderTest.delete(TOKEN_CUSTOMER, USER_ID));

        verify(userRepository).findById(USER_ID);
        verify(userRepository, never()).delete(USER);
    }

    @Test
    void delete_UserNotLoggedInException() {
        assertThrows(UserNotLoggedInException.class, () -> userServiceUnderTest.delete(TOKEN_NOT_LOGGED_IN, USER_ID));
        verify(userRepository, never()).findById(USER_ID);
        verify(userRepository, never()).delete(USER);
    }

    @Test
    void delete_InvalidTokenException() {
        assertThrows(InvalidTokenException.class, () -> userServiceUnderTest.delete(TOKEN_INVALID, USER_ID));
        verify(userRepository, never()).findById(USER_ID);
        verify(userRepository, never()).delete(USER);
    }

    @Test
    void delete_DatabaseError() {
        when(userRepository.findById(USER_ID)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> userServiceUnderTest.delete(TOKEN_ADMIN, USER_ID));

        verify(userRepository).findById(USER_ID);
        verify(userRepository, never()).delete(USER);
    }

    @Test
    void getUser() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(USER));

        var result = userServiceUnderTest.getUser(USER_ID);
        verify(userRepository).findById(USER_ID);
        assertEquals(USER, result);
    }

    @Test
    void getUser_NoSuchElementException() {
        when(userRepository.findById(USER_ID)).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> userServiceUnderTest.getUser(USER_ID));
        verify(userRepository, times(1)).findById(USER_ID);
    }

    @Test
    void getUser_DatabaseError() {
        when(userRepository.findById(USER_ID)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> userServiceUnderTest.getUser(USER_ID));
        verify(userRepository, times(1)).findById(USER_ID);
    }
}