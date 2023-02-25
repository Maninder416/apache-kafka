package com.optum.labs.kafka.repository;

import com.optum.labs.kafka.entity.FlexActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlexActivityRepository extends JpaRepository<FlexActivity,Long> {
}
