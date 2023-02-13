package com.optum.labs.kafka.repository;

import com.optum.labs.kafka.entity.Loan;
import com.optum.labs.kafka.entity.PsRtRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PsRateRepository extends JpaRepository<PsRtRate, Loan> {
}
