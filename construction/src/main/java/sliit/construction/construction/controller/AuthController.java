package sliit.construction.construction.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.bind.annotation.*;

import sliit.construction.construction.config.JwtService;
import sliit.construction.construction.dto.AuthDtos;
import sliit.construction.construction.dto.UserDtos;
import sliit.construction.construction.entity.Role;
import sliit.construction.construction.entity.User;
import sliit.construction.construction.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService users;

    private final AuthenticationManager auth;

    private final JwtService jwt;


    public AuthController(
            UserService users,
            AuthenticationManager auth,
            JwtService jwt) {

        this.users = users;

        this.auth = auth;

        this.jwt = jwt;
    }


    // ==========================================
    // CLIENT REGISTRATION
    // ==========================================

    @PostMapping("/register")
    public ResponseEntity<UserDtos.Response> register(
            @Valid @RequestBody AuthDtos.RegisterRequest request) {


        /*
         * IMPORTANT:
         *
         * Public registrations are ALWAYS CLIENT accounts.
         *
         * The user cannot choose:
         *
         * PROJECT_MANAGER
         * SITE_ENGINEER
         * SYSTEM_ADMINISTRATOR
         *
         * etc.
         */

        UserDtos.Response response =
                users.create(

                        new UserDtos.Request(

                                request.username(),

                                request.email(),

                                request.password(),

                                Role.CLIENT,

                                request.fullName(),

                                request.phoneNumber()

                        )

                );


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // ==========================================
    // LOGIN
    // ==========================================

    @PostMapping("/login")
    public AuthDtos.AuthResponse login(
            @Valid @RequestBody AuthDtos.LoginRequest request) {


        /*
         * Authenticate username + password.
         */

        auth.authenticate(

                new UsernamePasswordAuthenticationToken(

                        request.username(),

                        request.password()

                )

        );


        /*
         * Get the authenticated user.
         */

        User user =
                users.getByUsername(request.username());


        /*
         * Generate JWT token.
         */

        String token =
                jwt.generate(

                        user.getUsername(),

                        user.getRole().name()

                );


        return new AuthDtos.AuthResponse(

                token,

                user.getId(),

                user.getUsername(),

                user.getRole().name()

        );
    }

}