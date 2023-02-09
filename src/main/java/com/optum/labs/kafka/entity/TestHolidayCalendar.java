package com.optum.labs.kafka.entity;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "TEST_HOLIDAY_CALENDAR")
public class TestHolidayCalendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "BRANCH_HOLIDAY_DT")
    private Date branchHolidayDt;

}
