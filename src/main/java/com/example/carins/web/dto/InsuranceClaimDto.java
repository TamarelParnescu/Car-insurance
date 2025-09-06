package com.example.carins.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record InsuranceClaimDto(
        @NotNull(message = "Claim date is required")
        LocalDate claimDate,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.0", message = "Amount should be greater than 0")
        Double amount,

        @NotBlank(message = "Description is required")
        String description
) {
}
