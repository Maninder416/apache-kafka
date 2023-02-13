package com.optum.labs.kafka.repository;

import com.optum.labs.kafka.entity.CreditLines;
import com.optum.labs.kafka.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditLineRepository extends JpaRepository<CreditLines, Long> {
}
