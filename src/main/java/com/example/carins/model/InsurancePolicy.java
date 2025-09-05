package com.example.carins.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Check;

import java.time.LocalDate;

@Entity
@Table(name = "insurancepolicy")
@Check(constraints = "end_date > start_date")
public class InsurancePolicy {
    @Id
    @SequenceGenerator(name = "pk_sequence", sequenceName = "pk_sequence", initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Car car;

    private String provider;
    private LocalDate startDate;
    @Column(nullable = false)
    @NotNull(message = "End date of the insurance policy must not be NULL")
    @Future(message = "End date of the insurance policy must be a valid date in the future")
    private LocalDate endDate; // nullable == open-ended

    public InsurancePolicy() {}
    public InsurancePolicy(Car car, String provider, LocalDate startDate, LocalDate endDate) {
        this.car = car; this.provider = provider; this.startDate = startDate; this.endDate = endDate;
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
