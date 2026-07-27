package io.github.eyeve.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Email @NotBlank String username,

        /*
         * BCrypt only uses the first 72 bytes. The upper bound prevents giving users
         * a false sense that extremely long passwords are fully represented.
         */
        @NotBlank @Size(min = 8, max = 72) String password
) {
}
