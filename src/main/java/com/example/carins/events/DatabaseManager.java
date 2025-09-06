package com.example.carins.events;

import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.InsurancePolicyRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DatabaseManager {

    private final InsurancePolicyRepository repository;
    private final EntityManager entityManager;

    public DatabaseManager(InsurancePolicyRepository repository, EntityManager entityManager)
    {
        this.repository =  repository;
        this.entityManager = entityManager;
    }
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void addConstraintsToDatabase()
    {
        repository.findAll().forEach(policy -> {
            if (policy.getEndDate() == null) {
                policy.setEndDate(policy.getStartDate().plusYears(1));
                repository.save(policy);
            }
        });

        entityManager.createNativeQuery(
                "ALTER TABLE insurancepolicy ALTER COLUMN end_date SET NOT NULL"
        ).executeUpdate();

    }
}
