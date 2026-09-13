package capp.modules.security.dto;

public record RegistrationRequestDTO(
        String username,
        String email,
        String password,
        String confirmPassword
) {}
