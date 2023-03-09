package com.optum.labs.kafka.entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "FLEX_ACTIVITY")
@Data
public class FlexActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("ID")
    private Long id;

    @Column(name = "CUST_LINE_NBR")
    @JsonProperty("CUST_LINE_NBR")
    private String customerLineNumber;

//    @Column(name = "POSTDT")
//    @JsonProperty("POSTDT")
//    @Temporal(TemporalType.DATE)
//    private Date postDt;

    @Column(name = "POSTDT")
    @JsonProperty("POSTDT")
    private String postDt;
}
