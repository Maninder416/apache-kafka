package com.optum.labs.kafka.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "table3")
@Data
public class CanDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("ID")
    private Long id;

    @Column(name = "first_name",length = 10)
    @JsonProperty("first_name")
    private String first_name;

    @Column(name = "last_name",length = 10)
    @JsonProperty("last_name")
    private String last_name;
}
