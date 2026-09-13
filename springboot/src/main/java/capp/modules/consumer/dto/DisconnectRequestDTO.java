package capp.modules.consumer.dto;

import jakarta.validation.constraints.NotBlank;

public record DisconnectRequestDTO(
        @NotBlank
        String username,
        @NotBlank
        String distributorId) {}
