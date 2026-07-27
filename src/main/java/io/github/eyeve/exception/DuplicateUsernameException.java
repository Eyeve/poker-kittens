package io.github.eyeve.exception;

public class DuplicateUsernameException extends RuntimeException {

    public DuplicateUsernameException(String username) {
        super("Username is already taken: " + username);
    }
}
