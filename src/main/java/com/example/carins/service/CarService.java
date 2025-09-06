package com.example.carins.service;

import com.example.carins.exception.DateOutOfRangeException;
import com.example.carins.exception.ResourceNotFoundException;
import com.example.carins.model.Car;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final InsurancePolicyRepository policyRepository;

    public CarService(CarRepository carRepository, InsurancePolicyRepository policyRepository) {
        this.carRepository = carRepository;
        this.policyRepository = policyRepository;
    }

    public List<Car> listCars() {
        return carRepository.findAll();
    }

    public boolean isInsuranceValid(Long carId, LocalDate date) {
        if (carRepository.findCarById(carId).isEmpty()) throw new ResourceNotFoundException("Car with id: " + carId + " not found");
        // DONE: optionally throw NotFound if car does not exist

        final LocalDate MIN_DATE = LocalDate.of(2000,1,1);
        final LocalDate MAX_DATE = LocalDate.now().plusYears(1); //Max Period for an Insurance Policy - one year

        if(date.isBefore(MIN_DATE) || date.isAfter(MAX_DATE))
            throw new DateOutOfRangeException("Selected Date is not inside the timeframe");

        return policyRepository.existsActiveOnDate(carId, date);
    }
}
