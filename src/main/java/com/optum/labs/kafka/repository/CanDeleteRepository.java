package com.optum.labs.kafka.repository;

import com.optum.labs.kafka.entity.CanDelete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CanDeleteRepository extends JpaRepository<CanDelete, Long> {
}
