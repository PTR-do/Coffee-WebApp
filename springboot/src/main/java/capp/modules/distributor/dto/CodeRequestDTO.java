package capp.modules.distributor.dto;

import jakarta.validation.constraints.NotBlank;

public record CodeRequestDTO (
        @NotBlank()
        String distributorId,
        @NotBlank()
        String distributorCode){}
