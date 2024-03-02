package com.example.techtaskagencyamazon.restControllers;

import com.example.techtaskagencyamazon.interfaces.AsinValidatorInterface;
import com.example.techtaskagencyamazon.interfaces.ReportServiceInterface;
import com.example.techtaskagencyamazon.models.report.salesAndTrafficByAsin.SalesAndTrafficByAsin;
import com.example.techtaskagencyamazon.models.report.salesAndTrafficByDate.SalesAndTrafficByDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for receiving report data.
 */
@RestController
@RequestMapping("api/v1/reports")
public class ReportController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
    private final ReportServiceInterface reportService;
    private final AsinValidatorInterface asinValidator;

    public ReportController(ReportServiceInterface reportService, AsinValidatorInterface asinValidator) {
        this.reportService = reportService;
        this.asinValidator = asinValidator;
    }

    /**
     * Method for obtaining report data by date.
     *
     * @param date The date for which you want to get data.
     * @return Report data for the specified date.
     */
    @GetMapping("/date/{date}")
    public SalesAndTrafficByDate findByDate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        logger.info("Fetching reports by date: {}", date);
        return reportService.findByDate(date);
    }

    /**
     * Method for obtaining report data for a specified period.
     *
     * @param startDate Start date of the period.
     * @param endDate   The end date of the period.
     * @return List of report data for the specified period.
     */
    @GetMapping("/date/{startDate}/{endDate}")
    public List<SalesAndTrafficByDate> findByDateBetween(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        logger.info("Fetching reports between dates: {} and {}", startDate, endDate);
        return reportService.findByDateBetween(startDate, endDate);
    }

    /**
     * Method for obtaining report data by ASIN.
     *
     * @param asin ASIN by which to get data.
     * @return Report data for the specified ASIN.
     */
    @GetMapping("/asin/{asin}")
    public SalesAndTrafficByAsin findByAsin(@PathVariable String asin) {
        asinValidator.validate(asin);
        logger.info("Fetching reports by ASIN: {}", asin);
        return reportService.findByAsin(asin);
    }

    /**
     * Method for obtaining report data for multiple ASINs.
     *
     * @param asins List of ASINs for which data should be retrieved.
     * @return List of report data for the specified ASINs.
     */
    @PostMapping("/asin")
    public List<SalesAndTrafficByAsin> findByAsinIn(@RequestBody List<String> asins) {
        asinValidator.validate(asins);
        logger.info("Fetching reports by multiple ASINs");
        return reportService.findByAsinIn(asins);
    }

    /**
     * Method for retrieving all report data by date.
     *
     * @return List all report data by date.
     */
    @GetMapping("/salesAndTrafficByDate")
    public List<SalesAndTrafficByDate> findAllBySalesAndTrafficByDate() {
        logger.info("Fetching all reports by sales and traffic by date");
        return reportService.findAllBySalesAndTrafficByDate();
    }

    /**
     * Method to retrieve all report data by ASIN.
     *
     * @return List all report data by ASIN.
     */
    @GetMapping("/salesAndTrafficByAsin")
    public List<SalesAndTrafficByAsin> findAllBySalesAndTrafficByAsin() {
        logger.info("Fetching all reports by sales and traffic by ASIN");
        return reportService.findAllBySalesAndTrafficByAsin();
    }
}


