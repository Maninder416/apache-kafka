package com.optum.labs.kafka.repository;

import com.optum.labs.kafka.entity.BpaUlfProductCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BpaProductCodeRepository extends JpaRepository<BpaUlfProductCodes,Long> {
}
