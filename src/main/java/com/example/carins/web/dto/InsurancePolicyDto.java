package com.example.carins.web.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record InsurancePolicyDto(
        @NotNull(message = "A car id is required")
        Long carId,

        @NotBlank(message = "An insurance provider is required")
        String provider,

        @NotNull(message = "Start date is required")
        LocalDate startDate,

        @NotNull(message = "End date is required")
        @Future(message = "End date should not be a past date")
        LocalDate endDate
) {}
