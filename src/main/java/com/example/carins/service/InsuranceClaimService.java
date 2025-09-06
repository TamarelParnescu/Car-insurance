package com.example.carins.service;

import com.example.carins.exception.InvalidInsuranceException;
import com.example.carins.exception.ResourceNotFoundException;
import com.example.carins.model.InsuranceClaim;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.InsuranceClaimRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.web.dto.InsuranceClaimDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class InsuranceClaimService {

    private final InsuranceClaimRepository repository;
    private final InsurancePolicyRepository insurancePolicyRepository;

    @Autowired
    public InsuranceClaimService(InsuranceClaimRepository repository, InsurancePolicyRepository insurancePolicyRepository)
    {
        this.repository = repository;
        this.insurancePolicyRepository = insurancePolicyRepository;
    }

    public InsuranceClaim getInsuranceClaimById(Long id)
    {
        Optional<InsuranceClaim> claimOptional = repository.findById(id);
        if(claimOptional.isEmpty()) throw new ResourceNotFoundException("No insurance claim with id: " + id + " found");
        return claimOptional.get();
    }

    public InsuranceClaim createInsuranceClaim(Long carId, InsuranceClaimDto dto){
        InsurancePolicy insurancePolicy = getActiveInsuranceAtClaimDate(carId, dto);
        return repository.save(convertDto(dto, insurancePolicy));
    }

    public List<InsuranceClaim> getCarHistory(Long carId)
    {
        List<InsurancePolicy> insurancePolicyList = insurancePolicyRepository.findAllByCar_Id(carId);

        List<InsuranceClaim> history = insurancePolicyList.stream().flatMap(policy -> repository.findAllByInsurancePolicyId(policy.getId()).stream()).sorted(Comparator.comparing(InsuranceClaim::getClaimDate)).toList();
        System.out.println(history);
        return history;
    }

    private InsurancePolicy getActiveInsuranceAtClaimDate(Long carId, InsuranceClaimDto dto)
    {
        Optional<InsurancePolicy> insurancePolicyOptional = insurancePolicyRepository.findAllByCar_Id(carId).stream().filter(
                insurancePolicy -> insurancePolicy.getStartDate().isBefore(dto.claimDate())
                        && insurancePolicy.getEndDate().isAfter(dto.claimDate())
        ).findFirst();

        if(insurancePolicyOptional.isPresent())
            return insurancePolicyOptional.get();

        throw new InvalidInsuranceException("Car is not insured at the claim date");
    }

    private InsuranceClaim convertDto(InsuranceClaimDto dto, InsurancePolicy insurancePolicy)
    {
        return new InsuranceClaim(dto.claimDate(), dto.description(), dto.amount(), insurancePolicy);
    }

}
