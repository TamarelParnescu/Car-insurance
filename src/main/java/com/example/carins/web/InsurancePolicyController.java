package com.example.carins.web;

import com.example.carins.model.InsurancePolicy;
import com.example.carins.service.InsurancePolicyService;
import com.example.carins.web.dto.InsurancePolicyDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class InsurancePolicyController {
    private final InsurancePolicyService service;

    @Autowired
    public InsurancePolicyController(InsurancePolicyService service)
    {
        this.service = service;
    }

    @PostMapping("/insurance-policy")
    public ResponseEntity<?> createInsurance(@Valid @RequestBody InsurancePolicyDto insurancePolicyDto)
    {
        InsurancePolicy savedInsurancePolicy = service.createInsurancePolicy(insurancePolicyDto);
        URI location = URI.create("/api/insurance-policy/" + savedInsurancePolicy.getId());
        return ResponseEntity.created(location).body(toDto(savedInsurancePolicy));
    }

    @GetMapping("/insurance-policy/{id}")
    public ResponseEntity<?> getPolicy(@PathVariable Long id)
    {
        return ResponseEntity.ok(this.toDto(service.getInsuranceClaimById(id)));
    }

    private InsurancePolicyDto toDto(InsurancePolicy policy) {
        return new InsurancePolicyDto(
                policy.getCar().getId(), policy.getProvider(), policy.getStartDate(), policy.getEndDate());
    }

}
