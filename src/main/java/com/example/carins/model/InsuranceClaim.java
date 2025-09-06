package com.example.carins.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="insuranceclaim")
public class InsuranceClaim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A date is required")
    @Column(nullable = false)
    private LocalDate claimDate;

    @NotBlank(message = "A description of the claim is required")
    @Column(nullable = false)
    private String description;

    @NotNull
    @Column(nullable = false)
    @DecimalMin(value = "0.0", message = "Amount should be greater than 0.0")
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private InsurancePolicy insurancePolicy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimStatus status = ClaimStatus.PENDING;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate()
    {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate()
    {
        updatedAt = LocalDateTime.now();
    }

    public InsuranceClaim() {}

    public InsuranceClaim(LocalDate claimDate, String description, Double amount, InsurancePolicy insurancePolicy) {
        this.claimDate = claimDate;
        this.description = description;
        this.amount = amount;
        this.insurancePolicy = insurancePolicy;
    }

    public Long getId()
    {
        return id;
    }

    public LocalDate getClaimDate() {
        return claimDate;
    }

    public String getDescription() {
        return description;
    }

    public Double getAmount() {
        return amount;
    }
}
