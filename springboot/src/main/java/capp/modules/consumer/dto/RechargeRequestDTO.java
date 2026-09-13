package capp.modules.consumer.dto;

import jakarta.validation.constraints.NotBlank;

public record RechargeRequestDTO (
        @NotBlank
        String username,
        @NotBlank
        String credit){}
