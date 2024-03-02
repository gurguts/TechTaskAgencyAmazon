package com.example.techtaskagencyamazon.models.report.salesAndTrafficByDate;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SalesAndTrafficByDate {
    private LocalDate date;
    private Object salesByDate;
    private Object trafficByDate;
}
