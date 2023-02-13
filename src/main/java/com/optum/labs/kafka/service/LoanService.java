package com.optum.labs.kafka.service;

import com.optum.labs.kafka.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;

}
