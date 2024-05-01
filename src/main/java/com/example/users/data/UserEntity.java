package com.example.users.data;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * UserEntity object.
 */
@Builder
@Data
public class UserEntity {

    @Email
    @NotBlank
    @NotNull
    private String email;

    @NotBlank
    @NotNull
    private String firstName;

    @NotBlank
    @NotNull
    private String lastName;

    @Past
    @NotNull
    private LocalDate birthDate;

    private String address;

    @Pattern(regexp = "\\d{12}")
    private String phoneNumber;

}
