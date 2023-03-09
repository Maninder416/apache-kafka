package com.optum.labs.kafka.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CanDelete")
@Data
public class CanDelete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String date;
    @Temporal(TemporalType.DATE)
    private Date birthDate;
}
