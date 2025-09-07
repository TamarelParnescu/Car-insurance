package com.example.carins.service;

import com.example.carins.exception.DateOutOfRangeException;
import com.example.carins.exception.ResourceNotFoundException;
import com.example.carins.model.Car;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.web.dto.InsurancePolicyDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class InsurancePolicyServiceTest {

    @Mock
    private InsurancePolicyRepository repository;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private InsurancePolicyService service;

    private final Long validId = 1L;
    private final Long invalidId = 2L;

    private final Car car = new Car();
    private final LocalDate startDate = LocalDate.now().plusDays(1);
    private final LocalDate endDate = startDate.plusYears(1).minusDays(1);
    private InsurancePolicyDto validInsuranceDto;
    private InsurancePolicy validInsurance;
    private InsurancePolicyDto insuranceWithStartDateGreaterThanEndDate;
    private InsurancePolicyDto insuranceWithStartDateAPastDate;
    private InsurancePolicyDto insuranceWithEndDateGreaterThanOneYear;

    @BeforeEach
    public void setup()
    {
        MockitoAnnotations.openMocks(this);
        String provider = "provider";
        validInsurance = new InsurancePolicy(car, provider, startDate, endDate);
        validInsuranceDto = new InsurancePolicyDto(1L, provider, startDate, endDate);
        insuranceWithStartDateGreaterThanEndDate = new InsurancePolicyDto(1L, provider, endDate, startDate);
        insuranceWithStartDateAPastDate = new InsurancePolicyDto(1L, provider, startDate.minusYears(10), endDate);
        insuranceWithEndDateGreaterThanOneYear = new InsurancePolicyDto(1L, provider, startDate, endDate.plusYears(2));
    }

    @Test
    public void testGetInsurancePolicyById()
    {
        when(repository.findById(validId)).thenReturn(Optional.of(validInsurance));
        InsurancePolicy result = service.getInsurancePolicyById(validId);
        assertEquals(result, validInsurance);
    }

    @Test
    public void testGetInsurancePolicyByIdNotOk()
    {
        when(repository.findById(invalidId)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {service.getInsurancePolicyById(invalidId);});
        assertEquals(ex.getMessage(), "No insurance policy with id: 2 found");
    }

    @Test
    public void testCreateInsurancePolicyOk()
    {
        when(repository.save(any(InsurancePolicy.class))).thenReturn(validInsurance);
        when(carRepository.findCarById(1L)).thenReturn(Optional.of(car));
        when(repository.findAllByCar_Id(1L)).thenReturn(Collections.emptyList());
        InsurancePolicy result = service.createInsurancePolicy(validInsuranceDto);
        assertEquals(validInsurance, result);
    }

    @Test
    public void testCreateInsurancePolicyCarNotFound()
    {
        when(carRepository.findCarById(1L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {service.createInsurancePolicy(validInsuranceDto);});
        assertEquals(ex.getMessage(), "Car with id : 1 not found");
    }

    @Test
    public void testCreateInsurancePolicyStartDateGreaterThanEndDate()
    {
        when(carRepository.findCarById(1L)).thenReturn(Optional.of(car));
        DateOutOfRangeException ex = assertThrows(DateOutOfRangeException.class, () -> {service.createInsurancePolicy(insuranceWithStartDateGreaterThanEndDate);});
        assertEquals(ex.getMessage(), "Start date must precede end date");
    }

    @Test
    public void testCreateInsurancePolicyStartDateIsPastDate()
    {
        when(carRepository.findCarById(1L)).thenReturn(Optional.of(car));
        DateOutOfRangeException ex = assertThrows(DateOutOfRangeException.class, () -> {service.createInsurancePolicy(insuranceWithStartDateAPastDate);});
        assertEquals(ex.getMessage(), "Start date should not be a past date");
    }

    @Test
    public void testCreateInsurancePolicyEndDateIsMoreThanOneYearIntoFuture()
    {
        when(carRepository.findCarById(1L)).thenReturn(Optional.of(car));
        DateOutOfRangeException ex = assertThrows(DateOutOfRangeException.class, () -> {service.createInsurancePolicy(insuranceWithEndDateGreaterThanOneYear);});
        assertEquals(ex.getMessage(), "End date should be at max one year from now");
    }
}
