package com.example.carins.service;

import com.example.carins.exception.InvalidInsuranceException;
import com.example.carins.model.Car;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.web.dto.InsurancePolicyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class InsurancePolicyService {

    InsurancePolicyRepository repository;
    CarService carService;

    @Autowired
    public InsurancePolicyService(InsurancePolicyRepository repository, CarService carService)
    {
        this.repository = repository;
        this.carService = carService;
    }

    public InsurancePolicyDto createInsurance(InsurancePolicyDto insurancePolicyDto)
    {
        this.validateDto(insurancePolicyDto);
        InsurancePolicy newInsurance = this.convertDto(insurancePolicyDto);

        return this.convertToDto(repository.save(newInsurance));
    }

    private void validateDto(InsurancePolicyDto dto)
    {
        if(carService.getCarById(dto.carId()).isEmpty())
            throw new InvalidInsuranceException("No car with current id: " + dto.carId());
        if(dto.provider().isBlank() || dto.provider().isEmpty())
            throw new InvalidInsuranceException("Insurance provider is required.");
        if(dto.startDate() == null)
            throw new InvalidInsuranceException("Start date is required.");
        if(dto.endDate() == null)
            throw new InvalidInsuranceException("End date is required.");
        if(dto.endDate().isBefore(LocalDate.now()))
            throw new InvalidInsuranceException("End date must be at least after today.");

        boolean isInsuranceValid = repository.findAllByCar_Id(dto.carId()).stream()
                .allMatch(insurance ->
                        dto.startDate().isAfter(insurance.getEndDate()) &&
                        dto.endDate().isBefore(insurance.getStartDate())
                        );
        if(!isInsuranceValid)
            throw new InvalidInsuranceException("An insurance already exists for this car.");
    }

    private InsurancePolicy convertDto(InsurancePolicyDto dto) {
        Optional<Car> car = carService.getCarById(dto.carId());

        if(car.isPresent()) return new InsurancePolicy(car.get(), dto.provider(), dto.startDate(), dto.endDate());

        throw new InvalidInsuranceException("No car with current id: " + dto.carId());
    }

    private InsurancePolicyDto convertToDto(InsurancePolicy insurancePolicy) {
        return new InsurancePolicyDto(
                insurancePolicy.getCar().getId(), insurancePolicy.getProvider(), insurancePolicy.getStartDate(), insurancePolicy.getEndDate());
    }

}
