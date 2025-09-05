package com.example.carins.web.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record InsurancePolicyDto(
        @NotNull
        Long carId,

        @NotBlank(message = "An insurance provider must be declared")
        String provider,

        @NotNull(message = "Start date must not be NULL")
        LocalDate startDate,

        @NotNull(message = "End date must not be NULL")
        @Future(message = "End date must be set to a date in the future")
        LocalDate endDate
) {}
