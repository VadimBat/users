package com.example.users.controller;

import com.example.users.controller.config.UserNotFoundException;
import com.example.users.data.UserEntity;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;
    private Validator validator;
    private List<UserEntity> users;

    @BeforeEach
    public void beforeEach() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        userController = new UserController();
        users = userController.getUsers();
    }

    @Test
    void createUserValidUserReturnsCreatedTest() {

        //Given
        UserEntity user = UserEntity.builder()
                .email("test@example.com")
                .firstName("Antony")
                .lastName("Coule")
                .birthDate(LocalDate.of(1997,2,15))
                .build();

        //When
        ResponseEntity<UserEntity> response = userController.createUser(user);

        //Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
        assertEquals(1, users.size());

    }

    @Test
    void updateUserExistingUserReturnsUpdatedUser() throws UserNotFoundException {

        //Given
        UserEntity existingUser = UserEntity.builder()
                .email("test@example.com")
                .firstName("Mike")
                .lastName("Webber")
                .birthDate(LocalDate.of(1992,2,15))
                .build();
        users.add(existingUser);

        UserEntity updatedUser = UserEntity.builder()
                .firstName("Ivan").build();

        //When
        ResponseEntity<UserEntity> response = userController.updateUser("test@example.com", updatedUser);

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ivan", ((UserEntity) response.getBody()).getFirstName());
        assertEquals(1, users.size());
    }


    @Test
    void deleteUserExistingUserRemovesUserTest() throws UserNotFoundException {

        //Given
        UserEntity existingUser = UserEntity.builder()
                .email("test@example.com")
                .firstName("Mike")
                .lastName("Webber")
                .birthDate(LocalDate.of(2001,3,17))
                .build();
        userController.getUsers().add(existingUser);

        //When
        ResponseEntity<UserEntity> response = userController.deleteUser("test@example.com");

        //Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        List<UserEntity> users = userController.getUsers();
        assertTrue(users.isEmpty());

    }


    @Test
    void searchUsersByBirthDateRangeExistingUsersInRangeReturnsUsersTest() {

        //Given
        UserEntity user1 = UserEntity.builder()
                .email("test@gmail.com")
                .firstName("Leon")
                .lastName("Wallace")
                .birthDate(LocalDate.of(1994,7,23))
                .build();

        UserEntity user2 = UserEntity.builder()
                .email("test@yahoo.com")
                .firstName("Oleg")
                .lastName("Gnatenko")
                .birthDate(LocalDate.of(1997,9,14))
                .build();

        userController.getUsers().add(user1);
        userController.getUsers().add(user2);

        //When
        LocalDate fromDate = LocalDate.of(1990, 1, 1);
        LocalDate toDate = LocalDate.of(1995, 1, 1);

        ResponseEntity<List<UserEntity>> response = userController.searchUsersByBirthDateRange(fromDate, toDate);

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<UserEntity> usersInRange = response.getBody();
        assertEquals(1, usersInRange.size());
        assertEquals(user1, usersInRange.get(0));

    }

    @Test
    void searchUsersByBirthDateRangeNoUsersInRangeReturnsEmptyListTest() {

        //Given
        UserEntity user = UserEntity.builder()
                .email("test@gmail.com")
                .firstName("Leon")
                .lastName("Wallace")
                .birthDate(LocalDate.of(1994,7,23))
                .build();
        userController.getUsers().add(user);

        //When
        LocalDate fromDate = LocalDate.of(2000, 1, 1);
        LocalDate toDate = LocalDate.of(2010, 1, 1);

        ResponseEntity<List<UserEntity>> response = userController.searchUsersByBirthDateRange(fromDate, toDate);

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<UserEntity> usersInRange = response.getBody();
        assertTrue(usersInRange.isEmpty());

    }

}

