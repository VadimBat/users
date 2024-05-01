package com.example.users.controller;

import com.example.users.controller.config.UserNotFoundException;
import com.example.users.data.UserEntity;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * UserController makes all responses on all requests.
 */
@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Getter
    @Value("${minimumAge}")
    private int minimumAge;

    @Getter
    private final List<UserEntity> users = new ArrayList<>();

    @PostMapping("/create")
    public ResponseEntity<UserEntity> createUser(@Valid @RequestBody UserEntity user) {
        if (user.getBirthDate().plusYears(minimumAge).isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("User must be at least " + minimumAge + " years old.");

        users.add(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/update")
    public ResponseEntity<UserEntity> updateUser(@PathVariable String email, @RequestBody UserEntity updateUser) throws UserNotFoundException {
        for (UserEntity user : users) {
            if (Objects.equals(user.getEmail(), email)) {
                user.setFirstName(updateUser.getFirstName());
                user.setLastName(updateUser.getLastName());
                user.setBirthDate(updateUser.getBirthDate());
                if (updateUser.getAddress() != null) user.setAddress(updateUser.getAddress());
                if (updateUser.getPhoneNumber() != null) user.setPhoneNumber(updateUser.getPhoneNumber());
                return ResponseEntity.ok(user);
            }

        }
        throw new UserNotFoundException();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<UserEntity> deleteUser(@PathVariable String email) throws UserNotFoundException {
        for (UserEntity user : users) {
            if (Objects.equals(user.getEmail(), email)) {
                users.remove(user);
                return ResponseEntity.noContent().build();
            }

        }
        throw new UserNotFoundException();
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserEntity>> searchUsersByBirthDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("From date must be before To date.");
        }

        List<UserEntity> result = new ArrayList<>();
        for (UserEntity user : users) {
            if (!user.getBirthDate().isBefore(from) && !user.getBirthDate().isAfter(to)) {
                result.add(user);
            }
        }
        return ResponseEntity.ok(result);
    }
}
