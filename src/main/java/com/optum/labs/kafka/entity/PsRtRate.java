package com.optum.labs.kafka.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.NavigableMap;

@Entity
@Table(name = "PS_RT_RATE_TBL")
@Data
public class PsRtRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "FROM_CUR", length = 9)
    private String from_cur;
    @Column(name = "TO_CUR", length = 9)
    private String to_cur;
//    @Column(name = "EFFDT")
//    @Temporal(TemporalType.DATE)
//    private Date effdt;

    @Column(name = "EFFDT")
    private String effdt;
    @Column(name = "SVB_RATE")
    private double svb_rate;
}
