package io.github.eyeve.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.eyeve.dto.AuthResponse;
import io.github.eyeve.dto.LoginRequest;
import io.github.eyeve.dto.RegisterRequest;
import io.github.eyeve.service.JwtService;
import io.github.eyeve.service.UserService;
import io.github.eyeve.model.ApplicationUser;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        ApplicationUser user = userService.createUser(request.username(), request.password());

        /*
         * Returning a token immediately after registration is common for APIs.
         * Some products require email verification first; that policy sits above
         * the JWT mechanics shown here.
         */
        return jwtService.issueAccessToken(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        /*
         * Delegate password verification to Spring Security. It will call our
         * UserDetailsService and PasswordEncoder, so we do not compare hashes by hand.
         */
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        ApplicationUser user = userService.requireByUsername(authentication.getName());
        return jwtService.issueAccessToken(user);
    }

    // maybe general LoginRequest and RegisterRequest
}
