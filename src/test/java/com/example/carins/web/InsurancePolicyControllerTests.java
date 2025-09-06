package com.example.carins.web;

import com.example.carins.web.dto.InsuranceClaimDto;
import com.example.carins.web.dto.InsurancePolicyDto;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InsurancePolicyControllerTests {
        @LocalServerPort
        private int port;

        private final String apiPort = "http://localhost:" + port;
        @Autowired
        private TestRestTemplate restTemplate;

        @Test
        void testGetInsurancePolicy() {
            Long validId = 1L;
            Long invalidId = 4L;

            ResponseEntity<String> response = restTemplate.getForEntity(apiPort + "/api/insurance-policy/{id}", String.class, validId);
            assertEquals(HttpStatus.OK, response.getStatusCode());

            ResponseEntity<Map> response1 = restTemplate.getForEntity("http://localhost:" + port + "/api/insurance-policy/{id}", Map.class, invalidId);
            assertEquals(HttpStatus.NOT_FOUND, response1.getStatusCode());
            assertEquals("No insurance policy with id: 4 found", response1.getBody().get("error"));
        }

        @Test
        void testPostInsurancePolicy()
        {
            InsurancePolicyDto insurancePolicyDto = new InsurancePolicyDto(1L, "ING", LocalDate.of(2021,1,1),LocalDate.of(2022,1,1)); //Continue Test Tomorrow
        }
}
