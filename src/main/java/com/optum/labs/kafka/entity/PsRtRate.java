package com.optum.labs.kafka.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.NavigableMap;

@Entity
@Table(name = "PS_RT_RATE_TBL")
public class PsRtRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "FROM_CUR", length = 9)
    private String from_cur;
    @Column(name = "TO_CUR", length = 9)
    private String to_cur;
    @Column(name = "EFFDT")
    private Date effdt;
    @Column(name = "SVB_RATE")
    private double svb_rate;
}
