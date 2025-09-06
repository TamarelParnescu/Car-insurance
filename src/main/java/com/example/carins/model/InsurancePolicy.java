package com.example.carins.model;

import com.example.carins.exception.DateOutOfRangeException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Check;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "insurancepolicy")
public class InsurancePolicy {
    @Id
    @SequenceGenerator(name = "pk_sequence", sequenceName = "pk_sequence", initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Car car;

    @Column(nullable = false)
    @NotBlank(message = "Provider of the insurance policy is required")
    private String provider;

    @Column(nullable = false)
    @NotNull(message = "Start date of the insurance policy is required")
    private LocalDate startDate;

    //@Column(nullable = false)
    //@NotNull(message = "End date of the insurance policy is required")
    //@Future(message = "End date of the insurance policy should not be a past date")
    private LocalDate endDate; // nullable == open-ended


    @OneToMany(mappedBy = "insurancePolicy", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<InsuranceClaim> claims = new ArrayList<>();


    public InsurancePolicy() {}
    public InsurancePolicy(Car car, String provider, LocalDate startDate, LocalDate endDate) {
        this.car = car; this.provider = provider; this.startDate = startDate; this.endDate = endDate;
    }

    @PrePersist
    public void checkEndDate()
    {
        if(endDate == null)
            throw new DateOutOfRangeException("End date is required");
    }
    public Long getId() { return id; }
    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}
