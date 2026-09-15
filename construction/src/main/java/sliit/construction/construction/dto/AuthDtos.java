package sliit.construction.construction.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class AuthDtos {

    private AuthDtos() {
    }

    // ==========================================
    // CLIENT REGISTRATION
    // ==========================================

    public record RegisterRequest(

            @NotBlank
            @Size(max = 50)
            String username,

            @NotBlank
            @Email
            @Size(max = 120)
            String email,

            @NotBlank
            @Size(min = 8, max = 100)
            String password,

            @NotBlank
            @Size(max = 120)
            String fullName,

            @Size(max = 30)
            String phoneNumber

    ) {
    }


    // ==========================================
    // LOGIN
    // ==========================================

    public record LoginRequest(

            @NotBlank
            String username,

            @NotBlank
            String password

    ) {
    }


    // ==========================================
    // LOGIN RESPONSE
    // ==========================================

    public record AuthResponse(

            String token,
            Long userId,
            String username,
            String role

    ) {
    }

}