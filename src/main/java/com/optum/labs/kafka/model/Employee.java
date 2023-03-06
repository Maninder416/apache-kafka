package com.optum.labs.kafka.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Employee")
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer empId;
    private String name;
    private String company;
    private Integer sin;
    private String department;
    private String status;
}
