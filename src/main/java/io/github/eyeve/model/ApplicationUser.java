package io.github.eyeve.model;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ApplicationUser {
    UUID id;
    String username;

    /*
     * This is a BCrypt hash, not the original password. The original password should
     * exist only briefly in memory during registration/login request processing.
     */
    String passwordHash;

    Role role;
}
