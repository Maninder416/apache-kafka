package com.optum.labs.kafka.repository;

import com.optum.labs.kafka.entity.TestLoanTransHist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestLoanTransHistRepository extends JpaRepository<TestLoanTransHist,Long> {
}
