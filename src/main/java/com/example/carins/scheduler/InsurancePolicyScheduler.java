package com.example.carins.scheduler;

import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.InsurancePolicyRepository;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class InsurancePolicyScheduler {


    private final InsurancePolicyRepository repository;

    @Autowired
    public InsurancePolicyScheduler(InsurancePolicyRepository repository)
    {
        this.repository = repository;
    }
    @Async
    @Scheduled(cron = "0 30 0 * * *")
    public void testSchedule()
    {
        List<InsurancePolicy> insurancePolicies = repository.findAll();

        insurancePolicies.stream()
                .filter(policy -> policy.getEndDate().equals(LocalDate.now().minusDays(1)))
                .forEach(policy -> System.out.println("Policy " + policy.getId() +" for car " + policy.getCar().getId() + " expired on " + policy.getEndDate()));
    }
}
