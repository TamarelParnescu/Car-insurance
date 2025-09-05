package com.example.carins.web;

import com.example.carins.exception.InvalidInsuranceException;
import com.example.carins.service.InsurancePolicyService;
import com.example.carins.web.dto.InsurancePolicyDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class InsurancePolicyController {
    InsurancePolicyService service;

    @Autowired
    public InsurancePolicyController(InsurancePolicyService service)
    {
        this.service = service;
    }

    @PostMapping("/insurance-policy")
    public ResponseEntity<?> createInsurance(@Valid @RequestBody InsurancePolicyDto insurancePolicyDto)
    {
        try{
            return ResponseEntity.ok(service.createInsurance(insurancePolicyDto));
        }
        catch (InvalidInsuranceException e)
        {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
        catch (Exception e)
        {
            Map<String, String> response = new HashMap<>();
            response.put("error", "An unexpected error occurred");
            return ResponseEntity.status(500).body(response);
        }
    }
}
