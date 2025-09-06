package com.example.carins.web;

import com.example.carins.model.Car;
import com.example.carins.model.InsuranceClaim;
import com.example.carins.service.CarService;
import com.example.carins.service.InsuranceClaimService;
import com.example.carins.web.dto.CarDto;
import com.example.carins.web.dto.InsuranceClaimDto;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CarController {

    private final CarService service;
    private final InsuranceClaimService insuranceClaimService;

    public CarController(CarService service, InsuranceClaimService insuranceClaimService) {
        this.service = service;
        this.insuranceClaimService = insuranceClaimService;
    }

    @GetMapping("/cars")
    public List<CarDto> getCars() {
        return service.listCars().stream().map(this::toDto).toList();
    }

    @GetMapping("/cars/{carId}/insurance-valid")
    public ResponseEntity<?> isInsuranceValid(@PathVariable Long carId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        // DONE: validate date format and handle errors consistently
        boolean valid = service.isInsuranceValid(carId, date);
        return ResponseEntity.ok(new InsuranceValidityResponse(carId, date.toString(), valid));
    }

    @PostMapping("/cars/{carId}/claims")
    public ResponseEntity<?> createInsuranceClaim(@PathVariable Long carId, @Valid @RequestBody InsuranceClaimDto insuranceClaim)
    {
        InsuranceClaim savedClaim = insuranceClaimService.createInsuranceClaim(carId, insuranceClaim);
        URI location = URI.create("/api/cars/" + savedClaim.getId() + "/claims");
        InsuranceClaimDto response = this.toDto(savedClaim);
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/cars/{id}/claims")
    public ResponseEntity<?> getInsuranceClaim(@PathVariable Long id)
    {
        return ResponseEntity.ok(this.toDto(insuranceClaimService.getInsuranceClaimById(id)));
    }

    @GetMapping("/cars/{carId}/history")
    public ResponseEntity<?> getCarHistory(@PathVariable Long carId)
    {
        List<InsuranceClaimDto> history = insuranceClaimService.getCarHistory(carId).stream().map(this::toDto).toList();
        return ResponseEntity.ok(history);
    }

    private CarDto toDto(Car c) {
        var o = c.getOwner();
        return new CarDto(c.getId(), c.getVin(), c.getMake(), c.getModel(), c.getYearOfManufacture(),
                o != null ? o.getId() : null,
                o != null ? o.getName() : null,
                o != null ? o.getEmail() : null);
    }

    private InsuranceClaimDto toDto(InsuranceClaim claim)
    {
        return new InsuranceClaimDto(claim.getClaimDate(), claim.getAmount(), claim.getDescription());
    }

    public record InsuranceValidityResponse(Long carId, String date, boolean valid) {}
}
