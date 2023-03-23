package com.optum.labs.kafka.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "TEST_HOLIDAY_CALENDAR")
@Data
public class TestHolidayCalendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "BRANCH_HOLIDAY_DT")
    private String branchHolidayDt;

}
