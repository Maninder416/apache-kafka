package com.optum.labs.kafka.repository;

import com.optum.labs.kafka.model.StockHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockHistoryRepository extends JpaRepository<StockHistoryEntity,Long> {
}
