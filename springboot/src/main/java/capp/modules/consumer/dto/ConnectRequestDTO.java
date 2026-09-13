package capp.modules.consumer.dto;

import jakarta.validation.constraints.NotBlank;

public record ConnectRequestDTO(
        @NotBlank
        String username,
        @NotBlank
        String distributorId,
        @NotBlank
        String distributorCode) {}
