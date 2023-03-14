package com.optum.labs.kafka.controller;

import com.optum.labs.kafka.entity.CanDelete;
import com.optum.labs.kafka.streams.TestingStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class Testing {

    @Autowired
    private TestingStream testingStream;

    @GetMapping("/test")
    public String test(){
        return "testing";
    }

    @GetMapping("/result")
    public ResponseEntity<List<CanDelete>> getResult(@RequestParam("startDate")@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate startDate,
                                                    @RequestParam("endDate")@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate endDate){
        System.out.println("start date: "+startDate);
        System.out.println("End date: "+endDate);
        List<CanDelete> data= testingStream.getDataBetweenDates(startDate,endDate);
        return ResponseEntity.ok(data);
    }
}
