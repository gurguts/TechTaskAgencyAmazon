package com.example.techtaskagencyamazon.services;

import com.example.techtaskagencyamazon.exception.ReportException;
import com.example.techtaskagencyamazon.interfaces.ReportServiceInterface;
import com.example.techtaskagencyamazon.models.report.Report;
import com.example.techtaskagencyamazon.models.report.salesAndTrafficByAsin.SalesAndTrafficByAsin;
import com.example.techtaskagencyamazon.models.report.salesAndTrafficByDate.SalesAndTrafficByDate;
import com.example.techtaskagencyamazon.repositories.ReportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for working with reports.
 */
@Service
public class ReportService implements ReportServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /**
     * Method for obtaining report data by date.
     *
     * @param date The date for which you want to get data.
     * @return Report data for the specified date.
     */
    @Cacheable(value = "reportsByDate", key = "#date")
    public SalesAndTrafficByDate findByDate(LocalDate date) {
        logger.info("Fetching reports by date: {}", date);
        List<Report> reportList = reportRepository.findBySalesAndTrafficByDate_Date(date);
        if (reportList.isEmpty() || reportList.get(0).getSalesAndTrafficByDate().isEmpty()) {
            throw new ReportException("There are no records with date " + date);
        }
        return reportList.get(0).getSalesAndTrafficByDate().get(0);
    }

    /**
     * Method for obtaining report data for a specified period.
     *
     * @param startDate Start date of the period.
     * @param endDate   The end date of the period.
     * @return List of report data for the specified period.
     */
    @Cacheable(value = "reportsByDateBetween", key = "#startDate.toString() + #endDate.toString()")
    public List<SalesAndTrafficByDate> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        try {
            logger.info("Fetching reports between dates: {} and {}", startDate, endDate);
            List<Report> reportList = reportRepository.findAllBySalesAndTrafficByDate();
            return reportList.stream()
                    .flatMap(report -> report.getSalesAndTrafficByDate().stream())
                    .filter(salesAndTrafficByDate ->
                            (salesAndTrafficByDate.getDate().isEqual(startDate) ||
                                    salesAndTrafficByDate.getDate().isAfter(startDate)) &&
                                    (salesAndTrafficByDate.getDate().isEqual(endDate) ||
                                            salesAndTrafficByDate.getDate().isBefore(endDate))
                    )
                    .collect(Collectors.toList());
        } catch (Exception exception) {
            throw new ReportException(exception + " Error occurred while fetching reports between dates:" +
                    startDate + " and " + endDate);
        }
    }

    /**
     * Method for obtaining report data by ASIN.
     *
     * @param asin ASIN by which to get data.
     * @return Report data for the specified ASIN.
     */
    @Cacheable(value = "reportsByAsin", key = "#asin")
    public SalesAndTrafficByAsin findByAsin(String asin) {
        logger.info("Fetching reports by ASIN: {}", asin);
        List<Report> reportList = reportRepository.findBySalesAndTrafficByAsin_Asin(asin);
        if (reportList.isEmpty() || reportList.get(0).getSalesAndTrafficByAsin().isEmpty()) {
            throw new ReportException("There are no records with ASIN " + asin);
        }
        return reportList.get(0).getSalesAndTrafficByAsin().get(0);
    }

    /**
     * Method for obtaining report data for multiple ASINs.
     *
     * @param asins List of ASINs for which data should be retrieved.
     * @return List of report data for the specified ASINs.
     */
    @Cacheable(value = "reportsByAsinIn", key = "#asins")
    public List<SalesAndTrafficByAsin> findByAsinIn(List<String> asins) {
        try {
            logger.info("Fetching reports by multiple ASINs");
            List<Report> reportList = reportRepository.findAllBySalesAndTrafficByAsin();
            return reportList.stream()
                    .flatMap(report -> report.getSalesAndTrafficByAsin().stream())
                    .filter(salesAndTrafficByAsin -> asins.contains(salesAndTrafficByAsin.getParentAsin()))
                    .collect(Collectors.toList());
        } catch (Exception exception) {
            throw new ReportException("Error occurred while fetching reports by multiple ASINs");
        }
    }

    /**
     * Method for retrieving all report data by date.
     *
     * @return List all report data by date.
     */
    @Cacheable(value = "allReportsByDate")
    public List<SalesAndTrafficByDate> findAllBySalesAndTrafficByDate() {
        try {
            logger.info("Fetching all reports by sales and traffic by date");
            List<Report> reportList = reportRepository.findAllBySalesAndTrafficByDate();
            return reportList.stream()
                    .flatMap(report -> report.getSalesAndTrafficByDate().stream())
                    .collect(Collectors.toList());
        } catch (Exception exception) {
            throw new ReportException("Error occurred while fetching all reports by sales and traffic by date");
        }
    }

    /**
     * Method to retrieve all report data by ASIN.
     *
     * @return List all report data by ASIN.
     */
    @Cacheable(value = "allReportsByAsin")
    public List<SalesAndTrafficByAsin> findAllBySalesAndTrafficByAsin() {
        try {
            logger.info("Fetching all reports by sales and traffic by ASIN");
            List<Report> reportList = reportRepository.findAllBySalesAndTrafficByAsin();
            return reportList.stream()
                    .flatMap(report -> report.getSalesAndTrafficByAsin().stream())
                    .collect(Collectors.toList());
        } catch (Exception exception) {
            throw new ReportException("Error occurred while fetching all reports by sales and traffic by ASIN");
        }
    }
}

