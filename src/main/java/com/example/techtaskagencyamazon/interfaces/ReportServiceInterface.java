package com.example.techtaskagencyamazon.interfaces;

import com.example.techtaskagencyamazon.models.report.salesAndTrafficByAsin.SalesAndTrafficByAsin;
import com.example.techtaskagencyamazon.models.report.salesAndTrafficByDate.SalesAndTrafficByDate;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface for the reporting service. It provides methods to retrieve sales and traffic data by date and ASIN.
 */
public interface ReportServiceInterface {

    SalesAndTrafficByDate findByDate(LocalDate date);

    List<SalesAndTrafficByDate> findByDateBetween(LocalDate startDate, LocalDate endDate);

    SalesAndTrafficByAsin findByAsin(String asin);

    List<SalesAndTrafficByAsin> findByAsinIn(List<String> asins);

    List<SalesAndTrafficByDate> findAllBySalesAndTrafficByDate();

    List<SalesAndTrafficByAsin> findAllBySalesAndTrafficByAsin();
}
