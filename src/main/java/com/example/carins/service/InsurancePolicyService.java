package com.example.carins.service;

import com.example.carins.exception.DateOutOfRangeException;
import com.example.carins.exception.InvalidInsuranceException;
import com.example.carins.exception.ResourceNotFoundException;
import com.example.carins.model.Car;
import com.example.carins.model.InsuranceClaim;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.web.dto.InsurancePolicyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class InsurancePolicyService {

    private final InsurancePolicyRepository repository;
    private final CarRepository carRepository;

    @Autowired
    public InsurancePolicyService(InsurancePolicyRepository repository, CarRepository carRepository)
    {
        this.repository = repository;
        this.carRepository = carRepository;
    }

    public InsurancePolicy getInsuranceClaimById(Long id)
    {
        Optional<InsurancePolicy> claimOptional = repository.findById(id);
        if(claimOptional.isEmpty()) throw new ResourceNotFoundException("No insurance policy with id: " + id + " found");
        return claimOptional.get();
    }
    public InsurancePolicy createInsurancePolicy(InsurancePolicyDto dto)
    {
        this.validateDto(dto);
        InsurancePolicy newInsurance = this.convertDto(dto);

        return repository.save(newInsurance);
    }

    private void validateDto(InsurancePolicyDto dto)
    {
        if(carRepository.findCarById(dto.carId()).isEmpty())
            throw new ResourceNotFoundException("Car with id : " + dto.carId() + " not found");

        if(dto.endDate().isBefore(dto.startDate()))
            throw new DateOutOfRangeException("Start date must precede end date");

        if(dto.startDate().isBefore(LocalDate.now().minusDays(1)))
            throw new DateOutOfRangeException("Start date should not be a past date");

        if(dto.endDate().isBefore(LocalDate.now()))
            throw new DateOutOfRangeException("End date should not be a past date");

        if(dto.endDate().isAfter(LocalDate.now().plusYears(1))) //SET MAX PERIOD OF ONE YEARS FOR INSURANCES
            throw new DateOutOfRangeException("End date should be at max one year from now");

        boolean isInsuranceValid = repository.findAllByCar_Id(dto.carId()).stream()
                .allMatch(insurance ->
                dto.startDate().isAfter(insurance.getEndDate()) ||
                            dto.endDate().isBefore(insurance.getStartDate()));
        if(!isInsuranceValid)
            throw new InvalidInsuranceException("An insurance already exists for this car.");
    }

    private InsurancePolicy convertDto(InsurancePolicyDto dto) {
        Optional<Car> car = carRepository.findCarById(dto.carId());

        if(car.isPresent()) return new InsurancePolicy(car.get(), dto.provider(), dto.startDate(), dto.endDate());

        throw new InvalidInsuranceException("No car with current id: " + dto.carId());
    }


}
