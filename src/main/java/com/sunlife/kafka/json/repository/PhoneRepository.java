package com.sunlife.kafka.json.repository;

import com.sunlife.kafka.json.jpa.PhoneObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepository extends JpaRepository<PhoneObject,Integer> {
}
