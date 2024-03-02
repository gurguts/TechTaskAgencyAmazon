package com.example.techtaskagencyamazon.models.report;

import com.example.techtaskagencyamazon.models.report.reportSpecification.ReportSpecification;
import com.example.techtaskagencyamazon.models.report.salesAndTrafficByAsin.SalesAndTrafficByAsin;
import com.example.techtaskagencyamazon.models.report.salesAndTrafficByDate.SalesAndTrafficByDate;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Model for report
 */
@Data
@Document(collection = "reports")
public class Report {

    @Id
    private String id;

    private ReportSpecification reportSpecification;
    private List<SalesAndTrafficByDate> salesAndTrafficByDate;
    private List<SalesAndTrafficByAsin> salesAndTrafficByAsin;
}
