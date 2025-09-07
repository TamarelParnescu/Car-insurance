package com.example.carins.service;

import com.example.carins.exception.DateOutOfRangeException;
import com.example.carins.exception.ResourceNotFoundException;
import com.example.carins.model.Car;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class CarServiceTest {
    @Mock
    private CarRepository repository;

    @Mock
    private InsurancePolicyRepository insurancePolicyRepository;

    @InjectMocks
    private CarService service;

    Car car = new Car();

    @BeforeEach
    public void setup()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void isInsuranceValidTestOk()
    {
        LocalDate date = LocalDate.now();
        when(repository.findCarById(1L)).thenReturn(Optional.of(car));
        when(insurancePolicyRepository.existsActiveOnDate(1L, date)).thenReturn(true);

        boolean result = service.isInsuranceValid(1L, date);
        assertTrue(result);
    }

    @Test
    public void isInsuranceValidTestInvalidCarId()
    {
        LocalDate date = LocalDate.now();
        when(repository.findCarById(1L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {service.isInsuranceValid(1L, date);});
        assertEquals(ex.getMessage(), "Car with id: 1 not found");
    }

    @Test
    public void isInsuranceValidTestInvalidDate()
    {
        LocalDate date = LocalDate.of(1999,1,1);
        when(repository.findCarById(1L)).thenReturn(Optional.of(car));
        DateOutOfRangeException ex = assertThrows(DateOutOfRangeException.class, () -> {service.isInsuranceValid(1L, date);});
        assertEquals(ex.getMessage(), "Selected Date is not inside the timeframe");
    }
}
