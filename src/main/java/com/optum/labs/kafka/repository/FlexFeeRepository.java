package com.optum.labs.kafka.repository;

import com.optum.labs.kafka.entity.FlexFeeActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlexFeeRepository extends JpaRepository<FlexFeeActivity,Long> {


}
