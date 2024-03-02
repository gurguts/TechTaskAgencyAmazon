package com.example.techtaskagencyamazon.models.report.salesAndTrafficByAsin;

import lombok.Data;

@Data
public class SalesAndTrafficByAsin {
    private String parentAsin;
    private Object salesByAsin;
    private Object trafficByAsin;
}
