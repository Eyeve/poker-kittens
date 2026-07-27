package io.github.eyeve.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import io.github.eyeve.exception.DuplicateUsernameException;
import io.github.eyeve.model.ApplicationUser;
import io.github.eyeve.model.Role;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    /*
     * In-memory storage keeps the example small and focused on security wiring.
     * Replace this map with a repository/JPA adapter in a real application.
     */
    private final Map<String, ApplicationUser> usersByUsername = new ConcurrentHashMap<>();
    private final PasswordEncoder passwordEncoder;

    public ApplicationUser createUser(String username, String rawPassword) {
        String normalizedUsername = normalizeUsername(username);

        ApplicationUser newUser = ApplicationUser.builder()
                .id(UUID.randomUUID())
                .username(normalizedUsername)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .role(Role.USER)
                .build();

        ApplicationUser existingUser = usersByUsername.putIfAbsent(normalizedUsername, newUser);
        if (existingUser != null) {
            throw new DuplicateUsernameException(normalizedUsername);
        }

        return newUser;
    }

    public Optional<ApplicationUser> findByUsername(String username) {
        return Optional.ofNullable(usersByUsername.get(normalizeUsername(username)));
    }

    public ApplicationUser requireByUsername(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        ApplicationUser user = requireByUsername(username);

        /*
         * Spring Security uses UserDetails during login. We keep the domain user
         * separate from Spring's adapter class so application code is not coupled
         * to framework internals.
         */
        return new User(
                user.getUsername(),
                user.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }

    private String normalizeUsername(String username) {
        return username.trim().toLowerCase(Locale.ROOT);
    }
}
